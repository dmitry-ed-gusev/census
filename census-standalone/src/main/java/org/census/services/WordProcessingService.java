package org.census.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.census.commons.dao.hibernate.personnel.PositionsSimpleDao;
import org.census.commons.dto.personnel.ContactTypeDto;
import org.census.commons.dto.personnel.PositionDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service for processing phone book from word file.
 * Created for Panfilov Konstantin, 16.08.2016
 * <p>
 * Created by vinnypuhh on 16.08.2016.
 */

@Service
@Transactional
public class WordProcessingService {

    private static final String DELIMETER = StringUtils.repeat("=", 100);

    @SuppressWarnings("ConstantNamingConvention")
    private static final Log LOG = LogFactory.getLog(WordProcessingService.class);

    @Autowired @Qualifier(value = "wordPhoneBookFile")
    private String phoneBookFile;
    @Autowired
    private PositionsSimpleDao positionsDao;

    public String getPhoneBookFile() {
        return phoneBookFile;
    }

    public void setPhoneBookFile(String phoneBookFile) {
        this.phoneBookFile = phoneBookFile;
    }

    public PositionsSimpleDao getPositionsDao() {
        return positionsDao;
    }

    public void setPositionsDao(PositionsSimpleDao positionsDao) {
        this.positionsDao = positionsDao;
    }

    /***/
    public void loadPhoneBook() throws IOException {
        LOG.debug(String.format("Started loading phone book from [%s] file.", this.phoneBookFile));

        // prepare contacts types (hardcoded)
        // todo: create Spring config and move it there
        ContactTypeDto emailContact = new ContactTypeDto(0, "email");
        ContactTypeDto phoneContact = new ContactTypeDto(0, "phone");
        ContactTypeDto addressContact = new ContactTypeDto(0, "address");

        // input stream for word document
        FileInputStream ins = new FileInputStream(this.phoneBookFile);
        // object for word document
        HWPFDocument doc = new HWPFDocument(ins);
        // main object for word document
        Range range = doc.getRange();
        // iterator over tables
        TableIterator itr = new TableIterator(range);
        int tablesCounter = 0;
        int columnsCount;
        String[] tmpStrArray; // temporary, for raw cell content
        PositionDto position;
        EmployeeDto employee;
        List<EmployeeDto> employeesList = new ArrayList<>();
        while (itr.hasNext()) { // iterate over found tables
            System.out.println(String.format("\n%s\ntable #%s\n%s", DELIMETER, tablesCounter, DELIMETER));

            Table table = itr.next();
            for (int rowIndex = 0; rowIndex < table.numRows(); rowIndex++) { // process a whole row
                TableRow row = table.getRow(rowIndex);
                columnsCount = row.numCells();
                System.out.println(String.format("row #%s (cells #%s)", rowIndex, columnsCount));

                if (columnsCount == 1) { // procesing header
                    // todo: for join array use StringUtils.join()!
                    System.out.print("HEADER CELL => ");
                    System.out.println(Arrays.toString(WordProcessingService.getCellContent(row.getCell(0))));
                    //TableCell headerCell = row.getCell(0);
                } else if (columnsCount == 3) { // regular row with 3 columns (position-name-phone)
                    // (column #0) read position
                    tmpStrArray = WordProcessingService.getCellContent(row.getCell(0));
                    position = new PositionDto(0, StringUtils.join(tmpStrArray, " "));
                    this.positionsDao.saveOrUpdate(position);

                    // (column #2) read full name
                    tmpStrArray = WordProcessingService.getCellContent(row.getCell(1));
                    employee = new EmployeeDto(0, StringUtils.join(tmpStrArray, " "));
                    employee.setOnlyPosition(position);
                    // (column #3) read contact info (phone/email/address)
                    tmpStrArray = WordProcessingService.getCellContent(row.getCell(2));
                    // todo: contacts parsing!!!
                    System.out.print("CONTACTS CELL => ");
                    System.out.println(Arrays.toString(tmpStrArray));
                    // adding found employee to resulting list
                    employeesList.add(employee);
                } else { // some unusual case
                    LOG.warn(String.format("Unusual case: columns count [%s].", columnsCount));
                }

                /*
                        for(int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
                            TableCell cell = row.getCell(columnIndex);
                            System.out.println(String.format("column #%s", columnIndex));
                            //System.out.println(cell.getParagraph(0).text());
                            //System.out.println(cell.numParagraphs());
                            for (int i = 0; i < cell.numParagraphs(); i++) {
                                Paragraph paragraph = cell.getParagraph(i);
                                //System.out.println(cell.getParagraph(i).text());
                                System.out.println(paragraph.text());
                                System.out.print("|");
                            }
                        } // end of FOR (columns processing)
                        */
            }
            tablesCounter++;
        } // end of while

        // debug output list of employees
        employeesList.forEach(emp -> System.out.println("->" + emp));

    }

    /**
     * Helper method: get cell contants as array of strings.
     */
    private static String[] getCellContent(TableCell cell) {

        if (cell == null) { // fail-fast
            throw new IllegalArgumentException("Can't read null cell!");
        }

        // processing not-empty table cell
        String[] result = new String[cell.numParagraphs()];
        for (int i = 0; i < cell.numParagraphs(); i++) {
            result[i] = StringUtils.trimToEmpty(cell.getParagraph(i).text());
        }
        return result;
    }

}

package org.census.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.census.commons.dao.hibernate.personnel.ContactsSimpleDao;
import org.census.commons.dao.hibernate.personnel.DepartmentsSimpleDao;
import org.census.commons.dao.hibernate.personnel.EmployeesSimpleDao;
import org.census.commons.dao.hibernate.personnel.PositionsSimpleDao;
import org.census.commons.dto.ContactType;
import org.census.commons.dto.personnel.ContactDto;
import org.census.commons.dto.personnel.PositionDto;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for processing phone book from word file.
 * Created for Panfilov Konstantin, 16.08.2016
 * <p>
 * Created by vinnypuhh on 16.08.2016.
 */

@Service
@Transactional
// todo: add package for service and create helper classes
public class WordProcessingService {

    private static final String DELIMETER = StringUtils.repeat("=", 100);

    @SuppressWarnings("ConstantNamingConvention")
    private static final Log LOG = LogFactory.getLog(WordProcessingService.class);

    @Autowired @Qualifier(value = "wordPhoneBookFile")
    private String phoneBookFile; // file with phonebook in MS Word format

    @Autowired private ContactsSimpleDao      contactsDao;      // DAO for contacts
    @Autowired private PositionsSimpleDao     positionsDao;     // DAO for positions
    @Autowired private DepartmentsSimpleDao   departmemtsDao;   // DAO for departments
    @Autowired private EmployeesSimpleDao     employeesDao;     // DAO for employees

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

        String[]      tmpStrArray; // temporary, for raw cell content
        String        tmpStr;      // temporary string
        PositionDto   position;    // temporary position object
        ContactDto    contact;     // temporary contact object
        DepartmentDto department;  // temporary department
        EmployeeDto employee;

        // todo: !!!!
        //employee = new EmployeeDto(0, "ff nn pp");
        //System.out.println("===> " + this.employeesDao.getEmployee(employee));
        //System.out.println("===> " + this.employeesDao.findById(22L));
        //System.out.println("===> " + this.employeesDao.getEmployeeByLogin("ddd"));
        //System.exit(777);
        // todo: !!!!

        List<EmployeeDto> employeesList = new ArrayList<>();
        while (itr.hasNext()) { // iterate over found tables
            System.out.println(String.format("\n%s\ntable #%s\n%s", DELIMETER, tablesCounter, DELIMETER));

            Table table = itr.next();
            for (int rowIndex = 0; rowIndex < table.numRows(); rowIndex++) { // process a whole row
                TableRow row = table.getRow(rowIndex);
                columnsCount = row.numCells();
                System.out.println(String.format("row #%s (cells #%s)", rowIndex, columnsCount));

                if (columnsCount == 1) { // row with 1 column (department)
                    tmpStrArray = WordProcessingService.getCellContent(row.getCell(0));
                    tmpStr = StringUtils.join(tmpStrArray, " ").replaceAll("\\s+", " ");
                    //department = (!StringUtils.isBlank(tmpStr) ? this.departmemtsDao.addDepartmentByName(tmpStr) : null);
                } else if (columnsCount == 3) { // regular row with 3 columns (position-name-phone)
                    // (column #0 (1st)) read and save position
                    tmpStrArray = WordProcessingService.getCellContent(row.getCell(0));
                    // prepare position name/object (join contents of cell and remove unnesessary spaces between words) and add it to db
                    tmpStr = StringUtils.join(tmpStrArray, " ").replaceAll("\\s+", " ");
                    // todo: !!!
                    if (!StringUtils.isBlank(tmpStr)) {
                        position = this.positionsDao.addPositionByName(tmpStr);
                    } else {
                        position = null;
                    }

                    // (column #2 (3rd)) read contact info (phone/email/address)
                    tmpStrArray = WordProcessingService.getCellContent(row.getCell(2));
                    // join contents and remove more than one space between words
                    tmpStr = StringUtils.join(tmpStrArray, " ").replaceAll("\\s+", " ");
                    // todo: !!!
                    if (!StringUtils.isBlank(tmpStr)) {
                        contact = new ContactDto(0, tmpStr, null, ContactType.OTHER);
                        this.contactsDao.save(contact);
                    } else {
                        contact = null;
                    }

                    // (column #1 (2nd)) read and save employee full name
                    tmpStrArray = WordProcessingService.getCellContent(row.getCell(1));
                    tmpStr = StringUtils.join(tmpStrArray, " ").replaceAll("\\s+", " ");

                    //employee = new EmployeeDto(0, StringUtils.join(tmpStrArray, " "));
                    //if (position != null) { // set the only position, if it isn't null
                    //    employee.setOnlyPosition(position);
                    //}


                    // adding found employee to resulting list
                    //employeesList.add(employee);
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

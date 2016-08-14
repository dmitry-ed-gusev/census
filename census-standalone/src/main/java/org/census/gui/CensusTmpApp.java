package org.census.gui;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.census.commons.dto.personnel.ContactTypeDto;
import org.census.commons.dto.personnel.PositionDto;
import org.census.commons.dto.personnel.employees.EmployeeDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Temporary application for
 * Created by vinnypuhh on 07.08.2016.
 */
 // http://svn.apache.org/repos/asf/tika/trunk/tika-parsers/src/main/java/org/apache/tika/parser/microsoft/WordExtractor.java

public class CensusTmpApp {

    private static final Log LOG = LogFactory.getLog(CensusTmpApp.class);

    private static final String DELIMETER = StringUtils.repeat("=", 100);

    public static void main(String[] args) throws IOException {
        LOG.info("Starting...");
        /*
        // file object was created
        //File docFile = new File("c:\\temp\\phonebook.doc");
        // file input stream with docFile
        FileInputStream finStream=new FileInputStream("c:\\temp\\phonebook.doc");

        // throws IOException and need to import org.apache.poi.hwpf.HWPFDocument;
        HWPFDocument doc=new HWPFDocument(finStream);

        // import  org.apache.poi.hwpf.extractor.WordExtractor
        WordExtractor wordExtract=new WordExtractor(doc);

        // dataArray stores the each line from the document
        String [] dataArray =wordExtract.getParagraphText();


        for(int i=0;i<dataArray.length;i++) {
            // printing lines from the array
            System.out.println("\n–"+dataArray[i]);

        }
        //closing fileinputstream
        finStream.close();
        */

        // prepare contacts types (hardcoded)
        // todo: create Spring config and move it there
        ContactTypeDto emailContact = new ContactTypeDto(0, "email");
        ContactTypeDto phoneContact = new ContactTypeDto(0, "phone");
        ContactTypeDto addressContact = new ContactTypeDto(0, "address");

        // input stream for word document
        FileInputStream ins = new FileInputStream("phonebook.doc");
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
        while(itr.hasNext()) { // iterate over found tables
            System.out.println(String.format("\n%s\ntable #%s\n%s", DELIMETER, tablesCounter, DELIMETER));

            Table table = itr.next();
            for(int rowIndex = 0; rowIndex < table.numRows(); rowIndex++) { // process a whole row
                TableRow row = table.getRow(rowIndex);
                columnsCount = row.numCells();
                System.out.println(String.format("row #%s (cells #%s)", rowIndex, columnsCount));

                if (columnsCount == 1) { // procesing header
                    // todo: for join array use StringUtils.join()!
                    System.out.print("HEADER CELL => ");
                    System.out.println(Arrays.toString(CensusTmpApp.getCellContent(row.getCell(0))));
                    //TableCell headerCell = row.getCell(0);
                } else if (columnsCount == 3) { // regular row with 3 columns (position-name-phone)
                    // (column #0) read position
                    tmpStrArray = CensusTmpApp.getCellContent(row.getCell(0));
                    position = new PositionDto(0, StringUtils.join(tmpStrArray, " "));
                    // (column #2) read full name
                    tmpStrArray = CensusTmpApp.getCellContent(row.getCell(1));
                    employee = new EmployeeDto(0, StringUtils.join(tmpStrArray, " "));
                    employee.setOnlyPosition(position);
                    // (column #3) read contact info (phone/email/address)
                    tmpStrArray = CensusTmpApp.getCellContent(row.getCell(2));
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

        /*
        System.out.println("paragraphs -> " + range.numParagraphs());

        for (int i = 0; i < range.numParagraphs(); i++) {
            Paragraph par = range.getParagraph(i);

            System.out.println("paragraph "+(i+1));
            System.out.println("is in table: "+par.isInTable());
            System.out.println("is table row end: "+par.isTableRowEnd());
            System.out.println(par.text());
            System.out.println("table level -> " + par.getTableLevel());

            if (!par.isInTable()) {
                System.out.println("text:" + par.text());
            } else {
                Table table = range.getTable(par);
                for (int rowIdx = 0; rowIdx < table.numRows(); rowIdx++) {
                    TableRow row = table.getRow(rowIdx);
                    for (int colIdx = 0; colIdx < row.numCells(); colIdx++) {
                        TableCell cell = row.getCell(colIdx);
                        //System.out.println("----> " + cell.numParagraphs());
                        System.out.print(" column=" + cell.getParagraph(0).text());
                        i++;
                    }
                    System.out.println();
                    i++;
                }
            }
        }
        */

    }

    /***/
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

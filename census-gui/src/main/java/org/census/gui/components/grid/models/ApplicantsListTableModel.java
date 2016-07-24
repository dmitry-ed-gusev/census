package org.census.gui.components.grid.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.hhpilot.domain.ApplicantDto;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model for JTable component in PilotApplicantsListFrame.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 12.11.12)
 */

public class ApplicantsListTableModel extends AbstractTableModel {

    // module logger
    private Log log = LogFactory.getLog(ApplicantsListTableModel.class);
    // column names for JTable component
    private String[]           columnNames    = {"Полное имя (ФИО)", "Телефон", "E-mail", "Дата ввода", "Комментарий"};
    // content for Jtable component
    private List<ApplicantDto> applicantsList = new ArrayList<>();
    //private boolean[][] editable_cells; // 2d array to represent rows and columns (editable status)

    public ApplicantsListTableModel(List<ApplicantDto> applicantsList) {
        if (applicantsList != null && !applicantsList.isEmpty()) {
            this.applicantsList = applicantsList;
        } else {
            log.warn("ApplicantsListTableModel constructor() - initializing with empty array!");
        }

    }

    @Override
    public int getRowCount() {
        return (applicantsList == null || applicantsList.isEmpty() ? 0 : applicantsList.size());
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /***/
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        log.debug("ApplicantsListTableModel.getValueAt() working. Row [" + rowIndex + "], column [" + columnIndex + "].");
        Object result = null;
        if (applicantsList != null && !applicantsList.isEmpty() && rowIndex < applicantsList.size() && columnIndex < columnNames.length) {
            switch (columnIndex) {
                case 0: result = applicantsList.get(rowIndex).getSurname();   break;
                case 1: result = applicantsList.get(rowIndex).getPhone();     break;
                case 2: result = applicantsList.get(rowIndex).getEmail();     break;
                case 3: result = applicantsList.get(rowIndex).getInputDate(); break;
                case 4: result = applicantsList.get(rowIndex).getComments();  break;
            }
        } else {
            log.error("Invalid cell index [row = " + rowIndex + "; column = " + columnIndex + "]!");
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        System.out.println("set value -> [" + aValue + "]");
        this.applicantsList.get(0).setSurname(String.valueOf(aValue));
        //super.setValueAt(aValue, rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        //return this.editable_cells[row][col];
        return true;
    }

    //@Override
    //public void setCellEditable(int row, int col, boolean value) {
    //    this.editable_cell[row][col] = value; // set cell true/false
    //    this.fireTableCellUpdated(row, int col);
    //}

    /**
     * Method will return name for column with number = [column]. Names are from internal array columnNames.
    */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /*
    public void setApplicantsList(List<ApplicantDto> applicantsList) {
        this.applicantsList = applicantsList;
        this.fireTableDataChanged(); // we changed data in table
    }
    */

    public void addEmptyRow() {
        log.debug("ApplicantsListTableModel.addEmptyRow() working.");
        int rowIndex = this.getRowCount();

        this.applicantsList.add(new ApplicantDto());
        this.fireTableChanged(new TableModelEvent(this, rowIndex, rowIndex, -1, TableModelEvent.INSERT));
    }

}
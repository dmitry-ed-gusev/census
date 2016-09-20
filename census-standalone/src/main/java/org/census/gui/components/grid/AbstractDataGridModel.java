package org.census.gui.components.grid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.AbstractEntity;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base abstract model class for JTable component. Class aggregates common behaviour for all models
 * for all entities classes. Some methods are abstract - they should be overrided in subclasses.
 * Notes:
 * - zero (0) column always is IDENTITY column and isn't editable
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 11.11.2014)
*/

public abstract class AbstractDataGridModel<T extends AbstractEntity> extends AbstractTableModel {

    @SuppressWarnings("ConstantNamingConvention")
    private static final Log log = LogFactory.getLog(AbstractDataGridModel.class);

    private String[]                columnNames;                        // columns for DataGrid
    private AbstractHibernateDao<T> dao;                                // DAO component for interacting with DB
    private List<T>                 objectsList    = new ArrayList<>(); // internal objects list
    private List<T>                 deletedObjects = new ArrayList<>(); // deleted objects list

    /***/
    protected AbstractDataGridModel(String[] columnNames, AbstractHibernateDao<T> dao) {
        this.columnNames = new String[checkNotNull(columnNames, "Columns names list cannot be null!").length];
        System.arraycopy(columnNames, 0, this.columnNames, 0, columnNames.length);
        this.dao = checkNotNull(dao, "DAO component cannot be null!");
    }

    @Override
    public int getRowCount() {
        return this.objectsList.size();
    }

    @Override
    public int getColumnCount() {
        return (this.columnNames == null ? 0 : this.columnNames.length);
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    /***/
    public void addEmptyRow() {
        log.debug("AbstractDataGridModel.addEmptyRow() working.");
        int rowIndex = this.getRowCount();
        this.addEmptyObject();
        this.fireTableChanged(new TableModelEvent(this, rowIndex, rowIndex, -1, TableModelEvent.INSERT));
    }

    @Override
    /** Custom method (by default all cells are editable). */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex > 0); // zero column isn't editable (IDENTITY)
    }

    /**
     * Method loads objects from database and put found objects in a list, that
     * connected with current object list of objects.
    */
    public void load() {
        log.debug("AbstractDataGridModel.load() working.");
        this.objectsList = this.dao.findAll(); // load data from db
        this.deletedObjects.clear();           // clear deleted objects list
        this.fireTableDataChanged();           // fire about changes in a model
    }

    /***/
    public void save() {
        log.debug("AbstractDataGridModel.save() working.");
        // iterate over objects list and save/update
        for (T object : this.objectsList) {
            this.dao.saveOrUpdate(object);
        }
        // iterate over deleted objects list and delete them from db
        for (T object : this.deletedObjects) {
            this.dao.delete(object);
        }
        this.load(); // load objects list from db (after save) - reload
    }

    /**
     * Method removes row number rowNumber (by removing underlying object from objects list).
     * @param rowNumber int row number for deletion
     */
    public void deleteRow(int rowNumber) {
        log.debug("AbstractDataGridModel.removeRow() working.");
        if (rowNumber >= 0 && rowNumber < this.getRowCount()) { // rowNumber is OK -> processing
            T deletedObject = this.getObjectByIndex(rowNumber);  // get object for deletion
            log.debug(String.format("Object for deletion -> [%s].", deletedObject));
            this.deletedObjects.add(deletedObject); // add deleted object to deleted objects list
            this.objectsList.remove(rowNumber);     // remove object from list of objects
            this.fireTableDataChanged();            // fire table data changed event
        } else { // invalid row number
            log.warn("Can't delete row with index = " + rowNumber);
        }
    }

    /***/
    public T getObjectByIndex(int index) {
        return this.objectsList.get(index);
    }

    /***/
    public void addObject(T object) {
        if (object != null) {
            this.objectsList.add(object);
        } else {
            log.error("Trying to add null object to DataGrid!");
        }
    }

    /**
     * Add empty object to objects list. Empty object is needed for a new empty line in a grid.
     * All child classes should implement this method.
    */
    protected abstract void addEmptyObject();

}
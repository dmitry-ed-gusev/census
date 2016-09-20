package org.census.gui.components.grid.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dao.hibernate.inventory.BuildingDao;
import org.census.commons.dto.inventory.BuildingDto;
import org.census.gui.components.grid.AbstractDataGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JTable GUI component model for BuildingDto object.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 12.11.12)
*/

@Component
public class BuildingsModel extends AbstractDataGridModel<BuildingDto> {

    private Log log = LogFactory.getLog(BuildingsModel.class);

    @Autowired
    public BuildingsModel(BuildingDao dao) {
        super(new String[] {"ID", "Здание", "Комментарий"}, dao);
    }

    /**
     * Method returns value for concrete cell by row and column indexes.
     * @param rowIndex int row index for getting data value
     * @param columnIndex int column index for getting data value
     * @return Object data value for concrete table cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //log.debug("BuildingDtoTableModel.getValueAt() working. Row [" + rowIndex + "], column [" + columnIndex + "].");
        Object result = null;
        if (rowIndex < this.getRowCount() && columnIndex < this.getColumnCount()) { // check row and column indexes
            BuildingDto building = this.getObjectByIndex(rowIndex);
            switch (columnIndex) {
                case 0: result = building.getId();           break; // ID
                case 1: result = building.getBuildingName(); break; // building name
                case 2: result = building.getComment();      break; // comment
                default: log.error(String.format("Invalid column index [%s]!", columnIndex));
            }
        } else { // row or column index is invalid
            log.error(String.format("Invalid cell index [row = %s; column = %s]!", rowIndex, columnIndex));
        }
        return result;
    }

    /**
     *
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //log.debug(String.format("BuildingDtoTableModel.setValueAt() working. Row [%s], column [%s], value [%s].",
        //        rowIndex, columnIndex, aValue));

        if (rowIndex < this.getRowCount() && columnIndex < this.getColumnCount()) { // check row and column indexes
            BuildingDto building = this.getObjectByIndex(rowIndex);
            switch (columnIndex) {
                case 1: building.setBuildingName(String.valueOf(aValue)); break; // building name
                case 2: /*building.setComment(String.valueOf(aValue));*/      break; // comment
                default: log.error(String.format("Invalid column index [%s]!", columnIndex));
            }
        } else { // row or column index is invalid
            log.error(String.format("Invalid cell index [row = %s; column = %s]!", rowIndex, columnIndex));
        }
    }

    @Override
    protected void addEmptyObject() {
        this.addObject(new BuildingDto());
    }

}
package org.census.gui.components.grid.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dao.hibernate.personnel.PositionsSimpleDao;
import org.census.commons.dto.personnel.PositionDto;
import org.census.gui.components.grid.AbstractDataGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JTable GUI component model for PositionDto entity object.
 * @author dgusev
 * @version 1.0 (24.09.2016)
 */

@Component
public class PositionsModel extends AbstractDataGridModel <PositionDto> {

    private static final Log LOG = LogFactory.getLog(PositionsModel.class);

    @Autowired
    public PositionsModel(PositionsSimpleDao dao) {
        super(new String[] {"ID", "Позиция", "Вес"}, dao);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        if (rowIndex < this.getRowCount() && columnIndex < this.getColumnCount()) { // check row and column indexes
            PositionDto position = this.getObjectByIndex(rowIndex);
            switch (columnIndex) {
                case 0: result = position.getId();     break; // ID
                case 1: result = position.getName();   break; // position name
                case 2: result = position.getWeight(); break; // position weight
                default: LOG.error(String.format("Invalid column index [%s]!", columnIndex));
            }
        } else { // row or column index is invalid
            LOG.error(String.format("Invalid cell index [row = %s; column = %s]!", rowIndex, columnIndex));
        }
        return result;
    }

    @Override
    protected void addEmptyObject() {
        this.addObject(new PositionDto());
    }

}

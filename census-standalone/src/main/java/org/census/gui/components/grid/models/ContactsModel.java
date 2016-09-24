package org.census.gui.components.grid.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dao.hibernate.personnel.ContactsSimpleDao;
import org.census.commons.dto.personnel.ContactDto;
import org.census.gui.components.grid.AbstractDataGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JTable GUI component model for ConatctDto entity object.
 * @author dgusev
 * @version 1.0
 */

@Component
public class ContactsModel extends AbstractDataGridModel <ContactDto> {

    private static final Log LOG = LogFactory.getLog(ContactsModel.class);

    @Autowired

    public ContactsModel(ContactsSimpleDao dao) {
        super(new String[] {"ID", "Контакт", "Тип"}, dao);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        if (rowIndex < this.getRowCount() && columnIndex < this.getColumnCount()) { // check row and column indexes
            ContactDto contact = this.getObjectByIndex(rowIndex);
            switch (columnIndex) {
                case 0: result = contact.getId();          break; // ID
                case 1: result = contact.getContact();     break; // contact value
                case 2: result = contact.getContactType(); break; // contact type
                default: LOG.error(String.format("Invalid column index [%s]!", columnIndex));
            }
        } else { // row or column index is invalid
            LOG.error(String.format("Invalid cell index [row = %s; column = %s]!", rowIndex, columnIndex));
        }
        return result;
    }

    @Override
    protected void addEmptyObject() {
        this.addObject(new ContactDto());
    }
}

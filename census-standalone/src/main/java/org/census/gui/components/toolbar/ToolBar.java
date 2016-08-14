package org.census.gui.components.toolbar;

import org.apache.commons.logging.LogFactory;
import org.census.gui.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Application toolbar, inherited from JToolBar object.
 * @author Gusev Dmitry (Чебурашка)
 * @version 1.0 (DATE: 21.12.2014)
*/

@Component
public class ToolBar extends JToolBar {

    @Autowired
    public ToolBar(ActionListener listener) {
        LogFactory.getLog(ToolBar.class).debug("ToolBar constructor() working.");

        ActionListener aListener = checkNotNull(listener, "Action listener for toolbar buttons is null!");

        this.setFloatable(false);
        // SAVE data button
        ToolbarButton saveButton = new ToolbarButton(Actions.SAVE, "save24x24.png");
        saveButton.addActionListener(aListener);
        this.add(saveButton);
        // ADD row button
        ToolbarButton addButton = new ToolbarButton(Actions.ADD, "add24x24.png");
        addButton.addActionListener(aListener);
        this.add(addButton);
        // REMOVE row button
        ToolbarButton removeButton = new ToolbarButton(Actions.REMOVE, "delete24x24.png");
        removeButton.addActionListener(aListener);
        this.add(removeButton);
        // REFRESH data button
        ToolbarButton refreshButton = new ToolbarButton(Actions.REFRESH, "refresh24x24.png");
        refreshButton.addActionListener(aListener);
        this.add(refreshButton);
        // INFO button
        ToolbarButton infoButton = new ToolbarButton(Actions.INFO, "info24x24.png");
        infoButton.addActionListener(aListener);
        this.add(infoButton);
    }

}
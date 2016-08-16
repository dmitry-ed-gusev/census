package org.census.gui.frames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.gui.components.grid.DataGrid;
import org.census.gui.components.panels.ToolbarPanel;
import org.census.gui.components.panels.TreePanel;
import org.census.gui.events.MainFrameEventsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

import static org.census.gui.GuiDefaults.APP_TITLE;
import static org.census.gui.GuiDefaults.MAIN_FRAME_DIMENSION;
import static com.google.common.base.Preconditions.*;

/**
 * Main application GUI frame. Frame structure:
 *  ____________________________________
 * |                                    |
 * |   top panel (menu+toolbar+search)  |
 * |____________________________________|
 * | left (west)  |      main (info)    |
 * | panel with   |      panel with     |
 * | objects tree |      data grid      |
 * |______________|_____________________|
 *
 * @author Gusev Dmitry (gusevd)
 * @version 2.0 (DATE: 21.11.2014)
*/

@Component
public class MainFrame extends JFrame {

    private final static Log log = LogFactory.getLog(MainFrame.class);

    private TreePanel treePanel; // reference to tree panel for events processing
    private JPanel    mainPanel;

    /***/
    @Autowired
    public MainFrame(MainFrameEventsProcessor eventsProcessor, ToolbarPanel toolbarPanel, TreePanel treePanel) {
        log.debug("MainFrame.createFrame() working.");

        this.setTitle(APP_TITLE);                      // creating main app gui frame
        this.treePanel = treePanel;                    // save link to tree panel (access to tree properties)
        this.mainPanel = new JPanel(new BorderLayout());
        this.add(toolbarPanel, BorderLayout.NORTH);    // toolbar panel at the top of main frame (north)
        this.add(treePanel, BorderLayout.WEST);        // jtree panel at the left of main frame left (west)
        this.add(this.mainPanel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // close application on exit
        this.setSize(MAIN_FRAME_DIMENSION);                           // default size for main frame
        this.setMinimumSize(MAIN_FRAME_DIMENSION);                    // min size for main frame
        this.pack();                                                  // pack GUI elements
        this.setLocationRelativeTo(null);                             // put frame to screen center

        eventsProcessor.setMainFrame(this); // put (inject) reference to MainFrame into events processor class
        this.treePanel.setAutoSelect();     // set selection on default tree node
    }

    /** Method returns last selected element (leaf, node) from JTree component. */
    public DefaultMutableTreeNode getLastSelectedPathComponent() {
        log.debug("MainFrame.getLastSelectedPathComponent() working.");
        return this.treePanel.getLastSelectedPathComponent();
    }

    /***/
    public void setMainDataGrid(DataGrid dataGrid) {
        log.debug("MainFrame.setMainDataGrid() working.");
        checkNotNull(dataGrid, "DataGrid component can't be null!").load();
        this.mainPanel.removeAll();
        this.mainPanel.add(dataGrid);
        this.mainPanel.validate();
        this.mainPanel.repaint();
    }

}
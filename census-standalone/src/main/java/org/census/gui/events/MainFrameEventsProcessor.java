package org.census.gui.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.gui.Actions;
import org.census.gui.components.grid.DataGrid;
import org.census.gui.components.jtree.TreeNode;
import org.census.gui.frames.MainFrame;
import org.springframework.stereotype.Component;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Events processor component for most of GUI elements.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 14.11.12)
*/

@Component
public class MainFrameEventsProcessor implements ActionListener, TreeSelectionListener {

    private static final Log log = LogFactory.getLog(MainFrameEventsProcessor.class);

    private MainFrame mainFrame; // reference to MainFrame component
    private DataGrid  dataGrid;  // current DataGrid component

    /** Default constructor. */
    public MainFrameEventsProcessor() {
        log.debug("MainFrameEventsProcessor constructor() working.");
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) { // processing events for some of GUI controls
        log.debug("MainFrameEventsProcessor.actionPerformed() working. Action [" + e.getActionCommand() + "].");
        if (this.dataGrid != null) {
            // actions handlers
            if (Actions.INFO.getActionName().equals(e.getActionCommand())) {
                System.out.println("---> INFO");
            } else if (Actions.ADD.getActionName().equals(e.getActionCommand())) {
                this.dataGrid.addEmptyRow();
            } else if (Actions.REMOVE.getActionName().equals(e.getActionCommand())) {
                this.dataGrid.removeSelectedRow();
            } else if (Actions.REFRESH.getActionName().equals(e.getActionCommand())) {
                this.dataGrid.load();
            } else if (Actions.SAVE.getActionName().equals(e.getActionCommand())) {
                this.dataGrid.save();
            } else {
                log.warn(String.format("Unknown action [%s]!", e.getActionCommand()));
            }
        } else { // current DataGrid component is empty
            log.warn("DataGrid component is null!");
        }
    }

    @Override
    /** Processing jtree component node selection. */
    public void valueChanged(TreeSelectionEvent e) { // processing JTree events
        log.debug("MainFrameEventsProcessor.valueChanged() working.");
        DefaultMutableTreeNode node = this.getMainFrame().getLastSelectedPathComponent(); // get selection
        if (node == null) { // selection is empty -> exit
            return;
        }

        // processing jtree node selection
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            if (nodeInfo instanceof TreeNode) { // tree node selection
                log.debug(String.format("JTree node click [%s].", nodeInfo));
                this.dataGrid = ((TreeNode) nodeInfo).getDataGrid();
                this.mainFrame.setMainDataGrid(this.dataGrid);
            } else { // tree selection (not tree node)
                log.debug(String.format("JTree leaf click [%s].", nodeInfo));
            }
        } else {
            log.debug(String.format("JTree click (not a leaf) [%s].", node));
        }
    }

}
package org.census.gui.components.jtree;

import org.census.gui.components.grid.DataGrid;

import static com.google.common.base.Preconditions.*;

/**
 * Custom JTree node element.
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 27.12.2014)
 */

public class TreeNode {

    private String   nodeName; // name of current tree node
    private DataGrid dataGrid; // DataGrid component for current tree node

    public TreeNode() {
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = checkNotNull(nodeName, "Node name can't be null!");
    }

    public DataGrid getDataGrid() {
        return dataGrid;
    }

    public void setDataGrid(DataGrid dataGrid) {
        this.dataGrid = (dataGrid != null ? dataGrid : new DataGrid());
    }

    @Override
    public String toString() { // method used for showing node name in a jtree
        return this.nodeName;
    }

}
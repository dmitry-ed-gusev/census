package org.census.gui.components.jtree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import static com.google.common.base.Preconditions.*;

/**
 * JTree visual component with some additions.
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 27.12.2014)
*/

public class Tree extends JTree {

    private DefaultMutableTreeNode selectedNode = null; // internal state - auto selected node

    public Tree(TreeModel root) {
        super(root);
    }

    public DefaultMutableTreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(DefaultMutableTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    /***/
    public void setAutoSelect() {
        this.setSelectionPath(new TreePath(checkNotNull(this.selectedNode, "Auto selected node can't be null!").getPath())); // auto selecting one of nodes
    }

}
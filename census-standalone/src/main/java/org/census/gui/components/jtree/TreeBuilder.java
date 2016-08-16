package org.census.gui.components.jtree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.census.gui.GuiDefaults.JTREE_ROOT_NODE_NAME;

/**
 * Builder class for JTree object.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 24.12.2014)
*/

@Component
public class TreeBuilder {

    @Resource(name = "tree") private Map<String, List<TreeNode>> treeObjects; // JTree config (see XML)
    @Autowired               private TreeSelectionListener               listener;

    public Map<String, List<TreeNode>> getTreeObjects() {
        return treeObjects;
    }

    public void setTreeObjects(Map<String, List<TreeNode>> treeObjects) {
        this.treeObjects = treeObjects;
    }

    public TreeSelectionListener getListener() {
        return listener;
    }

    public void setListener(TreeSelectionListener listener) {
        this.listener = listener;
    }

    /***/
    public Tree getTree() {
        // tree's root, accept children = true (default)
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(JTREE_ROOT_NODE_NAME);

        DefaultMutableTreeNode selectedNode = null;

        for (String objectsType : checkNotNull(treeObjects, "Tree objects list can't be null!").keySet()) {
            // subtype (level 1) for tree's root - group of objects, accept children = true (default)
            DefaultMutableTreeNode type = new DefaultMutableTreeNode(objectsType); // <- level 1
            root.add(type); // add type (group) to root
            // objects list for subtype of tree's root
            List<TreeNode>  objects = treeObjects.get(objectsType); // <- level 2
            if (objects != null && !objects.isEmpty()) {
                for (TreeNode object : objects) {
                    // subtype (level 2) - one object in a group, accept children = false
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(object, false);
                    type.add(node); // add object to group
                    if (selectedNode == null) {
                        selectedNode = node;
                    }
                }
            }
        } // end of FOR cycle (generating tree by config)

        // JTree object. We can directly create it with DefaultMutableTreeNode object (root), but in this case
        // tree will not ask for accept children property (if group - level 1 - doesn't have children it will
        // drawn as a leaf. But if we create JTree from DefaultTreeModel (see code) JTree will ask for allow
        // children property and draw empty groups - level 1 - as a node (empty folder)
        final Tree tree = new Tree(new DefaultTreeModel(root, true));
        tree.setSelectedNode(selectedNode); // set node for auto selection for tree
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // single selection mode
        tree.addTreeSelectionListener(checkNotNull(listener, "TreeSelectionListener can't be null!")); // listener
        TreeBuilder.expandAllNodes(tree, 0, tree.getRowCount()); // expand all nodes
        tree.putClientProperty("JTree.lineStyle", "Angled");      // style -> "Angled" (default), "Horizontal", "None"

        return tree; // return builded component
    }

    /** Utility method for expanding all nodes in a given tree. */
    private static void expandAllNodes(JTree tree, int startingIndex, int rowCount){
        for(int i=startingIndex;i<rowCount;++i){
            tree.expandRow(i);
        }

        if(tree.getRowCount()!=rowCount){
            TreeBuilder.expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

}
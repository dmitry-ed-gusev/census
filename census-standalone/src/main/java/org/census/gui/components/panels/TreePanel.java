package org.census.gui.components.panels;

import org.apache.commons.logging.LogFactory;
import org.census.gui.components.jtree.Tree;
import org.census.gui.components.jtree.TreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.census.gui.AppDefaults.*;

/**
 * Panel with objects tree. Place - left of the main frame.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 22.12.2014)
*/

@Component
public class TreePanel extends JPanel {

    private Tree tree;

    @Autowired
    public TreePanel(TreeBuilder treeBuilder) {
        super(new GridLayout(1, 1));
        LogFactory.getLog(TreePanel.class).debug("TreePanel constructor() working.");

        // generate tree and save reference to it
        this.tree = checkNotNull(treeBuilder, "TreeBuilder can't be null!").getTree();
        // scroll pane - scrolling component for current builded jtree
        JScrollPane treePane = new JScrollPane(this.tree);
        treePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        treePane.setPreferredSize(TREE_COMPONENT_DIMENSION);
        this.add(treePane);
    }

    /***/
    public DefaultMutableTreeNode getLastSelectedPathComponent() {
        return (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
    }

    /***/
    public void setAutoSelect() {
        this.tree.setAutoSelect();
    }

}
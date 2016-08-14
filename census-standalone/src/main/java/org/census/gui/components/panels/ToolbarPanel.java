package org.census.gui.components.panels;

import org.apache.commons.logging.LogFactory;
import org.census.gui.components.toolbar.ToolBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.census.gui.AppDefaults.*;

/**
 * Panel with toolbar and quick search text field. Place - top of main frame (NORTH).
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 22.12.2014)
*/

@Component
public class ToolbarPanel extends JPanel {

    @Autowired
    public ToolbarPanel(ActionListener listener, ToolBar toolBar) {
        LogFactory.getLog(ToolbarPanel.class).debug("ToolbarPanel constructor() working.");

        // GridBagLayout creation
        GridBagLayout      gridBagLayout      = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        this.setLayout(gridBagLayout);

        // Constraints for JToolBar. This component should stick to top-left corner of the panel and resize only horizontal
        gridBagConstraints.gridx     = gridBagConstraints.gridy   = 0; // position (left-top corner)
        gridBagConstraints.weightx   = gridBagConstraints.weighty = 0; // component doesn't have any weight (both x and y axis)
        gridBagConstraints.gridwidth = 2;                              // width in cells (two components below: label, text field)
        gridBagConstraints.anchor    = GridBagConstraints.NORTHWEST;   // anchor - left-top position
        gridBagConstraints.fill      = GridBagConstraints.HORIZONTAL;  // resize - only horizontal
        gridBagConstraints.insets    = new Insets(2, 5, 2, 5);         // insets for components
        gridBagLayout.setConstraints(toolBar, gridBagConstraints);
        this.add(toolBar);

        // Quick search label and text field. Placed under toolbar in the top of main window.
        JLabel quickSearchLabel = new JLabel(QUICK_SEARCH_LABEL_TEXT); // quick search label
        gridBagConstraints.weightx = 0;   // label shouldn't resize/move in x axis (no weight)
        gridBagConstraints.gridy = 1;     // position in y axis - under toolbar
        gridBagConstraints.gridwidth = 1; // width in cells = 1 -> there are two components - label and text field
        gridBagLayout.setConstraints(quickSearchLabel, gridBagConstraints);
        this.add(quickSearchLabel);

        JTextField quickSearchField = new JTextField(); // quick search text field
        gridBagConstraints.gridx = 1;   // position on x axis - right from quick search label
        // weight on x axis - this component should get all remain space (after label) and
        // if window will be resized horizontally - gets all added h-space.
        gridBagConstraints.weightx = 1;
        gridBagLayout.setConstraints(quickSearchField, gridBagConstraints);
        this.add(quickSearchField);
        quickSearchField.addActionListener(listener); // listener for quick search text field events
    }

}
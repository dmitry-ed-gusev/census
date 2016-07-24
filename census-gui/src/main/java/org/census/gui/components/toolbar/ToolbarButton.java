package org.census.gui.components.toolbar;

import org.apache.commons.logging.LogFactory;
import org.census.gui.Actions;
import javax.swing.*;

import static com.google.common.base.Preconditions.*;

/**
 * Simple toolbar button, inherited from JButton.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 21.11.2014)
*/

public class ToolBarButton extends JButton {

    /***/
    public ToolBarButton(Actions action, String iconName) {
        LogFactory.getLog(ToolBarButton.class).debug(
                String.format("ToolBarButton constructor() working. Action->[%s], icon->[%s].", action, iconName));
        // set some button parameters
        this.setIcon(new ImageIcon(getClass().getResource("icons/" + checkNotNull(iconName, "Icon name is null!"))));
        this.setFocusable(false);
        this.setActionCommand(checkNotNull(action.getActionName(), "Action for toolbar button is null!"));
    }

}
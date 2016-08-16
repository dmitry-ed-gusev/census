package org.census.gui;

import java.awt.*;

/**
 * Constants for Pilot application GUI module.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 12.11.12)
 */

public interface GuiDefaults {

    // default application title
    String APP_TITLE                   = "Census ERP system, 2012-2016.";
    // default width and height for main app window
    Dimension MAIN_FRAME_DIMENSION     = new Dimension(700, 450);
    // default width and height for jtree component
    Dimension TREE_COMPONENT_DIMENSION = new Dimension(160, 0);
    // jtree root node name
    String JTREE_ROOT_NODE_NAME        = "Управляемые объекты";
    // quick search label text
    String QUICK_SEARCH_LABEL_TEXT     = "Быстрый поиск: ";
    // text for 'no data' label on main panel
    String NO_DATA_LABEL_TEXT          = "Нет данных для отображения!";
    // default look and feel (windows classic)
    String DEFAULT_LOOK_AND_FEEL       = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    // spring bean name for main gui frame
    String MAIN_FRAME_BEAN             = "mainFrame";

}
package org.census.gui;

import java.awt.*;

/**
 * Constants for Pilot application GUI module.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 12.11.12)
 */

public interface GuiConstants {

    // default application title
    public static final String APP_TITLE                   = "Census ERP system, 2012-2015.";
    // default width and height for main app window
    public static final Dimension MAIN_FRAME_DIMENSION     = new Dimension(700, 450);
    // default width and height for jtree component
    public static final Dimension TREE_COMPONENT_DIMENSION = new Dimension(160, 0);
    // jtree root node name
    public static final String JTREE_ROOT_NODE_NAME        = "Управляемые объекты";
    // quick search label text
    public static final String QUICK_SEARCH_LABEL_TEXT     = "Быстрый поиск: ";
    // text for 'no data' label on main panel
    public static final String NO_DATA_LABEL_TEXT          = "Нет данных для отображения!";
}
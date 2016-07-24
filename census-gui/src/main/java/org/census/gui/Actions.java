package org.census.gui;

/**
 * Actions for application GUI.
 * @author Gusev Dmitry (Чебурашка)
 * @version 1.0 (DATE: 21.12.2014)
*/

public enum Actions {

    SAVE("actionSave"), INFO("actionInfo"), ADD("actionAdd"), REMOVE("actionRemove"), REFRESH("actionRefresh");

    private Actions(String actionName) {
        this.actionName = actionName;
    }

    private String actionName;

    public String getActionName() {
        return actionName;
    }

    /***/
    public static Actions getActionByName(String actionName) {

        for (Actions action : Actions.values()) {// search for matching action
            if (action.getActionName().equals(actionName)) {
                return action;
            }
        }

        return null; // no matching action found
    }

}
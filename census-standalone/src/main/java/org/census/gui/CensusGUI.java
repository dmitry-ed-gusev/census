package org.census.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.gui.frames.MainFrame;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.util.Locale;

/**
 * Census ERP system application main GUI (swing) module.
 * @author Gusev Dmitry (gusevd)
 * @version 2.0 (DATE: 09.11.2014)
*/

public class CensusGUI {

    private static final String SPRING_CONFIG_NAME = "CensusGUIContext.xml";

    /***/
    public static void main(String[] args)  {
        Log log = LogFactory.getLog(CensusGUI.class);
        log.info("Starting Census ERP system GUI application.");

        Locale.setDefault(Locale.US); // without this setting Oracle XE refuses connections (we have to set locale value).

        try {
            // set windows classical look and feel for whole application
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            // initialize and load Spring application context
            AbstractApplicationContext context = new ClassPathXmlApplicationContext(new String[] {SPRING_CONFIG_NAME}, false);
            context.refresh();

            //BuildingDao buildingDao = (BuildingDao) context.getBean("buildingDao");
            //System.out.println("-> " + buildingDao.findAll());

            final MainFrame mainFrame = (MainFrame) context.getBean("mainFrame");
            // application builder
            //final PilotBuilder builder = new PilotBuilder();
            // create and show GUI
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainFrame.setVisible(true);
                }
            });
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException |
                InstantiationException | IllegalAccessException e) {
            log.error(e);
        }
    }

}

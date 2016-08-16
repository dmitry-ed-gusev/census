package org.census.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.gui.frames.MainFrame;
import org.springframework.context.support.AbstractApplicationContext;

import javax.swing.*;

import static org.census.gui.GuiDefaults.DEFAULT_LOOK_AND_FEEL;
import static org.census.gui.GuiDefaults.MAIN_FRAME_BEAN;

/**
 * Starter/builder for GUI interface of Census system.
 * Just simple utility class.
 * Created by vinnypuhh on 16.08.2016.
 */

public final class CensusGuiStarter {

    @SuppressWarnings("ConstantNamingConvention")
    private static final Log LOG = LogFactory.getLog(CensusGuiStarter.class);

    private CensusGuiStarter() {}

    /***/
    public static void startGui(AbstractApplicationContext context)
            throws ClassNotFoundException, UnsupportedLookAndFeelException,
            InstantiationException, IllegalAccessException {

        LOG.info("Starting Census System GUI application.");

        if (context == null) { // fail fast
            throw new IllegalArgumentException("Application context is NULL!");
        }

        UIManager.setLookAndFeel(DEFAULT_LOOK_AND_FEEL);                    // set look and feel
        MainFrame mainFrame = (MainFrame) context.getBean(MAIN_FRAME_BEAN); // get bean
        SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));       // start gui

    }

}

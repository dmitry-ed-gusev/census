package org.census.gui.frames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Gusev Dmitry (GusevD)
 * @version 1.0 (DATE: 18.10.12)
 * @deprecated
*/

public class PilotOneApplicantFrame {

    private Log log = LogFactory.getLog(PilotOneApplicantFrame.class);

    // main frame
    private JFrame mainFrame;

    public PilotOneApplicantFrame() {
        log.debug("PilotMainWindow() constructor working.");
        // main frame
        mainFrame = new JFrame("HHPilot, 2012");

        // panel with one person view
        JPanel panel = new JPanel();
        GridBagLayout gb = new GridBagLayout(); // layout manager
        panel.setLayout(gb);
        GridBagConstraints gbc = new GridBagConstraints(); // constraints
        gbc.ipadx = 3;
        gbc.ipady = 3;
        gbc.insets = new Insets(2, 2, 2, 2);

        // candidate name label
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel fioLabel = new JLabel("ФИО кандидата");
        gb.setConstraints(fioLabel, gbc);
        panel.add(fioLabel);
        // candidate name text field
        gbc.gridx = 1;
        JTextField fioText = new JTextField(20);
        gb.setConstraints(fioText, gbc);
        panel.add(fioText);

        // candidate name field
        gbc.gridx = 2;
        JLabel idLabel = new JLabel("(ID)");
        gb.setConstraints(idLabel, gbc);
        panel.add(idLabel);

        // input date label
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel inputDateLabel = new JLabel("Дата ввода");
        gb.setConstraints(inputDateLabel, gbc);
        panel.add(inputDateLabel);
        // input date text field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;

        JTextField inputDateText = new JTextField(10);
        gb.setConstraints(inputDateText, gbc);
        panel.add(inputDateText);

        // interest label
        gbc.gridx = 2;
        JLabel interestLabel = new JLabel("интерес");
        gb.setConstraints(interestLabel, gbc);
        panel.add(interestLabel);
        // interest text field
        gbc.gridx = 3;
        JTextField interestField = new JTextField(15);
        gb.setConstraints(interestField, gbc);
        panel.add(interestField);

        // other setting for main frame
        mainFrame.setContentPane(panel);
        mainFrame.pack(); // auto set size
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.info("Closing application.");
                System.exit(0);
            }
        });
    } // end of constructor

    public void show() {
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Log log = LogFactory.getLog(PilotOneApplicantFrame.class);
        log.info("Main window initializing.");
        PilotOneApplicantFrame mainWin = new PilotOneApplicantFrame();
        mainWin.show();
    }
}

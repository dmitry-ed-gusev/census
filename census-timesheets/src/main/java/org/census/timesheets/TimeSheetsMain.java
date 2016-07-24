package org.census.timesheets;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.service.TimesheetsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.census.commons.CensusDefaults.*;

/**
 * @author Gusev Dmitry (gde)
 * @version 1.0 (DATE: 07.07.2015)
 */
public class TimeSheetsMain {

    /***/
    public static void main(String[] args) {
        Log log = LogFactory.getLog(TimeSheetsMain.class);
        log.info("Timesheets starting...");

        // ask for a dates interval and storage path
        JTextField datesRangeField  = new JTextField(); // dates range text field
        JTextField storagePathField = new JTextField(); // storage path text field
        // labels for text fields
        Object[] dialogMessage = {
                "Диапазон дат (дд.мм.гггг-дд.мм.гггг):", datesRangeField,
                "Каталог для генерации отчетов:", storagePathField
        };
        // show dialog window (ask for dates range and storage path)
        int option = JOptionPane.showConfirmDialog(null, dialogMessage, "Параметры генерации отчетов", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) { // selected OK button

            String dates   = "03.08.2015-07.08.2015"; // datesRangeField.getText();
            String storage = "c:/temp/" + System.currentTimeMillis(); // storagePathField.getText();

            if (!StringUtils.isBlank(dates) && !StringUtils.isBlank(storage)) {
                log.debug(String.format("Input parameters are not empty: dates [%s], storage [%s].", dates, storage));

                // parse dates
                String[] datesRange = dates.split("-");
                if (datesRange.length == 2) {
                    SimpleDateFormat sdf = new SimpleDateFormat(CS_DATETIME_FORMAT);
                    try {
                        Date start = sdf.parse(datesRange[0] + " 00:00:00");
                        Date end = sdf.parse(datesRange[1] + " 23:59:59");

                        // load spring application context
                        ApplicationContext context = new ClassPathXmlApplicationContext("TimesheetsContext.xml");
                        // get timesheets engine bean
                        TimesheetsService engine = (TimesheetsService) context.getBean("timesheetsService");
                        // generating reports
                        engine.getReportByDepartments(start, end, storage);

                    } catch (ParseException e) {
                        log.error(String.format("Can't parse dates: [%s]/[%s]!", datesRange[0], datesRange[1]));
                        JOptionPane.showMessageDialog(null, String.format("Неверные параметры [%s]!", dates), "Ошибка!", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Неверные параметры!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Неверные параметры!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            log.info("Report generating cancelled!");
        }

    }

}
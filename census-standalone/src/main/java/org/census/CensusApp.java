package org.census;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.gui.CensusGuiStarter;
import org.census.services.WordProcessingService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.io.IOException;
import java.util.Locale;

/**
 * Census System application main GUI (swing) module.
 * @author Gusev Dmitry (gusevd)
 * @version 2.0 (DATE: 09.11.2014)
*/

public class CensusApp {

    private static final String SPRING_CONFIG_NAME = "CensusAppContext.xml";

    /***/
    public static void main(String[] args)  {
        Log log = LogFactory.getLog(CensusApp.class);
        log.info("Starting Census application.");

        Locale.setDefault(Locale.US); // without this setting Oracle XE refuses connections (we have to set locale value).

        try {
            // initialize and load Spring application context
            AbstractApplicationContext context = new ClassPathXmlApplicationContext(new String[] {SPRING_CONFIG_NAME}, false);
            context.refresh();

            WordProcessingService wordService = (WordProcessingService) context.getBean("wordProcessingService");
            wordService.loadPhoneBook();

            /*
            ContactsTypesSimpleDao contactsTypes = (ContactsTypesSimpleDao) context.getBean("contactsTypesSimpleDao");
            ContactTypeDto emailContactType = new ContactTypeDto(0, "email");
            ContactTypeDto phoneContactType = new ContactTypeDto(0, "phone");
            contactsTypes.saveOrUpdate(emailContactType);
            contactsTypes.saveOrUpdate(phoneContactType);
            phoneContactType.setId(0);
            contactsTypes.saveOrUpdate(phoneContactType);
            System.out.println("===> " + emailContactType.getId());
            System.out.println("===> " + phoneContactType.getId());
            */

            CensusGuiStarter.startGui(context); // start GUI for Census application

        } catch (ClassNotFoundException | UnsupportedLookAndFeelException |
                InstantiationException | IllegalAccessException | IOException e) {
            log.error(e);
        }

    }

}

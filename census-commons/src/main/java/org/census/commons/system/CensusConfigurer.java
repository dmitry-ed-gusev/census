package org.census.commons.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 12.03.14)
*/

@Component
public class CensusConfigurer /*extends PropertyPlaceholderConfigurer*/ {

    private final static Log log = LogFactory.getLog(CensusConfigurer.class);

    //@Override
    @PostConstruct
    public void postProcessBeanFactory(/*ConfigurableListableBeanFactory beanFactory*/) {
        log.debug("CensusConfigurer.postProcessBeanFactory() working.");
        // do our custom configuration before spring gets a chance to
        /*
        try {
            this.configureCensus();
        } catch(Exception e) {
            throw new BeanCreationException("JtracConfigurer failed", e);
        }
        */
        //super.postProcessBeanFactory(beanFactory);
    }


    /***/
    /*
    private void configureCensus() throws Exception {
        log.debug("CensusConfigurer.configureCensus() working.");

        // census home catalog
        String            jtracHome         = null;
        ClassPathResource jtracInitResource = new ClassPathResource(CENSUS_INIT_PROPERTIES);
        // jtrac-init.properties assumed to exist
        Properties props = loadProps(jtracInitResource.getFile());
        logger.info("found 'jtrac-init.properties' on classpath, processing...");
        jtracHome = props.getProperty("jtrac.home");
        if (jtracHome != null) {
            logger.info("'jtrac.home' property initialized from 'jtrac-init.properties' as '" + jtracHome + "'");
        }
        //======================================================================
        FilenameFilter ff = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("messages_") && name.endsWith(".properties");
            }
        };
        File[] messagePropsFiles = jtracInitResource.getFile().getParentFile().listFiles(ff);
        String locales = "en";
        for(File f : messagePropsFiles) {
            int endIndex = f.getName().indexOf('.');
            String localeCode = f.getName().substring(9, endIndex);
            locales += "," + localeCode;
        }
        logger.info("locales available configured are '" + locales + "'");
        props.setProperty("jtrac.locales", locales);
        //======================================================================
        if (jtracHome == null) {
            logger.info("valid 'jtrac.home' property not available in 'jtrac-init.properties', trying system properties.");
                jtracHome = System.getProperty("jtrac.home");
                if (jtracHome != null) {
                    logger.info("'jtrac.home' property initialized from system properties as '" + jtracHome + "'");
                }
            }
            if (jtracHome == null) {
                logger.info("valid 'jtrac.home' property not available in system properties, trying servlet init paramters.");
                jtracHome = servletContext.getInitParameter("jtrac.home");
                if (jtracHome != null) {
                    logger.info("Servlet init parameter 'jtrac.home' exists: '" + jtracHome + "'");
                }
            }
            if (jtracHome == null) {
                jtracHome = System.getProperty("user.home") + "/.jtrac";
                logger.warn("Servlet init paramter  'jtrac.home' does not exist.  Will use 'user.home' directory '" + jtracHome + "'");
            }
            //======================================================================
            File homeFile = new File(jtracHome);
            if (!homeFile.exists()) {
                homeFile.mkdir();
                logger.info("directory does not exist, created '" + homeFile.getPath() + "'");
                if (!homeFile.exists()) {
                    String message = "invalid path '" + homeFile.getAbsolutePath() + "', try creating this directory first.  Aborting.";
                    logger.error(message);
                    throw new Exception(message);
                }
            } else {
                logger.info("directory already exists: '" + homeFile.getPath() + "'");
            }
            props.setProperty("jtrac.home", homeFile.getAbsolutePath());
            //======================================================================
            File attachmentsFile = new File(jtracHome + "/attachments");
            if (!attachmentsFile.exists()) {
                attachmentsFile.mkdir();
                logger.info("directory does not exist, created '" + attachmentsFile.getPath() + "'");
            } else {
                logger.info("directory already exists: '" + attachmentsFile.getPath() + "'");
            }
            File indexesFile = new File(jtracHome + "/indexes");
            if (!indexesFile.exists()) {
                indexesFile.mkdir();
                logger.info("directory does not exist, created '" + indexesFile.getPath() + "'");
            } else {
                logger.info("directory already exists: '" + indexesFile.getPath() + "'");
            }
            //======================================================================
            File propsFile = new File(homeFile.getPath() + "/jtrac.properties");
            if (!propsFile.exists()) {
                propsFile.createNewFile();
                logger.info("properties file does not exist, created '" + propsFile.getPath() + "'");
                OutputStream os = new FileOutputStream(propsFile);
                Writer out = new PrintWriter(os);
                try {
                    out.write("database.driver=org.hsqldb.jdbcDriver\n");
                    out.write("database.url=jdbc:hsqldb:file:${jtrac.home}/db/jtrac\n");
                    out.write("database.username=sa\n");
                    out.write("database.password=\n");
                    out.write("hibernate.dialect=org.hibernate.dialect.HSQLDialect\n");
                    out.write("hibernate.show_sql=false\n");
                } finally {
                    out.close();
                    os.close();
                }
                logger.info("HSQLDB will be used.  Finished creating '" + propsFile.getPath() + "'");
            } else {
                logger.info("'jtrac.properties' file exists: '" + propsFile.getPath() + "'");
            }
            //======================================================================
            String version = "0.0.0";
            String timestamp = "0000";
            ClassPathResource versionResource = new ClassPathResource("jtrac-version.properties");
            if(versionResource.exists()) {
                logger.info("found 'jtrac-version.properties' on classpath, processing...");
                Properties versionProps = loadProps(versionResource.getFile());
                version = versionProps.getProperty("version");
                timestamp = versionProps.getProperty("timestamp");
            } else {
                logger.info("did not find 'jtrac-version.properties' on classpath");
            }
            logger.info("jtrac.version = '" + version + "'");
            logger.info("jtrac.timestamp = '" + timestamp + "'");
            props.setProperty("jtrac.version", version);
            props.setProperty("jtrac.timestamp", timestamp);
            props.setProperty("database.validationQuery", "SELECT 1");
            props.setProperty("ldap.url", "");
            props.setProperty("ldap.activeDirectoryDomain", "");
            props.setProperty("ldap.searchBase", "");
            props.setProperty("database.datasource.jndiname", "");
            // set default properties that can be overridden by user if required
            setProperties(props);
            // finally set the property that spring is expecting, manually
            FileSystemResource fsr = new FileSystemResource(propsFile);
            setLocation(fsr);
        }

        private Properties loadProps(File file) throws Exception {
            InputStream is = null;
            Properties props = new Properties();
            try {
                is = new FileInputStream(file);
                props.load(is);
            } finally {
                is.close();
            }
            return props;
        }
    */

}
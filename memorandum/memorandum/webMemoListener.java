package memorandum;

/**
 * ������ ����� �������� �.�. "���������" - listener - �����, ������� ����������� ��������� ������� -
 * ��������/����������� ��������� (�����/���� ����� ����������), ��������/����������� ������, ���������/��������
 * ���������� ���������.
 * � ������ ������ ������������� ������ ������� ��������/�������� ������, ��� ������������ ���������� ���������� �������.
 */
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public final class webMemoListener implements ServletContextListener, HttpSessionListener {

    // ������ ���������� ������������ � ������ ������.
    private Logger logger = Logger.getLogger(getClass().getName());
    // ������� ������
    private static Integer sessionsCounter = 0;
    // �������� �������� - ����� ����������, ��� ���� ������� ������� (�.�. ��� �������� ����� ����������!).
    private static ServletContext servletContext = null;

    /**
     * ����� ���������� ��� �������� ������ ����� ������. ��� �������� ������ ������ ������������� ������� ��������
     * ������. ����� � ������ ������������ �������� ������-��������� ������ �� ��� DAO-���������� ����������.
     */
    @Override
    public synchronized void sessionCreated(HttpSessionEvent se) {
        sessionsCounter++;
        logger.debug("NEW SESSION [" + se.getSession().getId() + "] CREATED. Sessions counter = " + sessionsCounter);
        // ��������� ������� ������ ���������� - ������� ������
        if (servletContext != null) {
            servletContext.setAttribute(Defaults.ATTRIBUTE_APPLICATION_USERS, Integer.toString(sessionsCounter));
        } else {
            logger.error("Servlet context is NULL!");
        }
        // ������ ������������ � ������ ������ DaoHandler
        try {
            se.getSession().setAttribute(Defaults.ATTRIBUTE_SESSION_DAO, new daoHandler());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * ����� ���������� ����� �������� �������� ��������������� (web-���������� ��������� �� ������� � ����������).
     * ��� ������ ������ �� ������������� ������� ��������� - ����������, ������� ����� ���� ��������� � ���������.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // ���� ���������� ��� �������� ����� �� �������� ����������
        servletContext = sce.getServletContext();
        servletContext.setAttribute(Defaults.ATTRIBUTE_APPLICATION_USERS, Integer.toString(sessionsCounter));       
    }

    /**
     * ����� ���������� ����� �������� �������� ����������������� (���������� ������� � ������� ��� ������
     * ���������� ����������).
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("APPLICATION CONTEXT DESTROYED.");
    }

    /**
     * ����� ���������� ��� ����������� ������. ����������� ����������������� �������� �������� ������.
     * ����� ����������� ������������� ���� �������, ��������������� ������ �������.
     */
    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent se) {
        // ��������� ������� ������
        sessionsCounter--;
        logger.debug("SESSION [" + se.getSession().getId() + "] DESTROYED. Sessions counter = " + sessionsCounter);
        // ��������� ������� ������ ���������� - ������� ������
        if (servletContext != null) {
            servletContext.setAttribute(Defaults.ATTRIBUTE_APPLICATION_USERS, Integer.toString(sessionsCounter));
        } else {
            logger.error("Servlet context is NULL!");
        }
    }

}
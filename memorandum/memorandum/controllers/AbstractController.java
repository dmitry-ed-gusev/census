package memorandum.controllers;

import jpersonnel.dto.SimpleEmployeeDTO;
import memorandum.Defaults;
import memorandum.daoHandler;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class AbstractController extends HttpServlet {

    // ��������� �������, ������������������� ��� �������� ����������
    Logger logger = Logger.getLogger(getClass().getName());

    // ��������� ������� �������� ������� ��� ����������
    Logger acc_logger = Logger.getLogger(Defaults.LOGGER_ACCESS_NAME);

    // ��� ��������
    String ACTION      = null;
    // ������������ ���������� � �������
    SimpleEmployeeDTO simpleEmployee = null;
    // ��������� ������ �� ��� DAO-���������� ����������
    daoHandler dH = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // ��������� ��������� ��� ������ �������(REQUEST)
        req.setCharacterEncoding("Cp1251");
        // ��������� ��������� ��� ������ ������(RESPONSE)
        resp.setContentType(Defaults.FULL_CONTENT_TYPE);

        addSessionAttributes(req);
        getRequestParametrs(req);
        this.processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // ��������� ��������� ��� ������ �������(REQUEST)
        req.setCharacterEncoding("Cp1251");
        // ��������� ��������� ��� ������ ������(RESPONSE)
        resp.setContentType(Defaults.FULL_CONTENT_TYPE);

        addSessionAttributes(req);
        getRequestParametrs(req);
        this.processRequest(req, resp);
    }

    private void addSessionAttributes(HttpServletRequest req) {

        HttpSession session = req.getSession();
        logger.debug("addSessionAttributes: getSession = " + session.getId());

        String user = req.getRemoteUser();
        logger.debug("getRemoteUser:  = " + user);

        // �������� �� ������ ������������ ������ DaoHandler
        Object object = req.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_DAO);
        if (object != null) {
            this.dH = (daoHandler) object;
        }else{
            logger.error("DaoHandler object in session not initialized!");
        }

        // �������� �� ������ ������������
        Object objectUser = session.getAttribute(Defaults.ATTRIBUTE_SESSION_SIMPLEEMPLOYEE);
        logger.debug("objectUser:  = " + objectUser);
        if (objectUser != null) {
            this.simpleEmployee = (SimpleEmployeeDTO) objectUser;
        } else {
            //����� ���� �������� ������������, �.�. ��� �������� ��������, � ����� ������ ������ � ���������
            //���������������� ����� ������, �� �������� remoteUser ������� � ������������ ����
            this.simpleEmployee = null;
        }

        if(simpleEmployee == null && req.getRemoteUser() != null){

            //������ ������������ �� ������
            SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(this.dH, user,"getSimpleEmployeeByLogin");
            //�������� � ������ ������������
            session.setAttribute(Defaults.ATTRIBUTE_SESSION_SIMPLEEMPLOYEE, simpleEmployee);

        }
    }

    private void getRequestParametrs(HttpServletRequest req) {
        this.ACTION = req.getParameter(Defaults.ACTION_KEY);

        // �������� �� ������ ������������ ������ DaoHandler, ���� ���, �� ���������� �� �������� � �������
        Object object = req.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_DAO);
        if (object != null) {
            this.dH = (daoHandler) object;
        }else{
            logger.error("DaoHandler object in session not initialized!");
            this.ACTION = "ERROR";
        }

        // �������� �� ������ ������������ ������ simpleEmployee, ������ � ��� �� �� "�����"
        Object objectUser = req.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_SIMPLEEMPLOYEE);
        if (objectUser != null) {
            this.simpleEmployee = (SimpleEmployeeDTO) objectUser;
        }
    }

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}

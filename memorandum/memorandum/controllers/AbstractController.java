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

    // Получение логгера, сконфигурированного для данногоо приложения
    Logger logger = Logger.getLogger(getClass().getName());

    // Получение логгера контроля доступа для приложения
    Logger acc_logger = Logger.getLogger(Defaults.LOGGER_ACCESS_NAME);

    // Тип действия
    String ACTION      = null;
    // Пользователь работающий в системе
    SimpleEmployeeDTO simpleEmployee = null;
    // Держатель ссылок на все DAO-компоненты приложения
    daoHandler dH = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // Установим кодировку для данных ЗАПРОСА(REQUEST)
        req.setCharacterEncoding("Cp1251");
        // Установим кодировку для данных ОТВЕТА(RESPONSE)
        resp.setContentType(Defaults.FULL_CONTENT_TYPE);

        addSessionAttributes(req);
        getRequestParametrs(req);
        this.processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Установим кодировку для данных ЗАПРОСА(REQUEST)
        req.setCharacterEncoding("Cp1251");
        // Установим кодировку для данных ОТВЕТА(RESPONSE)
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

        // Получаем из сессии пользователя объект DaoHandler
        Object object = req.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_DAO);
        if (object != null) {
            this.dH = (daoHandler) object;
        }else{
            logger.error("DaoHandler object in session not initialized!");
        }

        // Получаем из сессии пользователя
        Object objectUser = session.getAttribute(Defaults.ATTRIBUTE_SESSION_SIMPLEEMPLOYEE);
        logger.debug("objectUser:  = " + objectUser);
        if (objectUser != null) {
            this.simpleEmployee = (SimpleEmployeeDTO) objectUser;
        } else {
            //Здесь надо обнулять пользователя, т.к. при закрытии браузера, а затем нового захода в программу
            //сформировывается новая сессия, но ПОЧЕМУТО remoteUser берется с преыдыдущего раза
            this.simpleEmployee = null;
        }

        if(simpleEmployee == null && req.getRemoteUser() != null){

            //найдем пользователя по логину
            SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(this.dH, user,"getSimpleEmployeeByLogin");
            //Запихаем в сессию пользователя
            session.setAttribute(Defaults.ATTRIBUTE_SESSION_SIMPLEEMPLOYEE, simpleEmployee);

        }
    }

    private void getRequestParametrs(HttpServletRequest req) {
        this.ACTION = req.getParameter(Defaults.ACTION_KEY);

        // Получаем из сессии пользователя объект DaoHandler, если нет, то отправляем на страницу с ошибкой
        Object object = req.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_DAO);
        if (object != null) {
            this.dH = (daoHandler) object;
        }else{
            logger.error("DaoHandler object in session not initialized!");
            this.ACTION = "ERROR";
        }

        // Получаем из сессии пользователя объект simpleEmployee, данные о нем из БД "Кадры"
        Object objectUser = req.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_SIMPLEEMPLOYEE);
        if (objectUser != null) {
            this.simpleEmployee = (SimpleEmployeeDTO) objectUser;
        }
    }

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}

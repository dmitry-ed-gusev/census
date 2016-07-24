package org.memorandum.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс реализует контроллер приложения "Служебные записки", который выполняет одно действие - показ страницы
 * приветствия.
 *
 * @author Gusev Dmitry
 * @version 1.0
 */

public class welcomeController extends AbstractController {
    // Тип действия: начальный экран входа в систему "Служебки"
    private static final String WELCOME_ACTION = "welcome";
    // Начальная страница для входа в систему "Служебки"
    private static final String WELCOME_PAGE = "/welcome.jsp";
    // Страница с сообщениями об ошибке
    private static final String ERROR_PAGE = "/error.jsp";

    /**
     * Непосредственная обработка поступившего запроса.
     *
     * @param request  HttpServletRequest http-запрос
     * @param response HttpServletResponse http-ответ
     * @throws ServletException ошибка в работе сервлета
     * @throws IOException      ошибка ввода/вывода
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("ENTERING welcomeController -> processRequest().");

        // Начальная страница
        String destPage = ERROR_PAGE;

        // Начальный экран входа в систему
        if (WELCOME_ACTION.equals(ACTION)) {
            destPage = WELCOME_PAGE;
        }

        logger.debug("ACTION: " + ACTION);
        logger.debug("DEST PAGE: " + destPage);

        // Получаем диспетчер данного запроса и перенаправляем запрос
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
        // Непосредственно перенаправление
        dispatcher.forward(request, response);

        logger.debug("LEAVING welcomeController -> processRequest().");
    }

}
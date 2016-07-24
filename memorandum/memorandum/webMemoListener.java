package memorandum;

/**
 * Данный класс реализет т.н. "слушатель" - listener - класс, который отслеживает различные события -
 * создание/уничтожение контекста (старт/стоп всего приложения), создание/уничтожение сессии, установка/удаление
 * переменных контектса.
 * В данном случае отслеживаются только события создания/удаления сессии, для отслеживания количества запущенных сеансов.
 */
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public final class webMemoListener implements ServletContextListener, HttpSessionListener {

    // Логгер приложения используется в данном классе.
    private Logger logger = Logger.getLogger(getClass().getName());
    // Счетчик сессий
    private static Integer sessionsCounter = 0;
    // Контекст сервлета - среда приложения, где этот сервлет запущен (т.е. это контекст всего приложения!).
    private static ServletContext servletContext = null;

    /**
     * Метод вызывается при создании каждой новой сессии. При создании каждой сессии увеличивается счетчик активных
     * сессий. Также в сессию пользователя кладется объект-держатель ссылок на все DAO-компоненты приложения.
     */
    @Override
    public synchronized void sessionCreated(HttpSessionEvent se) {
        sessionsCounter++;
        logger.debug("NEW SESSION [" + se.getSession().getId() + "] CREATED. Sessions counter = " + sessionsCounter);
        // Установим атрибут уровня приложения - счетчик сессий
        if (servletContext != null) {
            servletContext.setAttribute(Defaults.ATTRIBUTE_APPLICATION_USERS, Integer.toString(sessionsCounter));
        } else {
            logger.error("Servlet context is NULL!");
        }
        // Кладем пользователю в сессию объект DaoHandler
        try {
            se.getSession().setAttribute(Defaults.ATTRIBUTE_SESSION_DAO, new daoHandler());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Метод вызывается когда контекст сервлета инициализирован (web-приложение резмещено на сервере и стартовало).
     * При вызове метода он устанавливает атрибут контекста - переменную, которая видна всем сервлетам в контексте.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Инит переменной для хранения цылки на контекст приложения
        servletContext = sce.getServletContext();
        servletContext.setAttribute(Defaults.ATTRIBUTE_APPLICATION_USERS, Integer.toString(sessionsCounter));       
    }

    /**
     * Метод вызывается когда контекст сервлета деинициализирован (приложение удалено с сервера или сервер
     * приложений остановлен).
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("APPLICATION CONTEXT DESTROYED.");
    }

    /**
     * Метод вызывается при уничтожении сессии. Выполняется декрементирование счетчика активных сессий.
     * Также выполняется разблокировка всех записей, заблокированных данной сессией.
     */
    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent se) {
        // Уменьшаем счетчик сессий
        sessionsCounter--;
        logger.debug("SESSION [" + se.getSession().getId() + "] DESTROYED. Sessions counter = " + sessionsCounter);
        // Установим атрибут уровня приложения - счетчик сессий
        if (servletContext != null) {
            servletContext.setAttribute(Defaults.ATTRIBUTE_APPLICATION_USERS, Integer.toString(sessionsCounter));
        } else {
            logger.error("Servlet context is NULL!");
        }
    }

}
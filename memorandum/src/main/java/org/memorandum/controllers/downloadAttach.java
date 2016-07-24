package org.memorandum.controllers;

import org.census.commons.dao.simple.docs.Defaults;
import org.census.commons.dao.simple.docs.daoHandler;
import org.census.commons.dto.docs.memoDTO;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;


public class downloadAttach extends HttpServlet {
    // Получение логгера, сконфигурированного для данного приложения
    Logger logger = Logger.getLogger(getClass().getName());

    // Данный сервлет обрабатывает только метод "GET"
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("ENTERING downloadAttach -> doGet().");
       
        String FullPathName;         // Полное имя файла на HDD сервера(с путем)
        int memoID = 0;    // Храним идентификатор служебки
        memoDTO memo;                 // Храним саму служебку
        daoHandler dH = null;

        // Все действия сервлета выполняются в блоке try...catch - при любой возникшей
        // ошибке запрос перенаправляется на главную страницу (просмотр активных док-в)
        try {

            // Получаем из сессии пользователя объект DaoHandler
            Object object = request.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_DAO);
            if (object != null) {
                dH = (daoHandler) object;
            } else {
                logger.error("DaoHandler object in session not initialized!");
            }

            // Читаем из конфиг-файла приложения каталог для загрузки файлов на сервер
            String upload_dir = getServletContext().getInitParameter(Defaults.UPLOAD_DIR);
            // Путь должен оканчиваться косой чертой. Проверим и скорректируем это (если надо).
            if (!upload_dir.endsWith("\\")) upload_dir = upload_dir.concat("\\");
            // Проверим существование каталога - при ошибке: запись в лог и на главную
            if (!new File(upload_dir).exists()) throw new Exception("Upload dir [" + upload_dir + "] doesn't exists!");
            // Получим входной параметр - ID служебки, чей файл будем скачивать
            try {
                memoID = Integer.parseInt(request.getParameter("memoID"));
            }
            catch (Exception e) {
                throw new Exception("Invalid memoID parameter [" + memoID + "]!");
            }
            // Найдем саму служебку
            memo = dH.getMemoDAO().findByID(dH, memoID);
            // Если служебка не найдена - выход.
            if (memo == null) throw new Exception("Memo with ID = " + memoID + " doesn't exists!");
            // Получим полное имя файла (на жестком диске сервера)
            FullPathName = upload_dir + memo.getId() + "." + memo.getAttachedFile();
            // Перед отправкой файла клиенту выполним все проверки
            File fileToDownload = new File(FullPathName);
            // Если файл существует на диске -> работаем
            if (fileToDownload.exists()) {
                // --- Непосредственно выгрузка файла пользователю ---
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setBufferSize(8192);
                String disHeader = "Attachment; filename=\"" + memo.getId() + "." + memo.getAttachedFile() + "\"";
                response.setHeader("Content-Disposition", disHeader);
                PrintWriter out = response.getWriter();
                // transfer the file byte-by-byte to the response object
                FileInputStream fileInputStream = new FileInputStream(fileToDownload);
                int i;
                while ((i = fileInputStream.read()) != -1) {
                    out.write(i);
                }
                fileInputStream.close();
                out.close();
            } else throw new Exception("File [" + FullPathName + "] doesn't exists!");
        } // <- END OF {TRY} SECTION
        // Перехват ИС
        catch (Exception e) {
            // При возникновении ИС делаем запись в лог
            logger.error("ERROR occured while download file: [" + e.getMessage() + "]");
            // Переходим на главную страницу приложения
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/controller?action=depts");
            dispatcher.forward(request, response);
        }
        logger.debug("LEAVING downloadAttach -> doGet().");
    } // END OF {doGET} METHOD

} // END OF {DOWNLOAD_ATTACH} SERVLET

package org.memorandum.controllers;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Logger;
import org.census.commons.dto.personnel.departments.DepartmentDto;
import org.census.commons.dao.simple.docs.Defaults;
import org.census.commons.dto.docs.memoDTO;
import org.census.commons.dto.docs.recipientDeptDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class memoProcessing extends AbstractController {
    /**
     * Данный сервлет обрабатывает только данные пришедшие методом "POST". Он
     * получает данные типа "multipart/form-data" из формы и обрабатывает их -
     * текст сохраняется в БД, файл сохраняется на диске сервера. Сервлет не имеет
     * графической части - вывода в браузер пользователя - после обработки данных
     * (успешной/не успешной) запрос перенаправляется (через контроллер) на JSP-страницу,
     * которая отображает результат выполнения обработки и ссылочки на различные части
     * данного приложения. Все параметры проверяются по мере их получения. Если проверка завершилась
     * неудачно, то происходит выход - дальнейшая обработка не выполняется.
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Получение логгера, сконфигурированного для данного приложения
        Logger logger = Logger.getLogger(getClass().getName());
        logger.debug("ENTERING newMemoProcessing servlet.");
        String FileName; // Реальное имя файла (с расширением)
        String SavedFileName = null; // Имя сохраняемого файла (с расширением)
        // Список для хранения возникших в процессе работы сервлета ошибок. Если имеет значение null -
        // сервлет отработал без ошибок, если же есть ошибки, то сообщения - в списке.
        List errorsList = null;
        // Имя пользователя совершающего действия
        String remoteUser = null;
        // Список отделов-получателей служебки
        List recipientsList = null;
        // Сотрудник-исполнитель служебки
        //SimpleEmployeeDTO employee;
        // Непосредственно сама служебка
        memoDTO memo = null;
        // Переменная для хранения файла до его записи/удаления. Если данная переменная имеет занчение null,
        // то файл еще не обрабатывался, если же не null, то один файл уже был принят - ограничение на загрузку
        // только одного файла.
        FileItem file = null;
        // Идентификатор только что зарегистрированной служебки (должен быть > 0)
        int newMemoID = -1;

        // Признаак создания новой служебки, по умолчанию ДА
        int createnew = 1;

        // Держатель ссылок на все DAO-компоненты приложения
        //daoHandler dH = null;

        logger.debug("Recieving [upload_dir] and [upload_temp_dir].");
        // Значения каталога для аплоада и временных файлов
        String upload_dir = getServletContext().getInitParameter(Defaults.UPLOAD_DIR);
        String temp_dir = getServletContext().getInitParameter(Defaults.UPLOAD_TEMP_DIR);
        // Размер(в байтах) буфера памяти для временного хранения файлов (i*(1024b*1024b)Kb -> i Mb)
        final int MemBuffer = 1024;
        // макс. размер(указывается в байтах) загружаемого файла (i*(1024b*1024b)Kb -> i Mb)
        final int MaxFileSize = (Integer.parseInt(Defaults.MAX_FILE_SIZE)) * 1024 * 1024;
        logger.debug("Starting data processing.");

        // Непосредственно обработка запроса, пришедшего методом ПОСТ.
        try {
            logger.debug("Recieved remoteUser = " + employee.getShortRusName());

            // Т.к. по умолчанию создатель служебки - зарегистрированный пользователь, то
            // начинаем формировать объект "служебка" сразу после проверки регистрации пользователя
            // (добавляем идентификатор пользователя и идентификатор его отдела)
            memo = new memoDTO();

            // todo: temporary solution!
            Set<DepartmentDto> depts = employee.getDepartments();
            memo.setExecutorDeptID(depts != null && depts.size() > 0 ? depts.iterator().next().getId() : 0);

            memo.setExecutorUserID(employee.getId());
            memo.setUpdateUserID(employee.getId());

            // Путь должен оканчиваться косой чертой. Проверим и скорректируем это (если надо).
            if (!upload_dir.endsWith("\\")) upload_dir = upload_dir.concat("\\");
            // Также и путь к каталогу для временных файлов
            if (!temp_dir.endsWith("\\")) temp_dir = temp_dir.concat("\\");
            logger.debug("Checking [upload_dir] and [upload_temp_dir].");
            // Проверим существование каталога - при ошибке: запись в лог и на главную
            if (!new File(upload_dir).exists()) throw new Exception("Upload dir [" + upload_dir + "] doesn't exists!");
            // Также проверим существование каталога для временных файлов
            if (!new File(temp_dir).exists()) throw new Exception("Temporary dir [" + temp_dir + "] doesn't exists!");
            logger.debug("Checking form data type (data must be multipart).");
            // Проверим тип данных из формы - если данные не multipart -> выход
            if (!FileUpload.isMultipartContent(request)) {
                throw new Exception("FORM DATA IS NOT MULTIPART!");
            }

            logger.debug("Creating DiskFileUpload object.");
            // Создадим объект-обработчик загрузки файла
            DiskFileUpload upload = new DiskFileUpload();
            // Зададим свойства данного объекта:
            upload.setSizeThreshold(MemBuffer);
            upload.setSizeMax(MaxFileSize);
            //Каталог для временного хранения файлов
            upload.setRepositoryPath(temp_dir);
            // Непосредственно разбор запроса
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            logger.debug("Starting form data processing (in cycle).");

            // Непосредственная обработка данных, принятых из формы...
            while (iter.hasNext()) {

                FileItem item = (FileItem) iter.next();

                // ---------- Разбор полей формы ----------
                if (item.isFormField()) {

                    // Получим имя и значение поля из формы (если нужна перекодировка:
                    // new String(item.getString().getBytes(), encoding);)
                    String name = item.getFieldName();
                    String value = item.getString("windows-1251");
                    logger.debug("Recieved data pair: [" + name + " = " + value + "].");

                    // Если это идентификатор служебки и он > 0, то надо его запомнить и добавить в служебку (это значит,
                    // что мы не создаем а изменяем служебку)
                    if (name.equals("memoID")) {
                        // Пробуем перевести идентификатор в целое число
                        try {
                            newMemoID = Integer.parseInt(value);
                        } catch (Exception e) {
                            logger.error("MemoID is invalid [" + value + "]!");
                        }
                        // Если полученный идентификатор больше нуля - добавляем его в служебку
                        if (newMemoID > 0) memo.setId(newMemoID);
                    }

                    // Если это срок ответа на служебку, запишем его в наш объект и в переменную
                    // (если срок установлен - всем отделам-получателям надо установить поле "realized"[выполнено] = 0)
                    else if ((name.equals("realizedDate")) && (value != null) && (!value.trim().equals(""))) {
                        memo.setRealizedDate(value);
                    }
                    // Если это отдел-получатель - заносим его в список
                    else if (name.equals("recipient")) {
                        int deptID = -1;
                        // Пробуем получить идентификатор отдела-получателя
                        try {
                            deptID = Integer.parseInt(value);
                        } catch (Exception e) {
                            logger.error("deptID [" + value + "] is invalid!");
                        }
                        // Если удалось получить идентификатор - добавляем получателя в список
                        if (deptID > 0) {

                            // Новый объект - отдел-получатель служебки
                            recipientDeptDTO recipientDept = new recipientDeptDTO();
                            // Идентификатор отдела-получателя
                            recipientDept.setRecipientDeptID(deptID);
                            // Идентификатор пользователя, создавшего данную запись
                            recipientDept.setUpdateUserID(employee.getId());
                            if (recipientsList == null) recipientsList = new ArrayList();
                            recipientsList.add(recipientDept);

                        }
                    }
                    // Если это тема служебки - добавим ее в служебку
                    else if (name.equals("subject")) {
                        if ((value != null) && (!value.trim().equals(""))) {
                            memo.setSubject(value.replaceAll("'", "\""));
                        } else throw new Exception("Subject is empty!");
                    }
                    // Если это примечание служебки - добавим ее в служебку
                    else if (name.equals("note")) {
                        if ((value != null) && (!value.trim().equals(""))) memo.setNote(value);
                        else throw new Exception("Note is empty!");
                    }
                    // Если это текст служебки - добавим его в служебку
                    else if (name.equals("text")) {
                        if ((value != null) && (!value.trim().equals(""))) {
                            memo.setText(value.trim().replaceAll("'", "\""));
                        } else throw new Exception("Text is empty!");
                    }
                    // Если данная служебка - ответ на другую, то сохраним ID родительской служебки
                    else if (name.equals("parentID")) {
                        int parentID = 0;
                        try {
                            parentID = Integer.parseInt(value);
                        } catch (Exception e) {
                            logger.error("Invalid parentID value!");
                        }
                        // Если получили нормальный номерродительской служебки - сохраняем его
                        if (parentID > 0) memo.setParentID(parentID);
                    }
                    // Проверим есть ли поле с наименованием расширения файла приложения. Сделано для того, чтобы при
                    // редактировании служебки не пропадала ссылка на приложение, т.к. если не выбрать файл
                    // то при update служебки поле по умолчанию обнулится
                    else if (name.equals("fileExt")) {
                        if ((value != null) && (!value.trim().equals(""))) {
                            memo.setAttachedFile(value.trim());
                        }
                    }
                }

                // ---------- Разбор загрузки файла ----------
                else {

                    // Получим размер файла
                    long sizeInBytes = item.getSize();

                    // Если размер файла <= 0 -> нам не передали файл. Если файл не обрабатывали, тогда - вперед.
                    if ((sizeInBytes > 0) && (file == null)) {

                        logger.debug("File size OK [" + sizeInBytes + "]! This is first recieved file. Processing.");
                        // Сохраним файл в переменной (в памяти)
                        file = item;
                        // Получим полное имя файла в рамках файловой системы клиента и заменяем все \ на /
                        String FullFileName = new String(file.getName().getBytes(), Defaults.ENCODING).replace('\\', '/');
                        // Вычленяем только имя файла (имя файла - то, что находится справа от самого правого знака /)
                        FileName = FullFileName.substring(FullFileName.lastIndexOf('/') + 1);
                        // Проверим полученное имя файла
                        if ((FileName == null) || (FileName.trim().equals("")))
                            throw new Exception("Invalid FileName[" + FileName + "] parameter!");
                        // Вычленим расширение файла (то, что находится справа от самого правого знака .)
                        String fileExt = FileName.substring(FileName.lastIndexOf('.') + 1);
                        if ((fileExt == null) || (fileExt.trim().equals("")))
                            throw new Exception("Invalid fileExt[" + fileExt + "] parameter!");
                        // Занесем в объект "служебка" расширение загруженного нам файла
                        memo.setAttachedFile(fileExt);
                        // Формируем имя файла для сохранения (на диске сервера)
                        SavedFileName = "." + fileExt;
                    } // конец обработки принятого файла (if (sizeInBytes > 0))
                }
            } // end of while <- конец цикла обработки данных формы
            logger.debug("Form dataprocessing finished. Processing memo.");

            // Если мы обновляем служебку, а не создаем ее, то каждому получателю надо установить ее идентификатор.
            if ((newMemoID > 0) && (recipientsList != null))
                for (Object aObject : recipientsList) {
                    recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
                    recipientDept.setMemoID(newMemoID);
                }

            // Непосредственное создание служебки или изменение служебки
            logger.debug("Trying create/update memo.");

            // При создании служебки перехватим ошибку и создадим сообщение о ней (ошибке)
            try {
                logger.debug("Selecting operation mode.");
                // Если переменной newMemoID уже присвоено значение - мы обновляем существующую служебку
                if (newMemoID > 0) {
                    logger.debug("OPERATION MODE: *MEMO UPDATING*");
                    dH.getMemoDAO().update(dH, memo, recipientsList);
                    createnew = 0;
                } else {
                    logger.debug("OPERATION MODE: *NEW MEMO CREATING*");
                    newMemoID = dH.getMemoDAO().create(dH, memo, recipientsList);
                }
            } catch (Exception e) {
                // Если возникла ИС, то при создании или изменении служебки возникла ошибка - надо установить значение меньше
                // нуля для того, чтобы файл, принятый нами, не был сохранен на диске, а был бы удален.
                newMemoID = -1;
                throw new Exception("ERROR IN memoDAO.create()/update() FUNCTION [" + e.getMessage() + "]!");
            }
            logger.debug("Memo created/updated successfuly. MemoID = " + newMemoID);

            // Если создание служебки завершилось удачно - сохраняем полученный файл(если он есть)
            if ((newMemoID > 0) && (file != null)) {
                File TmpFile = new File(upload_dir + "/" + newMemoID + SavedFileName);
                file.write(TmpFile);
            }
            // Если же создание служебки завершилось неудачно - удалим файл (если он был)
            else if (file != null) file.delete();
        }// end of try statement

        // Перехватим ИС и сформируем сообщение об ошибке (если она есть)
        catch (Exception e) {
            // Запишем ошибку в лог
            logger.error("ERROR occured while new memo processing: [" + e.getMessage() + "]! user: " + remoteUser);
            // На страницу результата передадим список возникших ошибок
            errorsList = new ArrayList();
            errorsList.add(e.getMessage());
        }
        // Действия, к-ые надо выполнить в любом случае
        finally {
            // Передаем параметр EDIT говорящий, что служебка была отредактирована (если он имеется)
            request.setAttribute("edit", request.getAttribute("edit"));
            // Перед перенаправлением запроса установим атрибут - сообщение о результате. Если сообщение пустое - все в порядке.
            request.setAttribute("error", errorsList);
            // Перенаправим(через контроллер) запрос на страничку, которая отображает результат выполнения действий
            // если это ответ на служебку, то проверим данного чела, имеется ли у него поручение и если да,
            // то ответим на него автоматом, и если что пошлем уведомление шефу
            RequestDispatcher dispatcher;
            if (createnew == 1 && memo.getParentID() > 0 && memo.getExecutorUserID() > 0) {
                dispatcher = getServletContext().getRequestDispatcher("/editor/controller?action=answerAppointResult&memoID=" + memo.getParentID()
                        + "&recipientUserID=" + memo.getExecutorUserID() + "&answer=1&newMemoID=" + newMemoID);
            } else {
                dispatcher = getServletContext().getRequestDispatcher("/editor/controller?action=saveMemoRes&memoID=" + newMemoID);
            }

            //RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/controller?action=viewMemo&memoID=" + newMemoID);
            dispatcher.forward(request, response);
        }
        logger.debug("LEAVING newMemoProcessing servlet.");
    } // end of doPost method

}
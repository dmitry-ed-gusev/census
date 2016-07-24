package org.census.commons.dao.simple.docs;

/**
 * Модуль с константами приложения "СЛУЖЕБНЫЕ ЗАПИСКИ". Данные константы используются в приложении и
 * определяют его функциональность и внешний вид.
 * ВНИМАНИЕ! Изменять данные константы следует с осторожностью!
 *
 */
public interface Defaults {
    
    /**
     * Логгер для ведения журнала аудита приложения "Служебки". В этот журнал записываются
     * попытки доступа (успешные/неуспешные) к защищенным ресурсам данного приложения.
     */
    public final static String LOGGER_ACCESS_NAME = "memorandum_access";
    /**
     * Наименование параметра для задания значения файла конфигурации логгера. В данном файле
     * описывается конфигурация всех журналов (логгеров) для данного приложения.
     */
    //public final static String LOGGER_CONFIG = "log4j_init_file";

    /**
     * Ключ, означающий значение: "тип действия"
     */
    public static final String ACTION_KEY = "action";

    /** Источник данных(база) данной задачи */
    //public static final String MEMO_SOURCE = "java:comp/env/jdbc/MemorandumMSSQLDSpro";
    //public static final String MEMO_SOURCE          = "java:comp/env/jdbc/MemorandumMSSQLDS";
    public static final String MEMO_SOURCE = "java:comp/env/jdbc/memorandum";
    /** Источник данных(база) персонала(кадры) */
    //public static final String PERSONNEL_SOURCE = "java:comp/env/jdbc/PersonnelMSSQLDSpro";
    public static final String PERSONNEL_SOURCE = "java:comp/env/jdbc/memorandum";
    /** Источник данных(база) пользователей(для авторизации) */
    //public static final String USERS_SOURCE = "java:comp/env/jdbc/MySQLUsersDS";
    public static final String USERS_SOURCE = "java:comp/env/jdbc/memorandum";

    /**
     * Кол-во служебок на одной странице в режиме просмотра таблицы. Это кол-во служебок отражается в
     * ящиках "входящие"/"исходящие". Остальные - через поиск или переходом на нужную страницу.
     */
    public static final int PAGE_SIZE = 15;
    /**
     * Обозначение параметра: каталог для сохранения загруженных на сервер файлов.
     */
    public static final String UPLOAD_DIR = "upload_dir";
    /**
     * Обозначение папаметра: каталог для временного хранения загруженных на сервер файлов.
     */
    public static final String UPLOAD_TEMP_DIR = "upload_temp_dir";
    /**
     * Макс. размер файла для загрузки на сервер в Мб
     */
    public static final String MAX_FILE_SIZE = "10";

    /**
     * Формат даты для работы. Дату из БД-источника мы можем получить в каком угодно виде(формате),
     * а нам нужен совершенно определенный(фиксированный) формат, поэтому - преобразовываем по шаблону.
     * Шаблон форматирования(преобразования) даты и представлен ниже. Данный шаблон относится ТОЛЬКО
     * к языку JAVA (НЕ К БД!)!
     */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    /**
     * Формат даты MSSQL
     */
    public static final String DATE_MSSQL_PATTERN = "yyyyMMdd";

    /**
     * Кодировка данных по умолчанию.
     */
    public static final String ENCODING = "windows-1251";
    /**
     * Тип содержимого HTTP запросов/ответов.
     */
    public static final String CONTENT_TYPE = "text/html";
    /**
     * Полный тип содержимого HTTP запросов/ответов.
     */
    public static final String FULL_CONTENT_TYPE = CONTENT_TYPE + ";charset=" + ENCODING;

    /**
     * Статус записи - активна.
     */
    public final static int STATUS_UNDELETED = 0;

    /**
     * Тип ящика сообщений - "входящие" (служебки/сообщения направленные другими отделами в данный отдел).
     */
    public final static String INBOX = "inbox";
    /**
     * Подтип "входящих" сообщений - все "входящие" сообщения.
     */
    public final static String INBOX_ALL = "inbox_all";
    /**
     * Подтип "входящих" сообщений - все "входящие" сообщения, которые требуют ответа, но ответа не имеют.
     */
    public final static String INBOX_NO_ANSWER = "inbox_no_answer";
    /**
     * Подтип "входящих" сообщений - "входящие" сообщения, которые требуют ответа, но ответа не имеют и СРОКИ ПРОШЛИ.
     */
    public final static String INBOX_NO_ANSWER_DATE = "inbox_no_answer_date";

    /**
     * Тип ящика сообщений - "исходящие" (служебки/сообщения направленные данным отделов в другие отделы).
     */
    public final static String OUTBOX = "outbox";
    /**
     * Подтип "исходящих" сообщений - все "исходящие" сообщения.
     */
    public final static String OUTBOX_ALL = "outbox_all";
    /**
     * Подтип "исходящих" сообщений - "исходящие" сообщения, которые требуют ответа получателя, но ответа нет.
     */
    public final static String OUTBOX_NO_ANSWER = "outbox_no_answer";

    /**
     * Диапазон поиска служебок (абс. величина, указывается в месяцах) - это кол-во
     * месяцев будет отсчитано назад от текущей даты для формирования нижней границы даты
     * поиска служебок.
     */
    public final static int SEARCH_INTERVAL = 6;

    /**
     * Наименование параметра "сообщение об ошибке во время работы".
     */
    public final static String ERROR_MSG_PARAM = "errorMsg";

    /**
     * Почтовый сервер для отправки сообщений (SMTP-server).
     */
    public final static String MAIL_HOST = "smtp.rs-head.spb.ru";

    /** Обратный эл. адрес системы "Служебные записки".*/
    public final static String MAIL_FROM = "memorandum@rs-head.spb.ru";

    /** Держатель ссылок на все DAO-компоненты приложения*/
    public static final String ATTRIBUTE_SESSION_DAO = "daoHandler";
    /** Cчетчик сессий*/
    public static final String ATTRIBUTE_APPLICATION_USERS = "usersCounter";
    /** Пользователь работающий в данной сессии*/
    public static final String ATTRIBUTE_SESSION_SIMPLEEMPLOYEE = "simpleEmployee";

}
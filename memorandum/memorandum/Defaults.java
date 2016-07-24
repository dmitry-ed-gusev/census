package memorandum;

import java.util.HashMap;

/**
 * ������ � ����������� ���������� "��������� �������". ������ ��������� ������������ � ���������� �
 * ���������� ��� ���������������� � ������� ���.
 * ��������! �������� ������ ��������� ������� � �������������!
 *
 */
public interface Defaults {
    
    /**
     * ������ ��� ������� ������� ������ ���������� "��������". � ���� ������ ������������
     * ������� ������� (��������/����������) � ���������� �������� ������� ����������.
     */
    public final static String LOGGER_ACCESS_NAME = "memorandum_access";
    /**
     * ������������ ��������� ��� ������� �������� ����� ������������ �������. � ������ �����
     * ����������� ������������ ���� �������� (��������) ��� ������� ����������.
     */
    public final static String LOGGER_CONFIG = "log4j_init_file";

    /**
     * ����, ���������� ��������: "��� ��������"
     */
    public static final String ACTION_KEY = "action";

    /**
     * �������� ������(����) ������ ������
     */
    public static final String MEMO_SOURCE = "java:comp/env/jdbc/MemorandumMSSQLDSpro";
    //public static final String MEMO_SOURCE          = "java:comp/env/jdbc/MemorandumMSSQLDS";

    /**
     * �������� ������(����) ���������(�����)
     */
    public static final String PERSONNEL_SOURCE = "java:comp/env/jdbc/PersonnelMSSQLDSpro";

    /**
     * �������� ������(����) �������������(��� �����������)
     */
    public static final String USERS_SOURCE = "java:comp/env/jdbc/MySQLUsersDS";

    /**
     * ���-�� �������� �� ����� �������� � ������ ��������� �������. ��� ���-�� �������� ���������� �
     * ������ "��������"/"���������". ��������� - ����� ����� ��� ��������� �� ������ ��������.
     */
    public static final int PAGE_SIZE = 15;
    /**
     * ����������� ���������: ������� ��� ���������� ����������� �� ������ ������.
     */
    public static final String UPLOAD_DIR = "upload_dir";
    /**
     * ����������� ���������: ������� ��� ���������� �������� ����������� �� ������ ������.
     */
    public static final String UPLOAD_TEMP_DIR = "upload_temp_dir";
    /**
     * ����. ������ ����� ��� �������� �� ������ � ��
     */
    public static final String MAX_FILE_SIZE = "10";

    /**
     * ������ ���� ��� ������. ���� �� ��-��������� �� ����� �������� � ����� ������ ����(�������),
     * � ��� ����� ���������� ������������(�������������) ������, ������� - ��������������� �� �������.
     * ������ ��������������(��������������) ���� � ����������� ����. ������ ������ ��������� ������
     * � ����� JAVA (�� � ��!)!
     */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    /**
     * ������ ���� MSSQL
     */
    public static final String DATE_MSSQL_PATTERN = "yyyyMMdd";

    /**
     * ��������� ������ �� ���������.
     */
    public static final String ENCODING = "windows-1251";
    /**
     * ��� ����������� HTTP ��������/�������.
     */
    public static final String CONTENT_TYPE = "text/html";
    /**
     * ������ ��� ����������� HTTP ��������/�������.
     */
    public static final String FULL_CONTENT_TYPE = CONTENT_TYPE + ";charset=" + ENCODING;

    /**
     * ������ ������ - �������.
     */
    public final static int STATUS_UNDELETED = 0;

    /**
     * ��� ����� ��������� - "��������" (��������/��������� ������������ ������� �������� � ������ �����).
     */
    public final static String INBOX = "inbox";
    /**
     * ������ "��������" ��������� - ��� "��������" ���������.
     */
    public final static String INBOX_ALL = "inbox_all";
    /**
     * ������ "��������" ��������� - ��� "��������" ���������, ������� ������� ������, �� ������ �� �����.
     */
    public final static String INBOX_NO_ANSWER = "inbox_no_answer";
    /**
     * ������ "��������" ��������� - "��������" ���������, ������� ������� ������, �� ������ �� ����� � ����� ������.
     */
    public final static String INBOX_NO_ANSWER_DATE = "inbox_no_answer_date";

    /**
     * ��� ����� ��������� - "���������" (��������/��������� ������������ ������ ������� � ������ ������).
     */
    public final static String OUTBOX = "outbox";
    /**
     * ������ "���������" ��������� - ��� "���������" ���������.
     */
    public final static String OUTBOX_ALL = "outbox_all";
    /**
     * ������ "���������" ��������� - "���������" ���������, ������� ������� ������ ����������, �� ������ ���.
     */
    public final static String OUTBOX_NO_ANSWER = "outbox_no_answer";

    /**
     * �������� ������ �������� (���. ��������, ����������� � �������) - ��� ���-��
     * ������� ����� ��������� ����� �� ������� ���� ��� ������������ ������ ������� ����
     * ������ ��������.
     */
    public final static int SEARCH_INTERVAL = 6;

    /**
     * ������������ ��������� "��������� �� ������ �� ����� ������".
     */
    public final static String ERROR_MSG_PARAM = "errorMsg";

    /**
     * �������� ������ ��� �������� ��������� (SMTP-server).
     */
    public final static String MAIL_HOST = "smtp.rs-head.spb.ru";

    /** �������� ��. ����� ������� "��������� �������".*/
    public final static String MAIL_FROM = "memorandum@rs-head.spb.ru";

    /** ��������� ������ �� ��� DAO-���������� ����������*/
    public static final String ATTRIBUTE_SESSION_DAO = "daoHandler";
    /** C������ ������*/
    public static final String ATTRIBUTE_APPLICATION_USERS = "usersCounter";
    /** ������������ ���������� � ������ ������*/
    public static final String ATTRIBUTE_SESSION_SIMPLEEMPLOYEE = "simpleEmployee";
   
}
package memorandum.dataModel.dto;

/**
 * ����� ��������� ���� ������ �� ������� �������������(mapping) deptsEmailsMapping.
 * ��� ���������� ����������� � ������� �������� ������������ ����� deptsEmailsDAO.
 *
 * @author Gusev Dmitry
 * @version 1.0
 */
public class deptsEmailsMappingDTO{
    /*������������� ��������**/
    private int id;
    /**
     * ������������� ������������� ��"�����"
     */
    private int deptID;
    /**
     * ��� ������ ���� ������������
     */
    private String deptname;
    /**
     * ������������� E-mail �� LDAP
     */
    private int emailId;
    /**
     * E-mail �� LDAP
     */
    private String email;

    public deptsEmailsMappingDTO() {
        this.id = -1;
        this.deptID = 0;
        this.deptname = null;
        this.emailId = 0;
        this.email = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
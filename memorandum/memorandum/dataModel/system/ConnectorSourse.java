package memorandum.dataModel.system;

import jpersonnel.dto.*;
import memorandum.daoHandler;
import java.sql.Connection;
import java.util.*;

import static jpersonnel.JPersonnelConsts.DeptType;

public class ConnectorSourse extends Connectors implements Comparator<SimpleEmployeeDTO> {


    public ConnectorSourse() {
    }

    public Object getSomething(daoHandler dH, Object addParametr, String method){
        logger.debug("ENTERING into getSomething.findAllList(" +method+ ").");
        Connection conn = null;
        Object obj = null;

        try {
            //TODO://��������� ������ ������������� ��������
            if(method.equals("getDepartmentsList")){
                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getDepartmentsList(conn, (DeptType)addParametr);}

            //TODO://����� ��������� ����� ������������� �� �� "�����" �� ��� ��������������
            else if(method.equals("getSimpleDepartmentByID")){
                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getDepartmentById(conn, (Integer)addParametr);}

            //TODO://����� ��������� ����� ���������� �� �� "�����"  �� ��� ��������������
            else if(method.equals("getSimpleEmployeeByID")){
                conn = this.getPersonnelConnection();
                logger.debug("@@@@@@@@1");
                obj = dH.getPersonnelNewDAO().getSimpleEmployeeById(conn, (Integer)addParametr);
                logger.debug("@@@@@@@@2");
            }


            //TODO://����� ��������� ����� ���������� ������������� �� �� "�����" �� �������������� ������
            else if(method.equals("getChiefDepartment")){
                conn = this.getPersonnelConnection();
                SimpleEmployeeDTO chief = dH.getPersonnelNewDAO().getChiefForDepartmentId(conn, (Integer)addParametr);

                if(chief != null){
                    obj = this.getSomething(dH, chief.getPersonnelId(), "getSimpleEmployeeByID");
                }
            }
                
            //����� ��������� ����� ���������� �� �� "�����" �� ��� ������ � LDAP            
            else if(method.equals("getSimpleEmployeeByLogin")){
                conn = this.getUsersConnection();
                logger.debug("getUsersConnection = " + conn);
                int personnelId = dH.getPersonnelNewDAO().getPersonnelIdByLogin(conn, (String)addParametr);

                //���� �����, �� ������� ��� �������� � ��� �� ��"�����"
                if (personnelId > 0) {
                    obj = this.getSomething(dH, personnelId, "getSimpleEmployeeByID");                    
                }
            }

            //TODO://������ ����� ���������� ������ ����������� ��� �� �� "�����" �� ����������.
            //������� � ������ ����� ������������� �� �������.
            else if (method.equals("findAllPersonnelWithSimpleEmployeeDTO")) {

                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getEmployeesList(conn, (SimpleEmployeeDTO) addParametr);

                //���� ��� �� ���� ����������� �� �������
                if (obj != null) {
                    Collections.sort((List) obj, this);
                }
            }

            //TODO://������ ����� ���������� ������ ���� ����������� ��� �� �� "�����", ��� ������� �������������.
            //������� � ������ ����� ������������� �� �������.
            else if (method.equals("findAllPersonnelWithDepartmentId")) {

                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getEmployeesList(conn, (Integer)addParametr);

                //���������� ������ �� ���� Family
                Collections.sort((List) obj, this);
            }

            //����� ��������� ����� Email � LDAP, �� ��������������
            else if(method.equals("getEmailById")){
                conn = this.getUsersConnection();
                obj = dH.getPersonnelNewDAO().getEmailById(conn, (Integer)addParametr);}


            //����� ��������� ����� ���������� � LDAP, � ��� �� �������� Email, �� �������������� �� �� "�����"
            else if(method.equals("getUserEmailByPersonnelId")){
                conn = this.getUsersConnection();
                obj = dH.getPersonnelNewDAO().getUsersWithMainEmail(conn, (Integer)addParametr);}

            //����� ��������� ����� ���� Email � LDAP
            else if(method.equals("getAllEmails")){
                logger.debug("WWWWWWWWWW");
                conn = this.getUsersConnection();logger.debug("WWWWWWWWWW2");
                obj = dH.getPersonnelNewDAO().getAllEmails(conn);logger.debug("WWWWWWWWWW3");}

        }
        // �������� ��
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // ����������� ����� ���������� �������
        finally {
            try {if (conn != null) conn.close();}
            catch (Exception e_res) {logger.error("Can't close connection! [" + e_res.getMessage() + "]");}
        }
        logger.debug("LEAVING into ConnectorSourse.getSomething(" +method+").");
        return obj;
    }

    /**
     * ����� ��� ���������� �������� SimpleEmployeeDTO, ����������� � ������, �� ���� Family
     */
    public int compare(SimpleEmployeeDTO o1, SimpleEmployeeDTO o2) {
        return o1.getFamily().compareTo(o2.getFamily());
    }
}

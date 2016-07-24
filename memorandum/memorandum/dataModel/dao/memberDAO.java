package memorandum.dataModel.dao;

import jpersonnel.dto.*;
import memorandum.*;
import memorandum.dataModel.system.Connectors;

import java.util.*;

/**
 * ������ ����� ��������� ������ - ��������� ���. ������ ���������� �������
 * �� �� "�����". ����� ��������� ������ ������ ����������� �� ������ ����������,
 * ��������� � �� "�����" � ������� ������� ������ �� ������.
 * ����� �������� � PersonnelDAO ������� �������� ����������� ���������� IPersonnelDAO,
 * ������������� �� �� ����������� (LDAP-MySQL) � �� ����� (MS SQL 2005).
 */
public class memberDAO extends Connectors {


    public memberDAO() {
    }       

    /**
     * ����� �������������� ������ ����������� (�������� SimpleEmployeeDTO) �� ���������������,
     * ������� ���������� ��������� �������.
     * ���� �� ������ ������� �� ������� - ����� ������ �������� null.
     *
     * @param dH - ��������� ������ �� ��� DAO-���������� ����������
     * @param members - ��������� ������ ID ����������� �� �� "�����"
     * @return - ������ �������� SimpleEmployeeDTO �����������
     */
    public List findMemberID(daoHandler dH, String[] members) {
        logger.debug("ENTERING into memberDAO.findByMemberIDList().");
        int personnelId; // <- �������� ������ ��� �������� �������������
        List<SimpleEmployeeDTO> memberList = null;
        try {
            // ���� ������ ���� ��� null - ���������� ������
            if ((members == null) || (members.length <= 0)) throw new Exception("Memberss list are EMPTY!");
            // ��������� ���������� ��� ������ ��������������� - � �����
            for (String members2 : members) {
                personnelId = -1;
                // ����� ������� ������������� (���������)
                try {
                    personnelId = Integer.parseInt(members2);
                }
                catch (Exception e) {
                    logger.error("MemberID [" + members2 + "] is invalid!");
                }
                // ���� ������� ��������� ������������� �� ������ � ����� - ���� ������������ �� �����
                if (personnelId > 0) {
                    // ������ "���������" - ��� �������� �������������� � ���. ����
                    SimpleEmployeeDTO simpleEmployee = (SimpleEmployeeDTO)dH.getConnectorSource().getSomething(dH, personnelId, "getSimpleEmployeeByID");
                    // �������������� ������
                    if (memberList == null) memberList = new ArrayList<SimpleEmployeeDTO>();
                    memberList.add(simpleEmployee);
                }
            }
        }
        // �������� ��
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        logger.debug("LEAVING memberDAO.findByDeptIDList().");
        return memberList;
    }

}

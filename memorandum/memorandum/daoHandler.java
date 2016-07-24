package memorandum;

import jpersonnel.implementations.PersonnelDAO;
import jpersonnel.implementations.PersonnelNewDAO;
import memorandum.dataModel.dao.*;
import memorandum.dataModel.system.ConnectorSourse;

public final class daoHandler {
    private final deptsEmailsMappingDAO     deptsEmailsMappingDAO;
    //private final deptsPersonnelMappingDAO  deptsPersonnelMappingDAO;
    private final memberDAO                 memberDAO;
    private final memoDAO                   memoDAO;
    private final recipientDeptDAO          recipientDeptDAO;
    private final recipientUserDAO          recipientUserDAO;
    private final ConnectorSourse           connectorSource;
    //private final PersonnelDAO              personnelDAO;
    private final PersonnelNewDAO           personnelNewDAO;


    public daoHandler() {
        this.deptsEmailsMappingDAO = new deptsEmailsMappingDAO();
        //this.deptsPersonnelMappingDAO = new deptsPersonnelMappingDAO();
        this.memberDAO = new memberDAO();
        this.memoDAO = new memoDAO();
        this.recipientDeptDAO = new recipientDeptDAO();
        this.recipientUserDAO = new recipientUserDAO();
        this.connectorSource = new ConnectorSourse();
        //this.personnelDAO = new PersonnelDAO();
        this.personnelNewDAO = new PersonnelNewDAO();
    }

    public deptsEmailsMappingDAO getDeptsEmailsMappingDAO() {
        return deptsEmailsMappingDAO;
    }

//    public deptsPersonnelMappingDAO getDeptsPersonnelMappingDAO() {
//        return deptsPersonnelMappingDAO;
//    }

    public memberDAO getMemberDAO() {
        return memberDAO;
    }

    public memoDAO getMemoDAO() {
        return memoDAO;
    }

    public recipientDeptDAO getRecipientDeptDAO() {
        return recipientDeptDAO;
    }

    public recipientUserDAO getRecipientUserDAO() {
        return recipientUserDAO;
    }

    public ConnectorSourse getConnectorSource() {
        return connectorSource;
    }

//    public PersonnelDAO getPersonnelDAO() {
//        return personnelDAO;
//    }

    public PersonnelNewDAO getPersonnelNewDAO() {
        return personnelNewDAO;
    }
}
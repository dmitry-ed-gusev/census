package org.census.commons.dao.simple.docs;

//import jpersonnel.implementations.PersonnelNewDAO;

public final class daoHandler {
    //private final org.census.commons.dao.simple.docs.deptsEmailsMappingDAO deptsEmailsMappingDAO;
    //private final deptsPersonnelMappingDAO  deptsPersonnelMappingDAO;
    private final org.census.commons.dao.simple.docs.memberDAO memberDAO;
    private final org.census.commons.dao.simple.docs.memoDAO memoDAO;
    private final org.census.commons.dao.simple.docs.recipientDeptDAO recipientDeptDAO;
    private final org.census.commons.dao.simple.docs.recipientUserDAO recipientUserDAO;
    //private final ConnectorSourse connectorSource;
    //private final PersonnelNewDAO           personnelNewDAO;


    public daoHandler() {
        //this.deptsEmailsMappingDAO = new deptsEmailsMappingDAO();
        //this.deptsPersonnelMappingDAO = new deptsPersonnelMappingDAO();
        this.memberDAO = new memberDAO();
        this.memoDAO = new memoDAO();
        this.recipientDeptDAO = new recipientDeptDAO();
        this.recipientUserDAO = new recipientUserDAO();
        //this.connectorSource = new ConnectorSourse();
        //this.personnelNewDAO = new PersonnelNewDAO();
    }

    //public deptsEmailsMappingDAO getDeptsEmailsMappingDAO() {
    //    return deptsEmailsMappingDAO;
    //}

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

   // public ConnectorSourse getConnectorSource() {
   //     return connectorSource;
   // }

}
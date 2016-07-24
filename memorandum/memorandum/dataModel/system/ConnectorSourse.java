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
            //TODO://Получение списка подразделений Регистра
            if(method.equals("getDepartmentsList")){
                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getDepartmentsList(conn, (DeptType)addParametr);}

            //TODO://Метод выполняет поиск подразделения из БД "Кадры" по его идентификатору
            else if(method.equals("getSimpleDepartmentByID")){
                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getDepartmentById(conn, (Integer)addParametr);}

            //TODO://Метод выполняет поиск сотрудника из БД "Кадры"  по его идентификатору
            else if(method.equals("getSimpleEmployeeByID")){
                conn = this.getPersonnelConnection();
                logger.debug("@@@@@@@@1");
                obj = dH.getPersonnelNewDAO().getSimpleEmployeeById(conn, (Integer)addParametr);
                logger.debug("@@@@@@@@2");
            }


            //TODO://Метод выполняет поиск начальника подразделения из БД "Кадры" по идентификатору отдела
            else if(method.equals("getChiefDepartment")){
                conn = this.getPersonnelConnection();
                SimpleEmployeeDTO chief = dH.getPersonnelNewDAO().getChiefForDepartmentId(conn, (Integer)addParametr);

                if(chief != null){
                    obj = this.getSomething(dH, chief.getPersonnelId(), "getSimpleEmployeeByID");
                }
            }
                
            //Метод выполняет поиск сотрудника из БД "Кадры" по его логину в LDAP            
            else if(method.equals("getSimpleEmployeeByLogin")){
                conn = this.getUsersConnection();
                logger.debug("getUsersConnection = " + conn);
                int personnelId = dH.getPersonnelNewDAO().getPersonnelIdByLogin(conn, (String)addParametr);

                //Если нашли, то получим все сведения о нем из БД"Кадры"
                if (personnelId > 0) {
                    obj = this.getSomething(dH, personnelId, "getSimpleEmployeeByID");                    
                }
            }

            //TODO://Данный метод возвращает список сотрудников ГУР из БД "КАДРЫ" по параметрам.
            //Объекты в списке будут отсортированы по фамилии.
            else if (method.equals("findAllPersonnelWithSimpleEmployeeDTO")) {

                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getEmployeesList(conn, (SimpleEmployeeDTO) addParametr);

                //Если что то есть отсортируем по фамилии
                if (obj != null) {
                    Collections.sort((List) obj, this);
                }
            }

            //TODO://Данный метод возвращает список всех сотрудников ГУР из БД "КАДРЫ", для данного подразделения.
            //Объекты в списке будут отсортированы по фамилии.
            else if (method.equals("findAllPersonnelWithDepartmentId")) {

                conn = this.getPersonnelConnection();
                obj = dH.getPersonnelNewDAO().getEmployeesList(conn, (Integer)addParametr);

                //Сортировка списка по полю Family
                Collections.sort((List) obj, this);
            }

            //Метод выполняет поиск Email в LDAP, по идентификатору
            else if(method.equals("getEmailById")){
                conn = this.getUsersConnection();
                obj = dH.getPersonnelNewDAO().getEmailById(conn, (Integer)addParametr);}


            //Метод выполняет поиск сотрудника в LDAP, а так же основной Email, по идентификатору из БД "Кадры"
            else if(method.equals("getUserEmailByPersonnelId")){
                conn = this.getUsersConnection();
                obj = dH.getPersonnelNewDAO().getUsersWithMainEmail(conn, (Integer)addParametr);}

            //Метод выполняет поиск всех Email в LDAP
            else if(method.equals("getAllEmails")){
                logger.debug("WWWWWWWWWW");
                conn = this.getUsersConnection();logger.debug("WWWWWWWWWW2");
                obj = dH.getPersonnelNewDAO().getAllEmails(conn);logger.debug("WWWWWWWWWW3");}

        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        // Обязательно нужно освободить ресурсы
        finally {
            try {if (conn != null) conn.close();}
            catch (Exception e_res) {logger.error("Can't close connection! [" + e_res.getMessage() + "]");}
        }
        logger.debug("LEAVING into ConnectorSourse.getSomething(" +method+").");
        return obj;
    }

    /**
     * Метод для сортировки объектов SimpleEmployeeDTO, находящихся в списке, по полю Family
     */
    public int compare(SimpleEmployeeDTO o1, SimpleEmployeeDTO o2) {
        return o1.getFamily().compareTo(o2.getFamily());
    }
}

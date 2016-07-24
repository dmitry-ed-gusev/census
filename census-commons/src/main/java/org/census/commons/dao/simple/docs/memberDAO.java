package org.census.commons.dao.simple.docs;

import org.census.commons.dao.hibernate.personnel.EmployeesSimpleDao;
import org.census.commons.dto.personnel.employees.EmployeeDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Данный класс реализует объект - сотрудник ГУР. Данные сотрудника берутся
 * из БД "Кадры". Метод позволяет только искать сотрудников по разным параметрам,
 * изменения в БД "Кадры" в функции данного класса не входят.
 * Класс работает с PersonnelDAO который является реализацией интерфейса IPersonnelDAO,
 * базируеющийся на БД Авторизация (LDAP-MySQL) и БД Кадры (MS SQL 2005).
 */
public class memberDAO extends Connectors {

    private EmployeesSimpleDao employeesDao;

    public memberDAO() {
    }       

    /**
     * Метод подготавливает список сотрудников (объектов SimpleEmployeeDTO) по идентификаторам,
     * которые передаются строковым списком.
     * Если ни одного объекта не найдено - метод вернет значение null.
     *
     * @param dH - Держатель ссылок на все DAO-компоненты приложения
     * @param members - строковый список ID сотрудников из БД "Кадры"
     * @return - список объектов SimpleEmployeeDTO сотрудников
     */
    public List findMemberID(daoHandler dH, String[] members) {
        logger.debug("ENTERING into memberDAO.findByMemberIDList().");
        int personnelId; // <- временно храним тут числовой идентификатор
        List<EmployeeDto> memberList = null;
        try {
            // Если список пуст или null - генерируем ошибку
            if ((members == null) || (members.length <= 0)) throw new Exception("Memberss list are EMPTY!");
            // Разбираем переданный нам список идентификаторов - в цикле
            for (String members2 : members) {
                personnelId = -1;
                // Берем текущий идентификатор (строковый)
                try {
                    personnelId = Integer.parseInt(members2);
                }
                catch (Exception e) {
                    logger.error("MemberID [" + members2 + "] is invalid!");
                }
                // Если удалось перевести идентификатор из строки в число - ищем пользователя БД Кадры
                if (personnelId > 0) {
                    // Объект "сотрудник" - для проверки идентификатора и доп. инфы
                    EmployeeDto simpleEmployee = this.employeesDao.findById((long) personnelId);
                    // Инициализируем список
                    if (memberList == null) memberList = new ArrayList<>();
                    memberList.add(simpleEmployee);
                }
            }
        }
        // Перехват ИС
        catch (Exception e) {
            logger.error("Error occured: " + e.getMessage());
        }
        logger.debug("LEAVING memberDAO.findByDeptIDList().");
        return memberList;
    }

}

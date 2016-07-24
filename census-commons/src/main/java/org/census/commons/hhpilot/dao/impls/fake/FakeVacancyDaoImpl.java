package org.census.commons.hhpilot.dao.impls.fake;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.hhpilot.connection.interfaces.ConnectionManagerInterface;
import org.census.commons.hhpilot.dao.abstraction.AbstractDao;
import org.census.commons.hhpilot.dao.interfaces.VacancyDaoInterface;
import org.census.commons.dto.hr.VacancyDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fake implementation of VacancyDaoInterface. We need this implementation only for first time.
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 03.12.12)
 */
public class FakeVacancyDaoImpl extends AbstractDao implements VacancyDaoInterface {

        // module logger
    private Log log = LogFactory.getLog(FakeVacancyDaoImpl.class);

    public FakeVacancyDaoImpl(ConnectionManagerInterface connectionManager) {
        super(connectionManager);
    }

    @Override
    public List<VacancyDto> getAllVacancy() throws SQLException {
        log.debug("FakeVacancyDaoImpl.getAllVacancy() working.");
        List<VacancyDto> vacancyList = new ArrayList<VacancyDto>();
        vacancyList.add(new VacancyDto("фирма 1", "прогер", "java", 1));
        vacancyList.add(new VacancyDto("фирма 2", "прогер", "html", 1));
        vacancyList.add(new VacancyDto("фирма 3", "прогер", "fox", 2));
        vacancyList.add(new VacancyDto("фирма 1", "верстальщик", "photo", 2));
        return vacancyList;
    }

    @Override
    public VacancyDto getVacancyById(int id) throws SQLException {
        log.debug("FakeVacancyDaoImpl.getVacancyById() working.");
        return new VacancyDto("", "", "comment ZZZ gfklfgj", 0);
    }

}

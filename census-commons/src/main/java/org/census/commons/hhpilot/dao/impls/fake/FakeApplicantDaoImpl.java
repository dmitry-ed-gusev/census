package org.census.commons.hhpilot.dao.impls.fake;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.hhpilot.connection.interfaces.ConnectionManagerInterface;
import org.census.commons.hhpilot.dao.abstraction.AbstractDao;
import org.census.commons.hhpilot.dao.interfaces.ApplicantDaoInterface;
import org.census.commons.hhpilot.domain.ApplicantDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fake implementation of ApplicantDaoInterface. We need this implementation only for first time.
 * @author Gusev Dmitry (gusevd)
 * @version 1.0 (DATE: 14.11.12)
 */

public class FakeApplicantDaoImpl extends AbstractDao implements ApplicantDaoInterface {

    // module logger
    private Log log = LogFactory.getLog(FakeApplicantDaoImpl.class);

    public FakeApplicantDaoImpl(ConnectionManagerInterface connectionManager) {
       super(connectionManager);
    }

    @Override
    public List<ApplicantDto> getAllApplicants() throws SQLException {
        log.debug("FakeApplicantDaoImpl.getAllApplicants() working.");
        List<ApplicantDto> applicantsList = new ArrayList<ApplicantDto>();
        applicantsList.add(new ApplicantDto("Дмитрий", "Эдуардович", "Гусев", true, "comment йыцапау"));
        applicantsList.add(new ApplicantDto("Сергей", "Александрович", "Иванов", true, "йцупшоцелдуо цуепр цеу п еуцп уцпукп"));
        applicantsList.add(new ApplicantDto("Александр", "Александрович", "Петров", true, "лкполдукпо упдлкоуцпдлкауц пкуцп"));
        applicantsList.add(new ApplicantDto("Егор", "Петрович", "Исаев", true, "цкероцкенр цуенрцнр кценрцен"));
        applicantsList.add(new ApplicantDto("Петр", "Михайлович", "Мишин", true, "цкенрлдоцкен цкнодцке йзщцйшшеьвяыж фыпзжваып"));
        return applicantsList;
    }

    @Override
    public ApplicantDto getApplicantById(int id) throws SQLException {
        log.debug("FakeApplicantDaoImpl.getApplicantById() working.");
        return new ApplicantDto("", "", "", true, "comment ZZZ gfklfgj");
    }

}
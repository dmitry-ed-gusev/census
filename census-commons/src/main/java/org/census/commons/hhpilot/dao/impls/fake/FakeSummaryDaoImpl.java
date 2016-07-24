package org.census.commons.hhpilot.dao.impls.fake;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.hhpilot.connection.interfaces.ConnectionManagerInterface;
import org.census.commons.hhpilot.dao.abstraction.AbstractDao;
import org.census.commons.hhpilot.dao.interfaces.SummaryDaoInterface;
import org.census.commons.hhpilot.domain.SummaryDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fake implementation of SummaryDaoInterface. We need this implementation only for first time.
 * @author Ivanov Sergey
 * @version 1.0 (DATE: 03.12.12)
 */

public class FakeSummaryDaoImpl extends AbstractDao implements SummaryDaoInterface {

    // module logger
    private Log log = LogFactory.getLog(FakeSummaryDaoImpl.class);

    public FakeSummaryDaoImpl(ConnectionManagerInterface connectionManager) {
        super(connectionManager);
    }

    @Override
    public List<SummaryDto> getAllSummary() throws SQLException {
        log.debug("FakeSummaryDaoImpl.getAllSummary() working.");
        List<SummaryDto> summaryList = new ArrayList<SummaryDto>();
        summaryList.add(new SummaryDto("программист", "джава", "java", "слабоват", 1));
        summaryList.add(new SummaryDto("программист", "хтмл", "html", "пригласить", 1));
        summaryList.add(new SummaryDto("программист", "фокс", "fox", "не нужен", 2));
        summaryList.add(new SummaryDto("бухгалтер", "1С", "", "так себе", 2));
        return summaryList;
    }

    @Override
    public SummaryDto getSummaryById(int id) throws SQLException {
        log.debug("FakeSummaryDaoImpl.getSummaryById() working.");
        return new SummaryDto("", "", "", "comment ZZZ gfklfgj", 0);
    }

}

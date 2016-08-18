package org.census.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for generating organization phone book in HTML format.
 * Phone book is generated from database.
 * Created by vinnypuhh on 16.08.2016.
 */

@Service
@Transactional
public class HtmlPhoneBookService {
    // todo: needs -> employees dao, departments dao + hierarchy of depts
}

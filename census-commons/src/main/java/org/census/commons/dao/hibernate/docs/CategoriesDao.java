package org.census.commons.dao.hibernate.docs;

import org.census.commons.dao.AbstractHibernateDao;
import org.census.commons.dto.docs.CategoryDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO component (service) for manipulating with domain objects CategoryDto.
 * @author Gusev Dmitry (Dmitry)
 * @version 1.0 (DATE: 23.03.2014)
 * @deprecated don't use it!
*/

@Repository
// @Service("categoriesService") // <- we can use one of annotation @Service or @Repository
@Transactional
public class CategoriesDao extends AbstractHibernateDao<CategoryDto> {

    public CategoriesDao() {
        //this.setClazz(CategoryDto.class);
        super(CategoryDto.class);
    }

}
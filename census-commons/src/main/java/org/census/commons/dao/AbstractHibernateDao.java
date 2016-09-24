package org.census.commons.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.census.commons.dto.AbstractEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Abstract Hibernate Dao component, uses plain Hibernate (not JPA).
 * @author Gusev Dmitry (gusevd)
 * @version 2.0 (DATE: 03.01.2015)
*/

@Repository
@Transactional
public abstract class AbstractHibernateDao <T extends AbstractEntity> {

    private Log log = LogFactory.getLog(AbstractHibernateDao.class);

    @Autowired @Qualifier("censusSessionFactory")
    private SessionFactory sessionFactory; // <- Hibernate session factory
    private Class<T>       clazz; // <- class used for this DAO component (generic)

    public AbstractHibernateDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    /***/
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Find all objects.
     */
    public List<T> findAll() {
        log.debug(String.format("Retrieving all [%s] objects.", this.clazz.getSimpleName()));
        return this.sessionFactory.getCurrentSession().createQuery("FROM " + clazz.getName()).list();
    }

    /**
     * Find all active (not deleted) objects.
     */
    // todo: implement!
    public List<T> findAllActive() {
        //log.debug(String.format("Retrieving all active (not deleted) [%s] objects.", this.clazz.getSimpleName()));
        //return this.sessionFactory.getCurrentSession().createQuery("FROM " + clazz.getName() + " where ").list();
        throw new NotYetImplementedException();
    }

    /**
     * Find one entity by ID.
    */
    public T findById(Long id) {
        log.debug(String.format("Search object [%s] by ID = [%s].", this.clazz.getSimpleName(), id));
        return (T) this.sessionFactory.getCurrentSession().get(clazz, id);
    }

    /**
     * Save entity.
    */
    public void save(T entity) {
        log.debug(String.format("Saving new object [%s].", this.clazz.getSimpleName()));
        this.sessionFactory.getCurrentSession().save(entity);
    }

    /**
     * Update entity.
    */
    public void update(T entity) {
        log.debug(String.format("Updating existing object [%s].", this.clazz.getSimpleName()));
        this.sessionFactory.getCurrentSession().update(entity);
    }

    /**
     * Save or update entity based on primary key value.
     */
    public void saveOrUpdate(T entity) {
        log.debug(String.format("Saving or updating entity [%s].", this.clazz.getSimpleName()));
        this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    /**
     * Delete an existing entity by ID.
     * @param id the id of the existing category.
     */
    public void deleteById(Long id) {
        log.debug(String.format("Deleting existing object [%s] by ID = [%s].", this.clazz.getSimpleName(), id));
        this.delete(this.findById(id));
    }

    /**
     * Delete an existing entity object.
     */
    public void delete(T entity) {
        log.debug(String.format("Deleting existing object [%s].", this.clazz.getSimpleName()));
        this.sessionFactory.getCurrentSession().delete(entity);
    }

    /***/
    public void merge (T entity) {
        log.debug(String.format("Merging object [%s].", this.clazz.getSimpleName()));
        this.sessionFactory.getCurrentSession().merge(entity);
    }

}
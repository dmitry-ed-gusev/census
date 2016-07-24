package org.census.commons.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract JPA Dao component. If application will use JPA (provider - Hibernate), we may
 * create customized DAO's by inheriting from this component.
 *
 * @author Gusev Dmitry (dgusev)
 * @version 2.0 (DATE: 23.03.14)
 */

@Repository
@Transactional
public abstract class AbstractJpaDao<T extends Serializable> {

    private Class<T> clazz;

    @PersistenceContext
    private EntityManager entityManager;

    /***/
    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    /***/
    public T findOne(Long id) {
        return entityManager.find(clazz, id);
    }

    /***/
    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    /***/
    public void save(T entity) {
        entityManager.persist(entity);
    }

    /***/
    public void update(T entity) {
        entityManager.merge(entity);
    }

    /***/
    public void delete(T entity) {
        entityManager.remove(entity);
    }

    /***/
    public void deleteById(Long entityId) {
        final T entity = this.findOne(entityId);
        this.delete(entity);
    }

    /***/
    public EntityManagerFactory getEMF() {
        return entityManager.getEntityManagerFactory();
    }

    /***/
    //public EntityManager getEM() {
    //    return entityManager;
    //}

}
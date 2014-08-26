package net.remisan.base.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.remisan.base.model.PersistableEntity;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class BaseJpaRepository<T extends PersistableEntity>
    extends SimpleJpaRepository<T, Long>
    implements Repository<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    
    protected Class<? extends T> typeParameterClass;
    
    protected String className;
    
    @SuppressWarnings("unchecked")
    public BaseJpaRepository(Class<? extends T> typeParameterClass, EntityManager em) {
        super((Class<T>) typeParameterClass, em);
        this.entityManager = em;
        this.typeParameterClass = typeParameterClass;
        this.className = typeParameterClass.getName();
    }

    @Override
    public T getNewInstance() throws InstantiationException, IllegalAccessException {
        return this.typeParameterClass.newInstance();
    }
    
    @Override
    @Transactional
    public T findOneEager(Long id) {
        return this.findOne(id);
    }
    
    @Override
    @Transactional
    public <R extends T> R merge(R entity) {
        return this.entityManager.merge(entity);
    }

    @Override
    public T getPersistable(T obj) throws InstantiationException, IllegalAccessException {
        return obj;
    }

    @Override
    @Transactional
    public void deleteAll(Iterable<Long> ids) {
        Query query = this.entityManager.createQuery("DELETE " + this.className + " where id IN (:ids)");
        query.setParameter("ids", ids);
        query.executeUpdate();
    }

}

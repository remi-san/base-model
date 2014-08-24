package net.remisan.base.manager;

import java.util.List;

import net.remisan.base.model.PersistableEntity;
import net.remisan.base.repository.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Transactional
public abstract class AbstractManager<T extends PersistableEntity> implements Manager<T> {
    
    @Autowired
    @Qualifier("objectValidator")
    protected Validator validator;

    protected abstract Repository<T> getRepository();

    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// Select /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public T getById(Long id) {
        return this.getRepository().findOne(id);
    }
    
    @Override
    public T getByIdEager(Long id) {
        return this.getRepository().findOneEager(id);
    }

    @Override
    public List<T> getByIds(List<Long> ids) {
        return this.getRepository().findAll(ids);
    }

    @Override
    public T get(Specification<T> specification) {
        return this.getRepository().findOne(specification);
    }
    
    @Override
    public List<T> getAll() {
        return this.getRepository().findAll();
    }

    @Override
    public List<T> getAll(Specification<T> specification) {
        return this.getRepository().findAll(specification);
    }

    @Override
    public Page<T> getAll(Specification<T> specification, Pageable pageable) {
        return this.getRepository().findAll(specification, pageable);
    }

    @Override
    public List<T> getAll(Specification<T> specification, Sort sort) {
        return this.getRepository().findAll(specification, sort);
    }

    @Override
    public long count(Specification<T> spec) {
        return this.getRepository().count(spec);
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// Save //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public T save(T entity) throws BindException {
        
        Errors errors = this.validate(entity);
        if (errors.getErrorCount() > 0) {
            throw (BindException) errors;
        }
        
        return this.getRepository().save(entity);
    }
    
    @Override
    public <S extends T> List<S> save(Iterable<S> entities) throws BindException {
        
        for (S entity : entities) {
            Errors errors = this.validate(entity);
            if (errors.getErrorCount() > 0) {
                throw (BindException) errors;
            }
        }
        
        return this.getRepository().save(entities);
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// Delete /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void delete(T obj) {
        this.getRepository().delete(obj);
    }

    @Override
    public void delete(Long id) {
        this.getRepository().delete(id);
    }

    @Override
    public void deleteAll() {
        this.getRepository().deleteAll();
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// Util //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public T getPersistable(T obj) throws InstantiationException, IllegalAccessException {
        return this.getRepository().getPersistable(obj);
    }
    
    @Override
    public T getNewInstance() throws InstantiationException, IllegalAccessException {
        return this.getRepository().getNewInstance();
    }
    
    @Override
    public Errors validate(T entity) {
        BindException errors = new BindException(entity, entity.getClass().getCanonicalName());
        
        if (this.validator != null && this.validator.supports(entity.getClass())) {
            this.validator.validate(entity, errors);
        }
        
        return errors;
    }
}

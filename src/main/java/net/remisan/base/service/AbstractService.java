package net.remisan.base.service;

import java.util.List;

import net.remisan.base.manager.Manager;
import net.remisan.base.model.PersistableEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

@Transactional
public abstract class AbstractService<T extends PersistableEntity> implements Service<T> {

    protected abstract Manager<T> getManager();

    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// Select /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public T getById(Long id) {
        return this.getManager().getById(id);
    }
    
    @Override
    public T getByIdEager(Long id) {
        return this.getManager().getByIdEager(id);
    }

    @Override
    public T get(Specification<T> specification) {
        return this.getManager().get(specification);
    }

    @Override
    public List<T> getByIds(List<Long> ids) {
        return this.getManager().getByIds(ids);
    }

    @Override
    public List<T> getAll() {
        return this.getManager().getAll();
    }

    @Override
    public List<T> getAll(Specification<T> specification) {
        return this.getManager().getAll(specification);
    }

    @Override
    public Page<T> getAll(Specification<T> specification, Pageable pageable) {
        return this.getManager().getAll(specification, pageable);
    }

    @Override
    public List<T> getAll(Specification<T> specification, Sort sort) {
        return this.getManager().getAll(specification, sort);
    }

    @Override
    public long count(Specification<T> spec) {
        return this.getManager().count(spec);
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// Save //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public T save(T obj) throws BindException {
        return this.getManager().save(obj);
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) throws BindException {
        return this.getManager().save(entities);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// Delete /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void delete(Long id) {
        this.getManager().delete(id);
    }
    
    @Override
    public void delete(T obj) {
        this.getManager().delete(obj);
    }

    @Override
    public void deleteAll() {
        this.getManager().deleteAll();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// Util //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public T getPersistable(T obj) throws InstantiationException, IllegalAccessException {
        return this.getManager().getPersistable(obj);
    }

    @Override
    public T getNewInstance() throws InstantiationException, IllegalAccessException {
        return this.getManager().getNewInstance();
    }

    @Override
    public Errors validate(T entity) {
        return this.getManager().validate(entity);
    }
}

package net.remisan.base.repository.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.remisan.base.model.PersistableEntity;
import net.remisan.base.repository.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindException;

import com.google.common.collect.Lists;

public abstract class DefaultRepositoryMock<T extends PersistableEntity>
    implements Repository<T> {

    private Map<Long, T> objects = new HashMap<Long, T>();
    private Long index = 0L;
    
    public Map<Long, T> getObjects() {
        return this.objects;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////     SAVE     ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public <R extends T> R save(R entity) {
        if (entity.isNew()) {
            entity.setId(++this.index);
        }
        this.objects.put(entity.getId(), entity);
        return entity;
    }
    
    @Override
    public <R extends T> List<R> save(Iterable<R> entities) {
        for (R entity : entities) {
            this.save(entity);
        }
        return Lists.newArrayList(entities);
    }
    
    public <S extends T> S saveWithValidation(S entity) throws BindException {
        return this.save(entity);
    }
    
    public <R extends T> R merge(R entity) {
        return this.save(entity);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////    DELETE    ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    
    public void delete(T entity) {
        this.delete(entity.getId());
    }
    
    
    public void delete(Long id) {
        if (this.objects.containsKey(id)) {
            this.objects.remove(id);
        }
    }

    
    public void delete(Iterable<? extends T> entities) {
        for (T entity : entities) {
            this.delete(entity);
        }
    }

    public void deleteAll(Iterable<Long> ids) {
        for (Long id : ids) {
            this.delete(id);
        }
    }
    
    public void deleteAll() {
        this.objects = new HashMap<Long, T>();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////    SELECT    ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    public T findOne(Long id) {
        if (this.objects.containsKey(id)) {
            return this.objects.get(id);
        }
        return null;
    }

    public T findOneEager(Long id) {
        return this.findOne(id);
    }
    
    public List<T> findAll() {
        return new ArrayList<T>(this.objects.values());
    }
    
    
    public List<T> findAll(Iterable<Long> ids) {
        return this.findAll();
    }
    
    
    public List<T> findAll(Sort sort) {
        return this.findAll();
    }

    
    public Page<T> findAll(Pageable pageable) {
        return new PageImpl<T>((List<T>) (this.findAll()));
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////     UTIL     ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public boolean exists(Long id) {
        return this.objects.containsKey(id);
    }
    
    
    public long count() {
        return this.objects.size();
    }
    
    
    public void flush() {
        
    }

    public <R extends T> R saveAndFlush(R entity) {
        this.save(entity);
        this.flush();
        return entity;
    }

    public void deleteInBatch(Iterable<T> entities) {
        this.delete(entities);
    }

    public void deleteAllInBatch() {
        this.deleteAll();
    }

    public T getOne(Long id) {
        return this.findOne(id);
    }
    
    public T findOne(Specification<T> spec) {
        return this.findAll(spec).get(0);
    }

    public List<T> findAll(Specification<T> spec) {
        return this.findAll();
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return this.findAll(pageable);
    }

    public List<T> findAll(Specification<T> spec, Sort sort) {
        return this.findAll(sort);
    }

    public long count(Specification<T> spec) {
        return this.count();
    }
    
    public T getPersistable(T obj) {
        return obj;
    }
}

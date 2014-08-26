package net.remisan.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.remisan.base.model.PersistableEntity;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

public abstract class HibernateRepository<T extends PersistableEntity> implements Repository<T> {

    /**
     * The entity manager to access the persistence context.
     */
    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<? extends T> typeParameterClass;
    
    protected String className;
    
    public HibernateRepository(Class<? extends T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
        this.className = typeParameterClass.getName();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////     SAVE     ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    @Transactional 
    public <R extends T> R save(R entity) {
        if (entity.isNew()) {
            this.entityManager.persist(entity);
        }
        return this.merge(entity);
    }
    
    @Transactional
    public <R extends T> List<R> save(Iterable<R> entities) {
        for (R entity : entities) {
            this.save(entity);
        }
        return Lists.newArrayList(entities);
    }
    
    @Transactional
    public <R extends T> R merge(R entity) {
        return this.entityManager.merge(entity);
    }
    
    @Transactional
    public <R extends T> R saveAndFlush(R entity) {
        this.save(entity);
        this.flush();
        return entity;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////    DELETE    ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public void delete(T entity) {
        this.delete(entity.getId());
    }
    
    @Transactional
    public void delete(Long id) {
        Query query = this.entityManager.createQuery("DELETE " + this.className + " where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Transactional
    public void delete(Iterable<? extends T> entities) {
        List<Long> ids = new ArrayList<Long>();
        for (T entity : entities) {
            ids.add(entity.getId());
        }
        
        this.deleteAll(ids);
    }
    
    @Transactional
    public void deleteAll(Iterable<Long> ids) {
        Query query = this.entityManager.createQuery("DELETE " + this.className + " where id IN (:ids)");
        query.setParameter("ids", ids);
        query.executeUpdate();
    }

    @Transactional
    public void deleteAll() {
        Query query = this.entityManager.createQuery("DELETE " + this.className);
        query.executeUpdate();
    }
   
    @Transactional
    public void deleteInBatch(Iterable<T> entities) {
        this.delete(entities);
    }

    @Transactional
    public void deleteAllInBatch() {
        this.deleteAll();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////    SELECT    ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    @Transactional
    public T findOne(Long id) {
        return this.entityManager.find(this.typeParameterClass, id);
    }
    
    @Transactional
    public T findOneEager(Long id) {
        T obj = this.findOne(id);
        Hibernate.initialize(obj);
        
        return obj;
    }
    
    @Transactional
    public T getOne(Long id) {
        return this.findOne(id);
    }
    
    @Transactional
    public T findOne(Specification<T> spec) {
        return this.executeQuery(spec);
    }

    @Transactional
    public List<T> findAll() {
        return this.findAll((Sort) null);
    }
    
    @Transactional
    public List<T> findAll(final Iterable<Long> ids) {
        Specification<T> spec = new Specification<T>() {
            
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get("id").in(Lists.newArrayList(ids));
            }
        };
        return this.findAll(spec);
    }
    
    @Transactional
    public List<T> findAll(Sort sort) {
        return this.findAll(null, sort);
    }

    @Transactional
    public Page<T> findAll(Pageable pageable) {
        return this.findAll(null, pageable);
    }
    
    @Transactional
    public List<T> findAll(Specification<T> spec) {
        return this.findAll(spec, (Sort) null);
    }

    @Transactional
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return this.executeQuery(spec, pageable);
    }

    @Transactional
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return this.executeQuery(spec, sort);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////     UTIL     ///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    @Transactional
    public boolean exists(Long id) {
        T e = this.findOne(id);
        return e != null;
    }
    
    @Transactional
    public long count() {
        return this.count(null);
    }
    
    @Transactional
    public long count(Specification<T> spec) {
        return this.executeCountQuery(spec);
    }
    
    @Transactional
    public void flush() {
        this.entityManager.flush();
    }
    
    public T getNewInstance() throws InstantiationException, IllegalAccessException {
        return this.typeParameterClass.newInstance();
    }

    public T getPersistable(T obj) throws InstantiationException, IllegalAccessException {
        return obj;
    }
    
    protected void eager(T obj) {
        Hibernate.initialize(obj);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////     QUERY HELPER     ///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Returns an order list from a Sort parameter
     * 
     * @param sort
     * @param root
     * @return
     */
    protected List<javax.persistence.criteria.Order> getOrderList(Sort sort, Root<? extends T> root) {
        
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        
        List<javax.persistence.criteria.Order> orders = new ArrayList<javax.persistence.criteria.Order>();
        
        for (Order order : sort) {
            javax.persistence.criteria.Order o = null;
            if (order.isAscending()) {
                o = builder.asc(root.get(order.getProperty()));
            } else {
                o = builder.desc(root.get(order.getProperty()));
            }
            orders.add(o);
        }
        
        return orders;
    }
    
    /**
     * Get the where clauses
     * 
     * @param filters
     * @return
     */
    protected Predicate getPredicate(
        Specification<T> filters,
        Root<T> root,
        CriteriaQuery<?> query,
        CriteriaBuilder cb
    ) {
        return filters.toPredicate(root, query, cb);
    }
    
    /**
     * Get select Query
     * 
     * @param filters
     * @param sort
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <R extends T> CriteriaQuery<R> getQuery(Specification<T> filters, Sort sort) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        
        CriteriaQuery<R> query = (CriteriaQuery<R>) builder.createQuery(this.typeParameterClass);
        Root<R> root = (Root<R>) query.from(this.typeParameterClass);
        query.select(root);
        
        if (filters != null) {
            Predicate restrictions = this.getPredicate(filters, (Root<T>) root, query, builder);
            if (restrictions != null) {
                query.where(restrictions);
            }
        }
        
        if (sort != null) {
            List<javax.persistence.criteria.Order> orders = this.getOrderList(sort, root);
            if (orders != null && orders.size() > 0) {
                query.orderBy(orders);
            }
        }
        
        return query;
    }
    
    /**
     * Get count query
     * 
     * @param filters
     * @return
     */
    @SuppressWarnings("unchecked")
    protected CriteriaQuery<Long> getCountQuery(Specification<T> filters) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<? extends T> root = query.from(this.typeParameterClass);
        query.select(builder.count(root));
        
        if (filters != null) {
            Predicate restrictions = this.getPredicate(filters, (Root<T>) root, query, builder);
            if (restrictions != null) {
                query.where(restrictions);
            }
        }
        
        return query;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////     EXECUTE QUERY     //////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Executes a count query
     * 
     * @param filters
     * @return
     */
    protected Long executeCountQuery(Specification<T> filters) {
        CriteriaQuery<Long> countQuery = this.getCountQuery(filters);
        return this.entityManager.createQuery(countQuery).getSingleResult();
    }
    
    /**
     * 
     * @param filters
     * @return
     */
    protected T executeQuery(Specification<T> filters) {
        CriteriaQuery<? extends T> query = this.getQuery(filters, null);
        return this.entityManager.createQuery(query).getSingleResult();
    }
    
    /**
     * Executes a query with filters
     * 
     * @param filters
     * @param sort
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<T> executeQuery(Specification<T> filters, Sort sort) {
        CriteriaQuery<? extends T> query = this.getQuery(filters, sort);
        return (List<T>) this.entityManager.createQuery(query).getResultList();
    }
    
    /**
     * Executes a paginated query
     * 
     * @param filters
     * @param pageable
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Page<T> executeQuery(Specification<T> filters, Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable must be not null!");
        }
        
        Long count = this.count(filters);
        
        CriteriaQuery<? extends T> query = this.getQuery(filters, pageable.getSort());
        TypedQuery<T> typedQuery = (TypedQuery<T>) this.entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
        return new PageImpl<T>(typedQuery.getResultList(), pageable, count);
    }
}

package net.remisan.base.repository.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.remisan.base.model.TestEntity;
import net.remisan.base.model.hibernate.mock.HibernateTest;
import net.remisan.base.repository.TestRepository;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class HibernateRepositoryTest {

    @Autowired
    private TestRepository repository;
    
    @BeforeClass
    public static void init() {
        Configuration configuration = new Configuration();
        
        configuration.addAnnotatedClass(HibernateTest.class);
        
        configuration.setProperty(Environment.USER, "postgres");
        configuration.setProperty(Environment.PASS, "Chanec12");
        configuration.setProperty(Environment.URL, "jdbc:postgresql://localhost:5432/ratemydates");
        configuration.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty(Environment.DRIVER, "org.postgresql.Driver");
        
        SchemaExport se = new SchemaExport(configuration);
        se.create(true, true);
    }
    
    @Test
    public void test() throws InstantiationException, IllegalAccessException {
        
        // Entity
        
        TestEntity entity = this.repository.getNewInstance();
        entity.setName("name");
        entity.setDummy("dummy");
        entity.setFoo("foo");
        entity.setBar("bar");
        
        Assert.assertNull(entity.getId());
        
        entity.setId(null);
        Assert.assertNull(entity.getId());
        Assert.assertEquals("name", entity.getName());
        Assert.assertEquals("dummy", entity.getDummy());
        Assert.assertEquals("foo", entity.getFoo());
        Assert.assertEquals("bar", entity.getBar());
        
        TestEntity persistableEntity = this.repository.getPersistable(entity);
        Assert.assertEquals(entity, persistableEntity);
        
        // Save
        List<TestEntity> persistEntities = new ArrayList<TestEntity>();
        persistEntities.add(persistableEntity);
        List<TestEntity> persistedEntities = this.repository.save(persistEntities);
        Assert.assertNotNull(entity.getId());
        Assert.assertTrue(persistedEntities.contains(entity));
        
        final Long id = entity.getId();
        
        TestEntity persistedEntity = this.repository.saveAndFlush(persistableEntity);
        Assert.assertEquals(entity, persistedEntity);
        
        //Select One
        TestEntity selectedEntity = this.repository.getOne(id);
        Assert.assertEquals(entity, selectedEntity);
        
        Specification<TestEntity> specId = new Specification<TestEntity>() {
            public Predicate toPredicate(Root<TestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("id"), id);
            }
        };
        selectedEntity = this.repository.findOne(specId);
        Assert.assertEquals(entity, selectedEntity);
        
        // Exists
        Assert.assertTrue(this.repository.exists(id));
        Assert.assertFalse(this.repository.exists(-1L));
        Assert.assertEquals(1, this.repository.count());
        
        // Select All
        List<TestEntity> entities = this.repository.findAll();
        Assert.assertEquals(1, entities.size());
        
        List<Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Order("name"));
        orders.add(new Order(Direction.DESC, "foo"));
        Sort sort = new Sort(orders);
        entities = this.repository.findAll(sort);
        Assert.assertEquals(1, entities.size());
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        entities = this.repository.findAll(ids);
        Assert.assertEquals(1, entities.size());
        
        // Pagination
        Specification<TestEntity> specFoo = new Specification<TestEntity>() {
            public Predicate toPredicate(Root<TestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("foo"), "foo");
            }
        };
        
        Pageable pageable = new PageRequest(0, 10, sort);
        Page<TestEntity> pagedEntities = this.repository.findAll(specFoo, pageable);
        Assert.assertEquals(1, pagedEntities.getContent().size());
        Assert.assertEquals(1, pagedEntities.getTotalElements());
        Assert.assertEquals(1, pagedEntities.getTotalPages());
        
        pagedEntities = this.repository.findAll(pageable);
        Assert.assertEquals(1, pagedEntities.getContent().size());
        Assert.assertEquals(1, pagedEntities.getTotalElements());
        Assert.assertEquals(1, pagedEntities.getTotalPages());
        
        // Delete
        List<TestEntity> persist = new ArrayList<TestEntity>();
        persist.add(new HibernateTest("1"));
        persist.add(new HibernateTest("2"));
        persist.add(new HibernateTest("3"));
        persist.add(new HibernateTest("4"));
        this.repository.save(persist);
        
        entities = this.repository.findAll();
        Assert.assertEquals(5, entities.size());
        
        TestEntity toDelete = new HibernateTest("5");
        this.repository.save(toDelete);
        Long toDeleteId = toDelete.getId();
        
        TestEntity toDelete2 = new HibernateTest("6");
        this.repository.save(toDelete2);
        Long toDeleteId2 = toDelete2.getId();
        
        entities = this.repository.findAll();
        Assert.assertEquals(7, entities.size());
        
        this.repository.delete(toDelete);
        Assert.assertFalse(this.repository.exists(toDeleteId));
        
        this.repository.delete(toDeleteId2);
        Assert.assertFalse(this.repository.exists(toDeleteId2));
        
        entities = this.repository.findAll();
        Assert.assertEquals(5, entities.size());
        
        this.repository.deleteInBatch(persist);
        
        entities = this.repository.findAll();
        Assert.assertEquals(1, entities.size());
        Assert.assertTrue(this.repository.exists(id));
        
        this.repository.deleteAllInBatch();
        
        entities = this.repository.findAll();
        Assert.assertEquals(0, entities.size());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testException() {
        this.repository.findAll((Pageable) null);
    }
}

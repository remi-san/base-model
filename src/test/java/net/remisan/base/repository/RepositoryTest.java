package net.remisan.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.remisan.base.model.mock.TestMock;

import org.junit.Assert;
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
public class RepositoryTest {

    @Autowired
    private Repository<TestMock> repository;
    
    @Test
    public void testNotFound() {
        TestMock c = this.repository.findOne(new Long(99));
        Assert.assertNull(c);
    }

    @Test
    public void test() throws Exception {

        // Entity
        
        TestMock entity = this.repository.getNewInstance();
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
        
        TestMock persistableEntity = this.repository.getPersistable(entity);
        Assert.assertEquals(entity, persistableEntity);
        
        // Save
        List<TestMock> persistEntities = new ArrayList<TestMock>();
        persistEntities.add(persistableEntity);
        List<TestMock> persistedEntities = this.repository.save(persistEntities);
        Assert.assertNotNull(entity.getId());
        Assert.assertTrue(persistedEntities.contains(entity));
        
        final Long id = entity.getId();
        
        TestMock persistedEntity = this.repository.saveAndFlush(persistableEntity);
        Assert.assertEquals(entity, persistedEntity);
        
        //Select One
        TestMock selectedEntity = this.repository.getOne(id);
        Assert.assertEquals(entity, selectedEntity);
        
        Specification<TestMock> specId = new Specification<TestMock>() {
            public Predicate toPredicate(Root<TestMock> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        List<TestMock> entities = this.repository.findAll();
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
        Specification<TestMock> specFoo = new Specification<TestMock>() {
            public Predicate toPredicate(Root<TestMock> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("foo"), "foo");
            }
        };
        
        Pageable pageable = new PageRequest(0, 10, sort);
        Page<TestMock> pagedEntities = this.repository.findAll(specFoo, pageable);
        Assert.assertEquals(1, pagedEntities.getContent().size());
        Assert.assertEquals(1, pagedEntities.getTotalElements());
        Assert.assertEquals(1, pagedEntities.getTotalPages());
        
        pagedEntities = this.repository.findAll(pageable);
        Assert.assertEquals(1, pagedEntities.getContent().size());
        Assert.assertEquals(1, pagedEntities.getTotalElements());
        Assert.assertEquals(1, pagedEntities.getTotalPages());
        
        // Delete
        List<TestMock> persist = new ArrayList<TestMock>();
        persist.add(new TestMock("1"));
        persist.add(new TestMock("2"));
        persist.add(new TestMock("3"));
        persist.add(new TestMock("4"));
        this.repository.save(persist);
        
        entities = this.repository.findAll();
        Assert.assertEquals(5, entities.size());
        
        TestMock toDelete = new TestMock("5");
        this.repository.save(toDelete);
        Long toDeleteId = toDelete.getId();
        
        TestMock toDelete2 = new TestMock("6");
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
}

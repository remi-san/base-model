package net.remisan.base.manager;

import java.util.ArrayList;
import java.util.List;

import net.remisan.base.model.mock.TestMock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManagerTest {
    
    @Autowired
    private Manager<TestMock> manager;
    
    @Test
    public void test() throws BindException, InstantiationException, IllegalAccessException {
// Entity
        
        TestMock entity = this.manager.getNewInstance();
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
        
        TestMock persistableEntity = this.manager.getPersistable(entity);
        Assert.assertEquals(entity, persistableEntity);
        
        // Save
        this.manager.save(entity);
        final Long id = entity.getId();
        
        TestMock persistedEntity = this.manager.save(persistableEntity);
        Assert.assertEquals(entity, persistedEntity);
        
        //Select One
        TestMock selectedEntity = this.manager.getById(id);
        Assert.assertEquals(entity, selectedEntity);
        
        // Select All
        List<TestMock> entities = this.manager.getAll();
        Assert.assertEquals(1, entities.size());
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        entities = this.manager.getByIds(ids);
        Assert.assertEquals(1, entities.size());
        
        // Pagination
        
        
        // Delete
        this.manager.save(new TestMock("1"));
        this.manager.save(new TestMock("2"));
        this.manager.save(new TestMock("3"));
        this.manager.save(new TestMock("4"));
        
        entities = this.manager.getAll();
        Assert.assertEquals(5, entities.size());
        
        TestMock toDelete = new TestMock("5");
        this.manager.save(toDelete);
        Long toDeleteId = toDelete.getId();
        
        TestMock toDelete2 = new TestMock("6");
        this.manager.save(toDelete2);
        Long toDeleteId2 = toDelete2.getId();
        
        entities = this.manager.getAll();
        Assert.assertEquals(7, entities.size());
        
        this.manager.delete(toDelete);
        Assert.assertNull(this.manager.getById(toDeleteId));
        
        this.manager.delete(toDeleteId2);
        Assert.assertNull(this.manager.getById(toDeleteId2));
        
        entities = this.manager.getAll();
        Assert.assertEquals(5, entities.size());
        
        this.manager.deleteAll();
        
        entities = this.manager.getAll();
        Assert.assertEquals(0, entities.size());
    }
    
}

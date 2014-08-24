package net.remisan.base.model.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ValidationManagerTest {
    
    @Autowired
    @Qualifier("objectValidator")
    private ValidationManager manager;
    
    @Test
    public void test() {
        Assert.assertTrue(true);
    }
}

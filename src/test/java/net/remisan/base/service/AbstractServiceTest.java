package net.remisan.base.service;

import net.remisan.base.model.mock.TestMock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractServiceTest {

    @Autowired
    private Service<TestMock> service;
    
    @Test
    public void test() {
        Assert.assertTrue(true);
    }
}

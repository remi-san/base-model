package net.remisan.base.service.mock;

import net.remisan.base.manager.Manager;
import net.remisan.base.model.mock.TestMock;
import net.remisan.base.service.AbstractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestService extends AbstractService<TestMock> {

    @Autowired
    private Manager<TestMock> manager;
    
    @Override
    protected Manager<TestMock> getManager() {
        return this.manager;
    }
    
}

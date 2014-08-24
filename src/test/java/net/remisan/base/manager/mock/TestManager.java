package net.remisan.base.manager.mock;

import net.remisan.base.manager.AbstractManager;
import net.remisan.base.model.mock.TestMock;
import net.remisan.base.repository.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestManager extends AbstractManager<TestMock>  {

    @Autowired
    private Repository<TestMock> repository;
    
    @Override
    protected Repository<TestMock> getRepository() {
        return this.repository;
    }

}

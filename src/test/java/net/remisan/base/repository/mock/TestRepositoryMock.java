package net.remisan.base.repository.mock;

import net.remisan.base.model.mock.TestMock;

import org.springframework.stereotype.Component;

@Component
public class TestRepositoryMock extends DefaultRepositoryMock<TestMock> {

    public TestMock getNewInstance() {
        return new TestMock();
    }
}

package net.remisan.base.repository.hibernate.mock;

import net.remisan.base.model.TestEntity;
import net.remisan.base.model.hibernate.mock.HibernateTest;
import net.remisan.base.repository.HibernateRepository;
import net.remisan.base.repository.TestRepository;

import org.springframework.stereotype.Repository;

@Repository
public class TestHibernateRepositoryMock
    extends HibernateRepository<TestEntity>
    implements TestRepository {

    public TestHibernateRepositoryMock() {
        super(HibernateTest.class);
    }

    public TestEntity getNewInstance() {
        return new HibernateTest();
    }

    public TestEntity getPersistable(TestEntity obj) {
        return obj;
    }
}

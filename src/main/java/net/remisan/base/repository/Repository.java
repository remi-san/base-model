package net.remisan.base.repository;

import net.remisan.base.model.PersistableEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Repository<T extends PersistableEntity>
    extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    T findOneEager(Long id);
    
    T getNewInstance() throws InstantiationException, IllegalAccessException;
    
    <R extends T> R merge(R obj);

    void flush();
    
    T getPersistable(T obj) throws InstantiationException, IllegalAccessException;
    
    void deleteAll(Iterable<Long> entities);

}

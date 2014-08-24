package net.remisan.base.model;

import org.springframework.data.domain.Persistable;

public interface PersistableEntity extends Persistable<Long> {
    
    void setId(Long id);
    
}

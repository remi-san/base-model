package net.remisan.base.registry;

import java.util.Map;

import javassist.NotFoundException;

public class BaseRegistry<T> implements Registry<T> {

    private Map<String, T> objects;
    
    public BaseRegistry(Map<String, T> objects) {
        this.objects = objects;
    }
    
    @Override
    public T get(String alias) throws NotFoundException {
        if (!this.objects.containsKey(alias)) {
            throw new NotFoundException(alias + " not found in registry");
        }
        
        return this.objects.get(alias);
    }

}

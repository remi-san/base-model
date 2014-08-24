package net.remisan.base.registry;

import javassist.NotFoundException;


public interface Registry<T> {

    T get(String alias) throws NotFoundException;

}

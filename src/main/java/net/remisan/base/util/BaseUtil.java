package net.remisan.base.util;

import net.remisan.base.model.PersistableEntity;

import org.springframework.stereotype.Component;

@Component
public class BaseUtil implements ModelUtil {

    @Override
    public void preSave(PersistableEntity object) {
    }

    @Override
    public void postSave(PersistableEntity object, boolean newObject) {
    }
}

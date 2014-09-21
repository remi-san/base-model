package net.remisan.base.util;

import net.remisan.base.model.PersistableEntity;

public interface ModelUtil {

    void preSave(PersistableEntity object);

    void postSave(PersistableEntity object, boolean newObject);

}

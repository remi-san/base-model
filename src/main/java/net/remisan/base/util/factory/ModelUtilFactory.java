package net.remisan.base.util.factory;

import java.util.Map;

import net.remisan.base.util.ModelUtil;

public class ModelUtilFactory {

    private Map<String, ModelUtil> utilList;

    public ModelUtil getModelUtil(Object obj) {

        String key = "default";
        Class<? extends Object> objectClass = obj.getClass();
        for (String className : this.utilList.keySet()) {
        	try {
				Class<?> c = Class.forName(className);
				if (c.isAssignableFrom(objectClass)) {
					key = c.getCanonicalName();
					break;
				}
			} catch (ClassNotFoundException e) {}
        }

        return this.utilList.get(key);
    }

    public void setUtilList(Map<String, ModelUtil> utilList) {
        this.utilList = utilList;
    }
}

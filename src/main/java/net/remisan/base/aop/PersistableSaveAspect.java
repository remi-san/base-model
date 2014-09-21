package net.remisan.base.aop;

import net.remisan.base.model.PersistableEntity;
import net.remisan.base.util.ModelUtil;
import net.remisan.base.util.factory.ModelUtilFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class PersistableSaveAspect {

    @Autowired
    private ModelUtilFactory modelUtilFactory;

    @Around("execution(* *..service.*Service*.save(..))")
    public Object interceptSave(ProceedingJoinPoint joinPoint) throws Throwable {
        
    	Object obj = joinPoint.getArgs()[0];
        boolean newObject = false;
        ModelUtil modelUtil = this.modelUtilFactory.getModelUtil(obj);;
        
        if (((PersistableEntity) obj).isNew()) {
            newObject = true;
        }

        if (modelUtil != null) {
            modelUtil.preSave((PersistableEntity) obj);
        }

        obj = joinPoint.proceed();

        if (modelUtil != null) {
            modelUtil.postSave((PersistableEntity) obj, newObject);
        }

        return obj;
    }
}

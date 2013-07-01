package com.rai.services;

import android.util.Log;
import com.rai.context.ContextApp;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 23/06/13
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class ServiceController {

    private static ServiceController instance;
    private static final String TAG = "ServiceController";
    private Map<String,Class<? extends AbstractService>> services;

    private ServiceController(){
        services = new WeakHashMap<String, Class<? extends AbstractService>>();
        Reflections reflections = new Reflections("com.rai.services");
        Set<Class<? extends AbstractService>> set = reflections.getSubTypesOf(AbstractService.class);
        try{
            for(Class<? extends AbstractService> c : set){
                AbstractService service = c.newInstance();
                services.put(service.getName(),service.getClassName());
            }
            Log.i(TAG,"Services "+services.keySet());
        }
        catch (Exception e){
            Log.e(TAG,"Class "+services.keySet(),e);
        }
    }

    protected void addService(String serviceName,Class<? extends AbstractService> serviceClass){
        this.services.put(serviceName,serviceClass);
    }

    public static ServiceController instance(){
        if(instance == null){
            instance = new ServiceController();
        }
        return instance;
    }

    public Map<String,Class<? extends AbstractService>> getServices(){
        return this.services;
    }

    public AbstractService factory(String name){
        try{
            Log.i(TAG,"Name "+name);
            Class<?> c = services.get(name);
            Log.i(TAG,"Class Name "+c);
            Constructor<?> constructor = c.getConstructor(ServiceController.class);
            Object o = constructor.newInstance(instance());
            return (AbstractService)o;

        }
        catch (Exception e){
            Log.e(TAG,"Factory "+services.keySet(),e);
        }
        return null;
    }

}

package com.rai.services;

import android.os.AsyncTask;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 23/06/13
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractService extends AsyncTask<String,Void,Map<String,String>> {

//    public O execute(String... params);

//    public static String getName(){return "";}

    protected ServiceController serviceController;
//    public AbstractService(ServiceController serviceController){
//        this.serviceController = serviceController;
//    }

    public AbstractService(){}

    protected abstract String getName();
    protected abstract Class<? extends  AbstractService> getClassName();

}

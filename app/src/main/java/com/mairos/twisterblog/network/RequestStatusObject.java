package com.mairos.twisterblog.network;

import com.google.common.annotations.VisibleForTesting;

/**
 * Created by Mike on 01.02.2015.
 */
public class RequestStatusObject {

    public interface RequestStatusChange {
        public void onChange();
    }

    public static final String BEFORE_START = "before_start";
    public static final String IN_PROCESS = "in_process";
    public static final String FIHISHED = "finished";

    private static final RequestStatusObject sRequestStatusObject = new RequestStatusObject();

    private String state = BEFORE_START;
    private RequestStatusChange resourceCallback;

    private static RequestStatusObject mInstance = new RequestStatusObject();

    private RequestStatusObject(){}

    @VisibleForTesting
    public static synchronized RequestStatusObject getInstance(){
        return mInstance;
    }

    public void setResourceCallback(RequestStatusChange resourceCallback){
        this.resourceCallback = resourceCallback;
    }

    public String getState(){
        return state;
    }

    public void setStarted(){
        state = IN_PROCESS;
        if (resourceCallback != null) resourceCallback.onChange();
    }

    public void setFinished(){
        state = FIHISHED;
        if (resourceCallback != null) resourceCallback.onChange();
    }
}

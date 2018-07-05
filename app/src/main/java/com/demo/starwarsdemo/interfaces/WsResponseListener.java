package com.demo.starwarsdemo.interfaces;

public interface WsResponseListener {
    /**
     * @param serviceType
     * @param data
     *
     * @param error
     */

    abstract void onDelieverResponse(String serviceType, String data,
                                     Exception error);

}

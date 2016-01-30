/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.thor.communication.core.delegation.rmi;

import com.mob.thor.communication.core.delegation.AbstractCommunicationEndpoint;

/**
 * 基于rmi的endpoint的实现，包装了一个rmi remote对象
 * 
 * @author zxc Dec 24, 2015 4:41:14 PM
 */
public class RmiCommunicationEndpoint extends AbstractCommunicationEndpoint {

    private String             host;
    private int                port                 = 1099;
    // private RmiServiceExporter export;
    private boolean            alwaysCreateRegistry = false;

    public RmiCommunicationEndpoint() {
    }

    public RmiCommunicationEndpoint(int port) {
        this.port = port;
        initial();
    }

    public void initial() {
        // export = new RmiServiceExporter();
        // export.setServiceName("endpoint");
        // export.setService(this);// 暴露自己
        // export.setServiceInterface(CommunicationEndpoint.class);
        // export.setRegistryHost(host);
        // export.setRegistryPort(port);
        // export.setAlwaysCreateRegistry(alwaysCreateRegistry);// 强制创建一个
        //
        // try {
        // export.afterPropertiesSet();
        // } catch (RemoteException e) {
        // throw new CommunicationException("Rmi_Create_Error", e);
        // }

    }

    public void destory() {
        // try {
            // export.destroy();
        // } catch (RemoteException e) {
        // throw new CommunicationException("Rmi_Destory_Error", e);
        // }
    }

    public void setAlwaysCreateRegistry(boolean alwaysCreateRegistry) {
        this.alwaysCreateRegistry = alwaysCreateRegistry;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }
}

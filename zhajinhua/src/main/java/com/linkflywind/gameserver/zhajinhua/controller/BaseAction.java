package com.linkflywind.gameserver.zhajinhua.controller;

import com.linkflywind.gameserver.zhajinhua.redisModel.ConnectorData;

public abstract class BaseAction {
    private int protocol;
    BaseAction(int protocol){
        this.protocol = protocol;
    }
    public abstract void proccess(ConnectorData connectorData);

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }


}

package com.linkflywind.gameserver.zhajinhua;

import com.linkflywind.gameserver.zhajinhua.controller.BaseAction;
import com.linkflywind.gameserver.zhajinhua.controller.ListRoomAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DispatcherAction {
    @Autowired
    private ListRoomAction listRoomAction;

    public BaseAction createAction(int protocol) {

        if(listRoomAction.getProtocol() == protocol){
            return listRoomAction;
        }
        return null;
    }
}

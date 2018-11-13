package com.linkflywind.gameserver.core.action;

import com.linkflywind.gameserver.core.annotation.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;


@Component
public class DispatcherAction {

    final
    ApplicationContext context;
    private Map<Integer, BaseAction> actionCahce;

    @Autowired
    public DispatcherAction(ApplicationContext context) {
        this.context = context;
    }

    public Optional<BaseAction> createAction(int protocol) {
        if (actionCahce != null)
            return Optional.of(actionCahce.get(protocol));
        return Optional.empty();
    }

    @PostConstruct
    public void init() {
        for (String bean : context.getBeanNamesForAnnotation(Protocol.class)) {
            Object o = context.getBean(bean);
            Protocol protocol = (Protocol) o.getClass().getAnnotations()[0];
            actionCahce.put(protocol.value(), (BaseAction) o);
        }
    }
}

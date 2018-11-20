package com.linkflywind.gameserver.core.annotation;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface RoomActionMapper {
    Class value();
}

package com.linkflywind.gameserver.core.room.message;


import com.linkflywind.gameserver.core.room.message.baseMessage.UnhandledMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbandedMessage extends UnhandledMessage {
    private String roomId;
    private String name;
}

package com.linkflywind.gameserver.yingsanzhangserver.room;

import com.linkflywind.gameserver.cardlib.poker.YingSanZhang.YingSanZhangPoker;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomContext;
import com.linkflywind.gameserver.yingsanzhangserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.yingsanzhangserver.player.YingSanZhangPlayerState;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.response.A1005Response;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.response.A1006Response;
import org.springframework.data.redis.core.RedisTemplate;


public class YingSanZhangRoomContext extends RoomContext {

    public double deskChip;
    private YingSanZhangPoker poker = new YingSanZhangPoker();

    YingSanZhangRoomContext(
            String roomNumber,
            int playerUpLimit,
            int playerLowerlimit,
            RedisTemplate redisTemplate,
            Player master,
            String serverName,
            String connectorName,
            YingSanZhangRoomActorManager yingSanZhangRoomActor) {
        super(roomNumber, playerUpLimit, playerLowerlimit, redisTemplate, master, serverName, connectorName,yingSanZhangRoomActor);
        this.deskChip = 0;

    }


    public void beginGame() {
        for (Object player : this.playerList) {
            ((YingSanZhangPlayer) player).setYingSanZhang(poker.getPocker());
        }
        YingSanZhangPlayer currentPlayer = ((YingSanZhangPlayer) this.playerList.element());
        currentPlayer.setOp(true);
        this.playerList.toArray(new YingSanZhangPlayer[0]);
        sendAll(new A1005Response(this.playerList.toArray(new YingSanZhangPlayer[0]), currentPlayer), 1005);
        this.currentInningsNUmber++;
    }

    public void clearRoom() {
        roomManager.clearRoom(this.roomNumber);
    }

    public void next() {
        long count = this.playerList.stream().filter(p -> ((YingSanZhangPlayer) p).getState() == YingSanZhangPlayerState.none).count();
        if (count == 1) {
            YingSanZhangPlayer yingSanZhangPlayer = (YingSanZhangPlayer) this.playerList.stream().filter(p -> ((YingSanZhangPlayer) p).getState() == YingSanZhangPlayerState.win).findFirst().get();
            yingSanZhangPlayer.chip += this.deskChip;
            sendAll(yingSanZhangPlayer, 1009);
        }

        for (int i = 0; i < this.getPlayerList().size(); i++) {
            YingSanZhangPlayer player = (YingSanZhangPlayer) this.playerList.poll();
            if (player.getState() != YingSanZhangPlayerState.shu &&
                    player.getState() != YingSanZhangPlayerState.qiquan) {
                sendAll(new A1006Response(player), 1006);
                break;
            }
        }

    }
}

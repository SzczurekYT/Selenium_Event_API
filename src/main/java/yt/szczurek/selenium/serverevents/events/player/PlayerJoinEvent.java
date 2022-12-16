package yt.szczurek.selenium.serverevents.events.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class PlayerJoinEvent extends PlayerEvent {
    private MutableText joinMessage;

    public PlayerJoinEvent(ServerPlayerEntity player, MutableText joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
    }

    public MutableText getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(MutableText joinMessage) {
        this.joinMessage = joinMessage;
    }

}

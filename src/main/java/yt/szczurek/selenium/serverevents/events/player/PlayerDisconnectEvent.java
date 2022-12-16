package yt.szczurek.selenium.serverevents.events.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class PlayerDisconnectEvent extends PlayerEvent {

    private MutableText disconnectMessage;

    public PlayerDisconnectEvent(ServerPlayerEntity player, MutableText disconnectMessage) {
        super(player);
        this.disconnectMessage = disconnectMessage;
    }
    public MutableText getDisconnectMessage() {
        return disconnectMessage;
    }

    public void setDisconnectMessage(MutableText disconnectMessage) {
        this.disconnectMessage = disconnectMessage;
    }
}

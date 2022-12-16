package yt.szczurek.selenium.serverevents.events.player;

import yt.szczurek.selenium.eventapi.api.Event;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerEvent extends Event {

    private final ServerPlayerEntity player;

    public PlayerEvent(ServerPlayerEntity player) {
        this.player = player;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }
}

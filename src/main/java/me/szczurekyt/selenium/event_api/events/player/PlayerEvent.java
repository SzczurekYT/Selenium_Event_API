package me.szczurekyt.selenium.event_api.events.player;

import me.szczurekyt.selenium.event_api.api.Event;
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

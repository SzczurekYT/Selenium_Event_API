package yt.szczurek.selenium.serverevents.events.player;

import yt.szczurek.selenium.eventapi.api.Cancellable;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {

    public PlayerGameModeChangeEvent(ServerPlayerEntity player, GameMode newGameMode) {
        super(player);
        this.newGameMode = newGameMode;
    }

    private boolean isCancelled = false;

    private final GameMode newGameMode;

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public GameMode getNewGameMode() {
        return newGameMode;
    }
}

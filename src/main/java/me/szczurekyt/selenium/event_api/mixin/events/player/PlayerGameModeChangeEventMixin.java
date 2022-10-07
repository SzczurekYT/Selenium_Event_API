package me.szczurekyt.selenium.event_api.mixin.events.player;

import me.szczurekyt.selenium.event_api.api.SeleniumEventAPI;
import me.szczurekyt.selenium.event_api.events.player.PlayerGameModeChangeEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class PlayerGameModeChangeEventMixin {

    @Inject(method = "changeGameMode", at = @At("HEAD"), cancellable = true)
    private void inject(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {

        PlayerGameModeChangeEvent event = SeleniumEventAPI.getInstance().callEvent(new PlayerGameModeChangeEvent(((ServerPlayerEntity)(Object)this), gameMode));
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
}

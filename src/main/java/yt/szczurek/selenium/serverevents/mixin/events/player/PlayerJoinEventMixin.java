package yt.szczurek.selenium.serverevents.mixin.events.player;

import yt.szczurek.selenium.eventapi.api.SeleniumEventAPI;
import yt.szczurek.selenium.serverevents.events.player.PlayerJoinEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public class PlayerJoinEventMixin {

    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"))
    private MutableText removeYellow(MutableText instance, Formatting formatting, ClientConnection connection, ServerPlayerEntity player) {
        instance = instance.formatted(formatting);
        PlayerJoinEvent event = SeleniumEventAPI.getInstance().callEvent(new PlayerJoinEvent(player, instance));
        return event.getJoinMessage();
    }
}

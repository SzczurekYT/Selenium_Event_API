package yt.szczurek.selenium.serverevents.mixin.events.player;

import yt.szczurek.selenium.eventapi.api.SeleniumEventAPI;
import yt.szczurek.selenium.serverevents.events.player.PlayerDisconnectEvent;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerDisconnectEventMixin {

    @Shadow public ServerPlayerEntity player;

    @Redirect(method = "onDisconnected", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/TranslatableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"))
    private MutableText inject(TranslatableText instance, Formatting formatting) {
        instance = (TranslatableText) instance.formatted(formatting);
        PlayerDisconnectEvent event = SeleniumEventAPI.getInstance().callEvent(new PlayerDisconnectEvent(player, instance));
        return event.getDisconnectMessage();
    }


}

package yt.szczurek.selenium.serverevents.mixin.invokers;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerEntityInvoker {
    @Invoker("getXpToDrop")
    int invokeGetXpToDrop(PlayerEntity player);
}

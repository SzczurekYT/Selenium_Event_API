package me.szczurekyt.selenium.event_api.mixin.events.player;

import com.mojang.authlib.GameProfile;
import me.szczurekyt.selenium.event_api.api.SeleniumEventAPI;
import me.szczurekyt.selenium.event_api.events.player.PlayerDeathEvent;
import me.szczurekyt.selenium.event_api.mixin.invokers.PlayerEntityInvoker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
abstract class PlayerDeathEventServerPlayerMixin extends PlayerEntity{

    PlayerDeathEvent lastSeleniumDeathEvent;

    public PlayerDeathEventServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @ModifyArg(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"))
    public Packet<?> redirectPacket(Packet<?> packet) {
        if (! (packet instanceof DeathMessageS2CPacket castPacket)) return packet;
        lastSeleniumDeathEvent = handlePacket(castPacket.getMessage());
        return new DeathMessageS2CPacket(castPacket.getEntityId(), castPacket.getKillerId(), lastSeleniumDeathEvent.getDeathMessage());
    }

    @ModifyArg(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V"))
    public Packet<?> redirectListenerPacket(Packet<?> packet) {
        if (! (packet instanceof DeathMessageS2CPacket castPacket)) return packet;
        lastSeleniumDeathEvent = handlePacket(castPacket.getMessage());
        return new DeathMessageS2CPacket(castPacket.getEntityId(), castPacket.getKillerId(), lastSeleniumDeathEvent.getDeathMessage());
    }

    private PlayerDeathEvent handlePacket(Text text) {
        ServerPlayerEntity self = (ServerPlayerEntity)(Object)this;
        List<ItemStack> drops = new ArrayList<>();
        if (!self.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            PlayerInventory inventory = self.getInventory();
            drops.addAll(inventory.main);
            drops.addAll(inventory.armor);
            drops.addAll(inventory.offHand);
        }
        PlayerDeathEvent event = new PlayerDeathEvent(self, drops, text, ((PlayerEntityInvoker) self).getXpToDrop(this));
        SeleniumEventAPI.getInstance().callEvent(event);
        return event;
    }

}

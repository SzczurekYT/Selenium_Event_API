package me.szczurekyt.selenium.event_api.mixin.events.player;

import com.mojang.authlib.GameProfile;
import me.szczurekyt.selenium.event_api.api.SeleniumEventAPI;
import me.szczurekyt.selenium.event_api.events.player.PlayerDeathEvent;
import me.szczurekyt.selenium.event_api.mixin.invokers.PlayerEntityInvoker;
import me.szczurekyt.selenium.event_api.mixinhelpers.PlayerDeathEventHolder;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ServerPlayerEntity.class)
abstract class PlayerDeathEventServerPlayerMixin extends PlayerEntity implements PlayerDeathEventHolder {

    @Shadow @Final private ServerRecipeBook recipeBook;
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
            // Filter ou items with vanishing curse
            drops = drops.stream().filter(itemStack -> !(!itemStack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemStack))).collect(Collectors.toList());
        }
        PlayerDeathEvent event = new PlayerDeathEvent(self, drops, text, ((PlayerEntityInvoker) self).invokeGetXpToDrop(this));
        SeleniumEventAPI.getInstance().callEvent(event);
        return event;
    }

    @ModifyArg(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToTeam(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/text/Text;)V"), index = 1)
    private Text modifyTeamDeathMessage(Text message) {
        return lastSeleniumDeathEvent.getDeathMessage();
    }

    @ModifyArg(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToOtherTeams(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/text/Text;)V"), index = 1)
    private Text modifyOtherTeamDeathMessage(Text message) {
        return lastSeleniumDeathEvent.getDeathMessage();
    }

    @ModifyArg(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"), index = 0)
    private Text modifyDeathMessage(Text message) {
        return lastSeleniumDeathEvent.getDeathMessage();
    }

    @Override
    public PlayerDeathEvent getDeathEvent() {
        return lastSeleniumDeathEvent;
    }
}

@Mixin(LivingEntity.class)
abstract class PlayerDeathEventLivingEntityMixin extends Entity {


    @Shadow protected abstract int getXpToDrop(PlayerEntity player);

    public PlayerDeathEventLivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "dropXp", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getXpToDrop(Lnet/minecraft/entity/player/PlayerEntity;)I"))
    private int injectExp(LivingEntity instance, PlayerEntity player) {
        if (instance instanceof PlayerDeathEventHolder) {
            return ((PlayerDeathEventHolder) instance).getDeathEvent().getDroppedExp();
        }
        return getXpToDrop(player);
    }
}

@Mixin(PlayerEntity.class)
abstract class PlayerDeathEventPlayerEntityMixin extends LivingEntity {


    protected PlayerDeathEventPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void injectDrops(PlayerInventory instance) {
        if (this instanceof PlayerDeathEventHolder) {
            List<ItemStack> drops = ((PlayerDeathEventHolder) this).getDeathEvent().getDrops();
            for (ItemStack itemStack: drops) {
                (((PlayerEntity)(Object)this)).dropItem(itemStack, true, false);
            }
            return;
        }
        instance.dropAll();
    }
}

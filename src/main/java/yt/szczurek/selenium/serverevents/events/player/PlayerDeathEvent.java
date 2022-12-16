package yt.szczurek.selenium.serverevents.events.player;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

public class PlayerDeathEvent extends PlayerEvent {

    private List<ItemStack> drops;
    private Text deathMessage;
    private int droppedExp;

    public PlayerDeathEvent(ServerPlayerEntity player, List<ItemStack> drops, Text deathMessage, int droppedExp) {
        super(player);
        this.drops = drops;
        this.deathMessage = deathMessage;
        this.droppedExp = droppedExp;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }

    public Text getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(MutableText deathMessage) {
        this.deathMessage = deathMessage;
    }

    public int getDroppedExp() {
        return droppedExp;
    }

    public void setDroppedExp(int droppedExp) {
        this.droppedExp = droppedExp;
    }
}
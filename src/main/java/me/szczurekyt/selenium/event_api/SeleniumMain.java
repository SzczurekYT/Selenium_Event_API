package me.szczurekyt.selenium.event_api;

import me.szczurekyt.selenium.event_api.api.EventListener;
import me.szczurekyt.selenium.event_api.api.SeleniumEventAPI;
import me.szczurekyt.selenium.event_api.events.player.PlayerDeathEvent;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumMain implements ModInitializer {

    public static final String MOD_ID = "selenium_event_api";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        // Initialize SeleniumEventAPI singleton.
        new SeleniumEventAPI();
        SeleniumEventAPI.getInstance().registerEventListener(this);
    }

    @EventListener
    public void testDeath(PlayerDeathEvent event) {
        event.getDrops().add(new ItemStack(Items.TNT));
    }

}

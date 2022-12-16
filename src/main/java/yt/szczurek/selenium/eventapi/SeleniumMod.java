package yt.szczurek.selenium.eventapi;

import yt.szczurek.selenium.eventapi.api.EventListener;
import yt.szczurek.selenium.eventapi.api.SeleniumEventAPI;
import yt.szczurek.selenium.serverevents.events.player.PlayerDeathEvent;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumMod implements ModInitializer {

    public static final String MOD_ID = "selenium_event_api";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        // Initialize SeleniumEventAPI singleton.
        new SeleniumEventAPI();
        SeleniumEventAPI.getInstance().registerEventListeners(this);
    }

    @EventListener
    public void testDeath(PlayerDeathEvent event) {
        event.getDrops().add(new ItemStack(Items.TNT));
    }

}

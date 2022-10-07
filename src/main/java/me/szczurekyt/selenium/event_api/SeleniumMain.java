package me.szczurekyt.selenium.event_api;

import me.szczurekyt.selenium.event_api.api.EventListener;
import me.szczurekyt.selenium.event_api.api.SeleniumEventAPI;
import me.szczurekyt.selenium.event_api.events.player.PlayerDisconnectEvent;
import me.szczurekyt.selenium.event_api.events.player.PlayerGameModeChangeEvent;
import net.fabricmc.api.ModInitializer;
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
    public void cancelGameMode(PlayerDisconnectEvent event) {
        event.getPlayer().kill();
    }

}

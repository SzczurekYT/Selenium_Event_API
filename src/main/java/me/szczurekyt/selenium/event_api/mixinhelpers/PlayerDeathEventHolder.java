package me.szczurekyt.selenium.event_api.mixinhelpers;

import me.szczurekyt.selenium.event_api.events.player.PlayerDeathEvent;

public interface PlayerDeathEventHolder {

    PlayerDeathEvent getDeathEvent();
}

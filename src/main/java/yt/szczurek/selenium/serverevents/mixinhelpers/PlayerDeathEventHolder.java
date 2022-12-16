package yt.szczurek.selenium.serverevents.mixinhelpers;

import yt.szczurek.selenium.serverevents.events.player.PlayerDeathEvent;

public interface PlayerDeathEventHolder {

    PlayerDeathEvent getDeathEvent();
}

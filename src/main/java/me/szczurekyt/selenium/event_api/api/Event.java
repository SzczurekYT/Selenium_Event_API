package me.szczurekyt.selenium.event_api.api;

public abstract class Event {

    public ListenersHolder getHolder() {
        return SeleniumEventAPI.getHolderFor(this.getClass());
    }

}

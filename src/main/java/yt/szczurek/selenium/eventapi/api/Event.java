package yt.szczurek.selenium.eventapi.api;

public abstract class Event {

    public ListenersHolder getHolder() {
        return SeleniumEventAPI.getHolderFor(this.getClass());
    }

}

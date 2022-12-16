package yt.szczurek.selenium.eventapi.api;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean isCancelled);
}

package me.szczurekyt.selenium.event_api.api;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean isCancelled);
}

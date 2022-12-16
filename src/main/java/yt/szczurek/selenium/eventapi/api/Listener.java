package yt.szczurek.selenium.eventapi.api;

/**
 * @param callback - Callback that will be executed when the event happens.
 * @param priority - The priority of the listener.
 * @param ignoreCancelled - Should the listener be ommited when the event is cancelled?
 */
public record Listener(ListenerCallback callback, EventPriority priority, boolean ignoreCancelled) {
}

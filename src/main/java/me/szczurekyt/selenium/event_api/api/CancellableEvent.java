package me.szczurekyt.selenium.event_api.api;

public abstract class CancellableEvent implements Cancellable{

    private boolean isCancelled = false;

    /**
     * @return - true is the event is cancelled.
     */
    public boolean isCancelled() {return isCancelled;}

    /**
     * Sets the cancellation state of the event.
     * Cancelling the event prevents it from being fired on a server.
     * @param isCancelled - should the event be cancelled.
     */
    public void setCancelled(boolean isCancelled) {this.isCancelled = isCancelled;}

}

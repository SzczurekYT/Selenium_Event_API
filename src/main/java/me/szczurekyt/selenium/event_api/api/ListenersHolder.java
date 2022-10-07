package me.szczurekyt.selenium.event_api.api;

import me.szczurekyt.selenium.event_api.SeleniumMain;

import java.util.*;

public class ListenersHolder {
    private final EnumMap<EventPriority, Set<Listener>> listeners = new EnumMap<>(EventPriority.class);

    public ListenersHolder() {
        for (EventPriority priority : EventPriority.values()) {
            listeners.put(priority, new LinkedHashSet<>());
        }
    }

    public void registerListener(Listener listener) {
        listeners.get(listener.priority()).add(listener);
    }

    public Set<Listener> getOrderedListeners() {
        Set<Listener> list = new LinkedHashSet<>();
        list.addAll(listeners.get(EventPriority.HIGH));
        list.addAll(listeners.get(EventPriority.NORMAl));
        list.addAll(listeners.get(EventPriority.LOW));
        return list;
    }
}

package me.szczurekyt.selenium.event_api.api;

import me.szczurekyt.selenium.event_api.SeleniumMain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SeleniumEventAPI {

    private static final Map<Class<? extends Event>, ListenersHolder> holderMap = new HashMap<>();

    public SeleniumEventAPI() {
        if (instance != null) {
            throw new RuntimeException("SeleniumEventAPI can be constructed only once, please access it with SeleniumEventAPI.getInstance()");
        }
        instance = this;
    }

    private static SeleniumEventAPI instance;

    public static SeleniumEventAPI getInstance() {
        if (instance == null) {
            return new SeleniumEventAPI();
        }
        return instance;
    }

    public static ListenersHolder getHolderFor(Class<? extends Event> clazz) {
        if (holderMap.containsKey(clazz)) {
            return holderMap.get(clazz);
        }
        ListenersHolder holder = new ListenersHolder();
        holderMap.put(clazz, holder);
        return holder;
    }

    public <E extends Event> E callEvent(E event) {
        // Event can be canceled
        if (event instanceof Cancellable) {
            for (Listener listener: getHolderFor(event.getClass()).getOrderedListeners()) {
                if (listener.ignoreCancelled() && ((Cancellable) event).isCancelled()) continue;
                listener.callback().run(event);
            }
            return event;
        }
        // Event can't be canceled, just call all the listeners.
        for (Listener listener : getHolderFor(event.getClass()).getOrderedListeners()) {
            listener.callback().run(event);
        }
        return event;
    }

    public void registerEventListener(Object listener) {
        // Get methods of the object.
        Method[] methods = listener.getClass().getDeclaredMethods();

        for (Method listenerMethod: methods) {
            // Check if method has @EventListener annotation.
            EventListener annotation = listenerMethod.getAnnotation(EventListener.class);
            if (annotation == null) continue;
            // Do not register some special methods, that Java can generate. (I don't know what I'm talking about, this is based on Spigot's implementation)
            if (listenerMethod.isBridge() || listenerMethod.isSynthetic()) {
                continue;
            }
            // Make sure that listener has only one arg.
            if (listenerMethod.getParameterTypes().length != 1) {
                SeleniumMain.LOGGER.warn("Attempted to register an event that has more or less then one arg. Method " + listenerMethod.getName() + " in class " + listenerMethod.getDeclaringClass().getSimpleName());
            }
            // Make sure that one arg is an event.
            final Class<?> clazz;
            if (! Event.class.isAssignableFrom(clazz = listenerMethod.getParameterTypes()[0])) {
                SeleniumMain.LOGGER.warn(clazz.getSimpleName() + " is not an event!");
            }
            // Make sure method is accessible.
            listenerMethod.setAccessible(true);

            ListenersHolder holder = getHolderFor(listenerMethod.getParameters()[0].getType().asSubclass(Event.class));
            holder.registerListener(new Listener(new ListenerCallback(listener, listenerMethod), annotation.priority(), annotation.ignoreCancelled()));

        }
    }
}

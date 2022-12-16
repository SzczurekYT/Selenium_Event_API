package yt.szczurek.selenium.eventapi.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ListenerCallback {

    Object listener;
    Method method;

    /**
     * @param listener The object that contains the method passed in this constructor.
     * @param method Method to be executed when the event happens.
     */
    public ListenerCallback(Object listener, Method method) {
        this.listener = listener;
        this.method = method;
    }

    public void run(Event event) {
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

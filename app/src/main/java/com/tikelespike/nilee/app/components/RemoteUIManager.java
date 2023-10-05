package com.tikelespike.nilee.app.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

import java.util.Timer;
import java.util.TimerTask;

public class RemoteUIManager extends Div {

    private static final int DEFAULT_DURATION = 5000;
    private static final Notification.Position DEFAULT_POSITION = Notification.Position.BOTTOM_START;

    private UI ui;

    public RemoteUIManager() {
        setUI(UI.getCurrent());
        System.out.println("Initialized with UI: " + ui.toString());
    }

    /**
     * Sets the UI on which to show the notifications. By default, the UI that was active when the roll animator was
     * created is used.
     *
     * @param ui the ui on which to show the notifications
     */
    public void setUI(UI ui) {
        this.ui = ui;
    }

    /**
     * Opens a notification on the remote UI.
     *
     * @param notification the notification to show
     */
    public void open(Notification notification) {
        ui.access(notification::open);
    }

    /**
     * Opens a notification on the remote UI after a delay.
     *
     * @param notification the notification to show
     * @param delayMs      the delay in milliseconds before showing the notification
     */
    public void open(Notification notification, int delayMs) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                open(notification);
            }
        };
        timer.schedule(timerTask, delayMs);
    }

    /**
     * Convenience method analogous to {@link Notification#show(String)}, except not
     * shown on the current UI but on the stored one.
     *
     * @param text the text to show in the notification
     */
    public void show(String text) {
        show(text, DEFAULT_DURATION, DEFAULT_POSITION);
    }

    /**
     * Convenience method analogous to {@link Notification#show(String, int, Notification.Position)}, except not
     * shown on the current UI but on the stored one.
     *
     * @param text     the text to show in the notification
     * @param duration the duration in milliseconds to show the notification
     * @param position the position of the notification
     */
    public void show(String text, int duration, Notification.Position position) {
        Notification notification = new Notification(text, duration, position);
        open(notification);
    }

    public void execute(Runnable runnable) {
        ui.access(runnable::run);
    }
}

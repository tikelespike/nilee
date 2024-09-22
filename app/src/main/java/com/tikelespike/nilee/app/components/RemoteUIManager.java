package com.tikelespike.nilee.app.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages access to a UI from a different thread. This class is useful when you need to show notifications or other UI
 * elements from a background thread or from a different UI than the one that is currently active. For example, you can
 * use this class to show notifications on another users screen initiated by a click on a button on your screen.
 * <p>
 * This class supports convenience methods for showing notifications on the remote UI, since this is the most common use
 * case. If you need to show other UI elements, you can use the {@link #execute(Runnable)} method to run code on the
 * remote UI.
 * <p>
 * In a way, this class is a simple convenience wrapper around {@link UI#access(com.vaadin.flow.server.Command)} and
 * {@link Notification}.
 */
public class RemoteUIManager extends Div {

    private static final int DEFAULT_DURATION = 5000;
    private static final Notification.Position DEFAULT_POSITION = Notification.Position.BOTTOM_START;

    private UI ui;

    /**
     * Creates a new remote UI manager managing the current UI.
     */
    public RemoteUIManager() {
        setUi(UI.getCurrent());
    }

    /**
     * Sets the remote UI managed by this object. This defaults to the UI that was active when this object was created.
     * Notifications and other UI elements will be shown on this UI.
     *
     * @param ui the ui on which to show the notifications
     */
    public void setUi(UI ui) {
        this.ui = ui;
    }

    /**
     * Opens a notification on the remote UI.
     *
     * @param notification the notification to show
     */
    public void open(Notification notification) {
        open(notification, 0);
    }

    /**
     * Opens a notification on the remote UI after a delay.
     *
     * @param notification the notification to show
     * @param delayMs the delay in milliseconds before showing the notification
     */
    public void open(Notification notification, int delayMs) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ui.access(notification::open);
            }
        };
        timer.schedule(timerTask, delayMs);
    }

    /**
     * Convenience method analogous to {@link Notification#show(String)}, except not shown on the current UI but on the
     * stored one.
     *
     * @param text the text to show in the notification
     */
    public void show(String text) {
        show(text, DEFAULT_DURATION, DEFAULT_POSITION);
    }

    /**
     * Convenience method analogous to {@link Notification#show(String, int, Notification.Position)}, except not shown
     * on the current UI but on the stored one.
     *
     * @param text the text to show in the notification
     * @param duration the duration in milliseconds to show the notification
     * @param position the position of the notification
     */
    public void show(String text, int duration, Notification.Position position) {
        Notification notification = new Notification(text, duration, position);
        open(notification);
    }

    /**
     * Executes the given runnable on the remote UI. This is useful when you need to show other UI elements than
     * notifications.
     *
     * @param runnable the runnable to execute on the remote UI
     */
    public void execute(Runnable runnable) {
        ui.access(runnable::run);
    }
}

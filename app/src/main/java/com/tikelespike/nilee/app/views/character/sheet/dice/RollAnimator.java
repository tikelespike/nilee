package com.tikelespike.nilee.app.views.character.sheet.dice;

import com.tikelespike.nilee.core.dice.DiceConstant;
import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.game.RollBus;
import com.tikelespike.nilee.core.game.RollEvent;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.tikelespike.nilee.core.util.Pair;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.validation.constraints.NotNull;

import java.util.*;

/**
 * Manages the rolling of dice and the display of the results as notifications.
 */
public class RollAnimator extends Div implements EventListener<RollEvent> {

    /**
     * How long the instant response notification announcing that a roll is being made is shown in milliseconds
     */
    private static final int ROLL_IN_PROGRESS_DURATION_MS = 700;
    /**
     * How long after initiating the roll the result is shown in milliseconds
     */
    private static final int ROLL_RESULT_DELAY_MS = 1200;
    /**
     * How long the result notification is shown in milliseconds
     */
    private static final int ROLL_RESULT_DURATION_MS = 4000;

    private final List<Pair<RollResult, VerticalLayout>> openNotificationLayouts = new ArrayList<>();
    private final TranslationProvider translationProvider;
    private UI ui;
    private Registration lastRollBusRegistration;

    /**
     * Creates a new roll manager.
     *
     * @param translationProvider translation provider used to translate rolling notifications
     */
    public RollAnimator(@NotNull TranslationProvider translationProvider) {
        Objects.requireNonNull(translationProvider);
        this.ui = UI.getCurrent();
        this.translationProvider = translationProvider;
    }

    /**
     * Sets the UI on which to show the notifications. By default, the UI that was active when the roll animator was
     * created is used.
     *
     * @param ui the ui on which to show the notifications
     */
    public void setUi(UI ui) {
        this.ui = ui;
    }

    /**
     * Sets the rolls of which bus are to be displayed as notifications.
     *
     * @param rollBus the roll bus to listen to. If null, will detach from roll bus and stop displaying rolls.
     */
    public void setRollBus(RollBus rollBus) {
        if (lastRollBusRegistration != null) {
            lastRollBusRegistration.unregister();
        }
        lastRollBusRegistration = rollBus == null ? null : rollBus.registerRollListener(this);
    }


    @Override
    public void onEvent(RollEvent event) {
        LocalizedString nullSafeDescription = event.getDescription() == null ? t -> t.translate(
                "character_sheet.dice.default_roll_description") : event.getDescription();
        execute(this::showRollingAnimation);
        Notification resultNotification = createResultNotification(event.getRoll(), event.getPartialResult(),
                event.getResult(), nullSafeDescription);

        executeDelayed(() -> {
            minimizeOpenNotifications();
            resultNotification.open();
        }, ROLL_RESULT_DELAY_MS);
    }

    private void minimizeOpenNotifications() {
        for (var pair : openNotificationLayouts) {
            RollResult result = pair.getFirst();
            VerticalLayout layout = pair.getSecond();
            layout.removeAll();
            layout.add(result.getCompactLayout());
        }
    }

    private void showRollingAnimation() {
        Notification notification = new Notification(
                translationProvider.translate("character_sheet.dice.roll_in_progress"), ROLL_IN_PROGRESS_DURATION_MS,
                Notification.Position.MIDDLE);
        notification.open();
    }

    private Notification createResultNotification(DiceExpression roll, DiceExpression partialResult, int result, LocalizedString rollDescription) {
        RollResult rollResult = new RollResult(rollDescription, translationProvider, roll, partialResult,
                new DiceConstant(result));
        VerticalLayout resultLayout = new VerticalLayout(rollResult.getDetailedLayout());
        var pair = new Pair<>(rollResult, resultLayout);

        Notification resultNotification = new Notification(resultLayout);
        resultNotification.setDuration(ROLL_RESULT_DURATION_MS);
        resultNotification.setPosition(Notification.Position.MIDDLE);
        resultNotification.addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                openNotificationLayouts.add(pair);
            } else {
                openNotificationLayouts.remove(pair);
            }
        });
        return resultNotification;
    }

    private void execute(Runnable task) {
        executeDelayed(task, 0);
    }

    private void executeDelayed(Runnable task, long delayMs) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ui.access(task::run);
            }
        };
        timer.schedule(timerTask, delayMs);
    }
}

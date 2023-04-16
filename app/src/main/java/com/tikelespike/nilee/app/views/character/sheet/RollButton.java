package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.tikelespike.nilee.core.property.Property;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Timer;
import java.util.TimerTask;

public class RollButton extends Button {

    private final Property<DiceExpression> rollProperty;

    private final TranslationProvider translationProvider;

    private LocalizedString rollDescription;

    public RollButton(Property<DiceExpression> rollProperty, TranslationProvider translationProvider, LocalizedString rollDescription) {
        this.rollProperty = rollProperty;
        this.translationProvider = translationProvider;
        this.rollDescription = rollDescription;

        setText(rollProperty.getValue().toLocalizedString(true).getTranslation(translationProvider));
        addClickListener(e -> {
            makeRoll(rollProperty);
        });
    }

    private void makeRoll(Property<DiceExpression> rollProperty) {
        showRollingAnimation(rollProperty);
        Notification resultNotification = createResultNotification(rollProperty);

        executeDelayed(resultNotification::open, 1200);
    }

    private void showRollingAnimation(Property<DiceExpression> rollProperty) {
        Notification notification = new Notification("Rolling...", 700,
                Notification.Position.MIDDLE);
        notification.open();
    }

    private Notification createResultNotification(Property<DiceExpression> rollProperty) {
        DiceExpression roll = rollProperty.getValue();
        DiceExpression partialResult = roll.evaluatePartially();
        int result = partialResult.evaluate();

        String rollString = roll.toLocalizedString().getTranslation(translationProvider);
        String partialString = partialResult.toLocalizedString().getTranslation(translationProvider);

        VerticalLayout resultLayout = new VerticalLayout();
        H3 headline = new H3(rollDescription.getTranslation(translationProvider));
        Icon icon = new Icon(VaadinIcon.CUBE);
        HorizontalLayout rollDescriptor = new HorizontalLayout(icon, new Text(rollString));
        Div partialText = new Div(new Text("= " + partialString));
        Div resultText = new Div(new Text("= " + result));
        resultText.getStyle().set("font-size", "2em");
        resultLayout.add(headline, rollDescriptor, new Hr(), partialText, resultText);
        resultLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, partialText, resultText);


        Notification resultNotification = new Notification(resultLayout);
        resultNotification.setDuration(4000);
        resultNotification.setPosition(Notification.Position.MIDDLE);
        return resultNotification;
    }

    public Property<DiceExpression> getRollProperty() {
        return rollProperty;
    }

    private void executeDelayed(Runnable task, long delayMs) {
        UI ui = UI.getCurrent();
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

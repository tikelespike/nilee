package com.tikelespike.nilee.app.views.character.sheet.dice;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * The result of a dice roll, including the steps made to calculate the result. Can generate a Vaadin layout to display
 * the result and its calculation.
 */
public class RollResult {

    private final LocalizedString rollDescription;
    private final DiceExpression[] computationSteps;
    private final TranslationProvider translationProvider;

    /**
     * Creates a new roll result. The first step in the computation steps is expected to be the original roll expression,
     * the last step is expected to be the final result. All steps in between will be treated as intermediate results.
     *
     * @param description         a short description of what was rolled for, e.g. "Attack roll" (for display in the layout)
     * @param translationProvider translation provider to use for translating the UI components generated by this
     * @param computationSteps    all steps from the initial roll to the final result, including the original roll expression,
     *                            intermediate results (if any) and the final result (in that order). May not be empty.
     *                            If the length is 1, the result is assumed to be the original roll expression.
     * @throws IllegalArgumentException if the computation steps are empty
     */
    public RollResult(LocalizedString description, @NotNull TranslationProvider translationProvider, @NotNull DiceExpression... computationSteps) {
        this.translationProvider = Objects.requireNonNull(translationProvider);
        this.rollDescription = description == null ? t -> t.translate(
                "character_sheet.dice.default_roll_description") : description;
        this.computationSteps = computationSteps;
        if (computationSteps.length < 1) {
            throw new IllegalArgumentException("At least one computation step is required to describe the result");
        }
    }

    /**
     * Creates a vaadin layout that shows what was rolled for, all steps to calculate the result, and the result
     * itself.
     *
     * @return a Vaadin layout that displays the result and its calculation
     */
    public Component getDetailedLayout() {
        VerticalLayout resultLayout = new VerticalLayout();

        addHeader(resultLayout);

        for (int i = 1; i < computationSteps.length - 1; i++) {
            addPartialResult(resultLayout, computationSteps[i]);
        }

        addResult(resultLayout, computationSteps[computationSteps.length - 1]);

        return resultLayout;
    }

    /**
     * Creates a vaadin layout that shows what was rolled for and the result itself, in a compact format.
     *
     * @return a vaadin layout showing the result of the roll in a compact form
     */
    public Component getCompactLayout() {
        String rollString = rollDescription.getTranslation(translationProvider);
        String resultString = computationSteps[computationSteps.length - 1].toLocalizedString().getTranslation(
                translationProvider);
        Span description = new Span(rollString + ":");
        Span result = new Span(resultString);
        result.getStyle().set("font-weight", "bold");
        HorizontalLayout resultLayout = new HorizontalLayout(description, result);
        resultLayout.expand(description);
        resultLayout.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        return resultLayout;
    }

    private void addHeader(VerticalLayout resultLayout) {
        H3 headline = new H3(rollDescription.getTranslation(translationProvider));
        HorizontalLayout rollDescriptor = createRollDescriptor(computationSteps[0]);

        resultLayout.add(headline, rollDescriptor, new Hr());
    }

    private void addResult(VerticalLayout resultLayout, DiceExpression result) {
        String resultString = result.toLocalizedString().getTranslation(translationProvider);
        Div resultText = new Div(new Text("= " + resultString));
        resultText.getStyle().set("font-size", "var(--lumo-font-size-xxxl)");
        resultLayout.add(resultText);
        resultLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, resultText);
    }

    private void addPartialResult(VerticalLayout resultLayout, DiceExpression partialResult) {
        String partialString = partialResult.toLocalizedString().getTranslation(translationProvider);
        Div partialText = new Div(new Text("= " + partialString));
        resultLayout.add(partialText);
        resultLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, partialText);
    }

    private HorizontalLayout createRollDescriptor(DiceExpression roll) {
        Icon icon = new Icon(VaadinIcon.CUBE);
        String rollString = roll.toLocalizedString().getTranslation(translationProvider);
        return new HorizontalLayout(icon, new Text(rollString));
    }
}
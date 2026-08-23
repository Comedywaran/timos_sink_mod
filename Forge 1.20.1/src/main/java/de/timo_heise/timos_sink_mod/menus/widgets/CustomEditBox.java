package de.timo_heise.timos_sink_mod.menus.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CustomEditBox extends EditBox {
    private Consumer<String> onLooseFocus;
    private String oldValue;
    private Predicate<String> validator = Objects::nonNull;
    private Consumer<String> responder;

    public CustomEditBox(Font font, int x, int y, int width, int height, Component message, String initialValue) {
        super(font, x, y, width, height, message);
        super.setResponder(this::onValueChange);
        setTextColor(-1);
        setTextColorUneditable(-1);
        setMaxLength(Integer.MAX_VALUE);
        setOnLooseFocus(onLooseFocus);
        setValue(initialValue);
        oldValue = getValue();
    }

    public void setOnLooseFocus(Consumer<String> onLooseFocus) {
        this.onLooseFocus = onLooseFocus;
    }

    @Override
    public void setFocused(boolean pFocused) {
        boolean wasFocused = isFocused();
        super.setFocused(pFocused);
        if(wasFocused && !isFocused()) {
            OnLooseFocus();
        }
    }

    private void OnLooseFocus() {
        if(validator.test(getValue())) {
            oldValue = getValue();
            onLooseFocus.accept(getValue());
        }
        else {
            setValue(oldValue);
        }
    }

    private void onValueChange(String newText) {
        if (this.responder != null) {
            this.responder.accept(newText);
        }
        if(validator.test(newText)) {
            setTextColor(-1);
        }
        else {
            setTextColor(0xFF909090);
        }
    }

    @Override
    public void setResponder(Consumer<String> responder) {
        this.responder = responder;
    }

    public void setValidator(Predicate<String> validator) {
        this.validator = validator;
    }
}

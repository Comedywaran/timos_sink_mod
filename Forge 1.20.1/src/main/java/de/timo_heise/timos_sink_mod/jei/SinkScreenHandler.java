package de.timo_heise.timos_sink_mod.jei;

import de.timo_heise.timos_sink_mod.menus.sink.AbstractSinkConfigScreen;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.gui.handlers.IScreenHandler;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class SinkScreenHandler implements IScreenHandler<AbstractSinkConfigScreen> {
    @Override
    public @Nullable IGuiProperties apply(AbstractSinkConfigScreen screen) {
        if(screen.getWidth() == 0 || screen.getHeight() == 0 || screen.getImageHeight() == 0 || screen.getImageWidth() == 0) {return null;}

        return new IGuiProperties() {
            @Override
            public Class<? extends Screen> getScreenClass() {
                return AbstractSinkConfigScreen.class;
            }

            @Override
            public int getGuiLeft() {
                return screen.getLeftPos();
            }

            @Override
            public int getGuiTop() {
                return screen.getTopPos();
            }

            @Override
            public int getGuiXSize() {
                return screen.getImageWidth();
            }

            @Override
            public int getGuiYSize() {
                return screen.getImageHeight();
            }

            @Override
            public int getScreenWidth() {
                return screen.getWidth();
            }

            @Override
            public int getScreenHeight() {
                return screen.getHeight();
            }
        };
    }
}

//TODO: fix jei item list not updating values on window resize

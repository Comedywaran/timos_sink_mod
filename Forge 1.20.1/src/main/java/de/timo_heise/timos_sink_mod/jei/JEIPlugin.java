package de.timo_heise.timos_sink_mod.jei;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import de.timo_heise.timos_sink_mod.menus.sink.AbstractSinkConfigScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "jei_plugin");
    }
}
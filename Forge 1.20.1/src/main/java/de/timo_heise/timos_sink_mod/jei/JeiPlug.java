package de.timo_heise.timos_sink_mod.jei;

import de.timo_heise.timos_sink_mod.TimosSinkMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JeiPlug implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TimosSinkMod.MOD_ID, "jei_plugin");
    }
}
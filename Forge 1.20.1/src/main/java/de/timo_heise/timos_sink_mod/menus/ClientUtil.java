package de.timo_heise.timos_sink_mod.menus;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public final class ClientUtil {
    private static final Map<String, String> modNameCache = new HashMap<>();

    public static String getModNameForModId(String modId) {
        return modNameCache.computeIfAbsent(modId, ClientUtil::computeModNameForModId);
    }

    private static String computeModNameForModId(String modId) {
        return ModList.get()
                .getModContainerById(modId)
                .map(ModContainer::getModInfo)
                .map(IModInfo::getDisplayName)
                .orElseGet(() -> StringUtils.capitalize(modId));
    }

    public static boolean isRealFluid(String fluid) {
        return getFluidFromID(fluid) != Fluids.EMPTY;
    }

    public static Fluid getFluidFromID(String fluid) {
        if (fluid == null) {return Fluids.EMPTY;}
        fluid = fluid.trim().toLowerCase();
        ResourceLocation location = ResourceLocation.tryParse(fluid);
        if (location == null) {return Fluids.EMPTY;}
        Fluid f = ForgeRegistries.FLUIDS.getValue(location);
        return f == null ? Fluids.EMPTY : f;
    }
}

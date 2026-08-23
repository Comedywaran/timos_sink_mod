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
    public record RectPos(int x, int y, int width, int height) {}
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

    public static boolean betterIsHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) { // the default AbstractContainerScreen isHovering() method is kinda ass
        return pMouseX >= (double)(pX) && pMouseX < (double)(pX + pWidth) && pMouseY >= (double)(pY) && pMouseY < (double)(pY + pHeight);
    }

    public static boolean betterIsHovering(RectPos pos, double pMouseX, double pMouseY) {
        return betterIsHovering(pos.x,  pos.y, pos.width, pos.height, pMouseX, pMouseY);
    }
}

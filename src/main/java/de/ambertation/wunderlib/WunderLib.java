package de.ambertation.wunderlib;

import de.ambertation.wunderlib.general.Logger;

import net.minecraft.resources.ResourceLocation;

public class WunderLib {
    public static final String MOD_ID = "wunderlib";
    public static final Logger LOGGER = new Logger();

    public static ResourceLocation ID(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}

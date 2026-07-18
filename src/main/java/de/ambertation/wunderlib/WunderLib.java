package de.ambertation.wunderlib;

import de.ambertation.wunderlib.general.Logger;

import net.minecraft.resources.Identifier;

public class WunderLib {
    public static final String MOD_ID = "wunderlib";
    public static final Logger LOGGER = new Logger();

    public static Identifier ID(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

package de.ambertation.wunderlib.ui.layout.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import de.ambertation.wunderlib.ui.layout.values.Rectangle;

@Environment(EnvType.CLIENT)
public interface ComponentWithBounds {
    Rectangle getRelativeBounds();
}

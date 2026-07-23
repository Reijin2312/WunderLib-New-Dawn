package de.ambertation.wunderlib.ui.layout.components.render;

import de.ambertation.wunderlib.ui.layout.values.Rectangle;

import net.minecraft.client.gui.GuiGraphicsExtractor;


public interface ComponentRenderer {
    void renderInBounds(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            int mouseX,
            int mouseY,
            float deltaTicks,
            Rectangle bounds,
            Rectangle clipRect
    );
}

package de.ambertation.wunderlib.ui.layout.components.render;

import de.ambertation.wunderlib.ui.layout.values.Rectangle;

import net.minecraft.client.gui.GuiGraphicsExtractor;


public class NullRenderer implements ComponentRenderer {
    @Override
    public void renderInBounds(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            int mouseX,
            int mouseY,
            float deltaTicks,
            Rectangle bounds,
            Rectangle clipRect
    ) {

    }
}

package de.ambertation.wunderlib.ui.vanilla;

import de.ambertation.wunderlib.ui.layout.components.render.ScrollerRenderer;
import de.ambertation.wunderlib.ui.layout.values.Rectangle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;

public class VanillaScrollerRenderer implements ScrollerRenderer {
    public static final VanillaScrollerRenderer DEFAULT = new VanillaScrollerRenderer();

    @Override
    public void renderScrollBar(Rectangle b, int pickerOffset, int pickerSize, float zIndex) {
        b = this.getScrollerBounds(b);
        Rectangle p = this.getPickerBounds(b, pickerOffset, pickerSize);


        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        //scroller background

        bufferBuilder.addVertex(b.left, b.bottom(), zIndex).setColor(0, 0, 0, 255);
        bufferBuilder.addVertex(b.right(), b.bottom(), zIndex).setColor(0, 0, 0, 255);
        bufferBuilder.addVertex(b.right(), b.top, zIndex).setColor(0, 0, 0, 255);
        bufferBuilder.addVertex(b.left, b.top, zIndex).setColor(0, 0, 0, 255);

        //scroll widget shadow
        bufferBuilder.addVertex(p.left, p.bottom(), zIndex).setColor(128, 128, 128, 255);
        bufferBuilder.addVertex(p.right(), p.bottom(), zIndex).setColor(128, 128, 128, 255);
        bufferBuilder.addVertex(p.right(), p.top, zIndex).setColor(128, 128, 128, 255);
        bufferBuilder.addVertex(p.left, p.top, zIndex).setColor(128, 128, 128, 255);

        //scroll widget
        bufferBuilder.addVertex(p.left, p.bottom() - 1, zIndex)
                     .setColor(192, 192, 192, 255);
        bufferBuilder.addVertex(p.right() - 1, p.bottom() - 1, zIndex)
                     .setColor(192, 192, 192, 255);
        bufferBuilder.addVertex(p.right() - 1, p.top, zIndex).setColor(192, 192, 192, 255);
        bufferBuilder.addVertex(p.left, p.top, zIndex).setColor(192, 192, 192, 255);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }
}

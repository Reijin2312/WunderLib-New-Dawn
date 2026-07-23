package de.ambertation.wunderlib.ui.layout.components.render;

import de.ambertation.wunderlib.ui.layout.values.Rectangle;
import de.ambertation.wunderlib.ui.layout.values.Size;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class RenderHelper {
    public static void outline(GuiGraphicsExtractor GuiGraphicsExtractor, int x0, int y0, int x1, int y1, int color) {
        outline(GuiGraphicsExtractor, x0, y0, x1, y1, color, color);
    }

    public static void outline(GuiGraphicsExtractor GuiGraphicsExtractor, int x0, int y0, int x1, int y1, int color1, int color2) {
        int n;
        if (x1 < x0) {
            n = x0;
            x0 = x1;
            x1 = n;
        }

        if (y1 < y0) {
            n = y0;
            y0 = y1;
            y1 = n;
        }
        y1--;
        x1--;

        innerHLine(GuiGraphicsExtractor, x0, x1, y0, color1);
        innerVLine(GuiGraphicsExtractor, x0, y0 + 1, y1, color1);
        innerHLine(GuiGraphicsExtractor, x0 + 1, x1, y1, color2);
        innerVLine(GuiGraphicsExtractor, x1, y0 + 1, y1 - 1, color2);
    }

    public static void hLine(GuiGraphicsExtractor GuiGraphicsExtractor, int x0, int x1, int y, int color) {
        if (x1 < x0) {
            int m = x0;
            x0 = x1;
            x1 = m;
        }

        innerHLine(GuiGraphicsExtractor, x0, x1, y, color);
    }

    protected static void innerHLine(GuiGraphicsExtractor GuiGraphicsExtractor, int x0, int x1, int y, int color) {
        GuiGraphicsExtractor.fill(x0, y, x1 + 1, y + 1, color);
    }

    public static void vLine(GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y0, int y1, int color) {
        if (y1 < y0) {
            int m = y0;
            y0 = y1;
            y1 = m;
        }
        innerVLine(GuiGraphicsExtractor, x, y0, y1, color);
    }

    protected static void innerVLine(GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y0, int y1, int color) {
        GuiGraphicsExtractor.fill(x, y0, x + 1, y1 + 1, color);
    }

    /**
     * Alternative implementation using the new submit system if you need more control
     */
    private static void innerFillAdvanced(GuiGraphicsExtractor GuiGraphicsExtractor, int x0, int y0, int x1, int y1, int color) {
        // This approach uses the new render state submission system
        // You would need to create a custom ColoredRectangleRenderState if needed
        GuiGraphicsExtractor.fill(x0, y0, x1, y1, color);
    }

    public static void renderImage(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            int left, int top,
            Identifier location,
            Size resourceSize, Rectangle uvRect,
            float alpha
    ) {
        renderImage(GuiGraphicsExtractor, left, top, uvRect.width, uvRect.height, location, resourceSize, uvRect, alpha);
    }

    public static void renderImage(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            int left, int top,
            int width, int height,
            Identifier location,
            Size resourceSize, Rectangle uvRect,
            float alpha
    ) {
        int color = ((int) (Math.max(0.0F, Math.min(1.0F, alpha)) * 255.0F) << 24) | 0xFFFFFF;
        GuiGraphicsExtractor.blit(
                RenderPipelines.GUI_TEXTURED,
                location,
                left,
                top,
                uvRect.left,
                uvRect.top,
                width,
                height,
                uvRect.width,
                uvRect.height,
                resourceSize.width(),
                resourceSize.height(),
                color
        );
    }

    /**
     * Alternative image rendering method using the new pipeline system
     */
    public static void renderImageWithPipeline(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            int left, int top,
            int width, int height,
            Identifier location,
            Size resourceSize, Rectangle uvRect,
            float alpha
    ) {
        renderImage(GuiGraphicsExtractor, left, top, width, height, location, resourceSize, uvRect, alpha);
    }
}

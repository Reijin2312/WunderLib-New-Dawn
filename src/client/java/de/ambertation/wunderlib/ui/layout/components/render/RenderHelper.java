package de.ambertation.wunderlib.ui.layout.components.render;

import de.ambertation.wunderlib.ui.ColorHelper;
import de.ambertation.wunderlib.ui.layout.values.Rectangle;
import de.ambertation.wunderlib.ui.layout.values.Size;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import org.joml.Matrix4f;

public class RenderHelper {
    public static void outline(GuiGraphics guiGraphics, int x0, int y0, int x1, int y1, int color) {
        outline(guiGraphics, x0, y0, x1, y1, color, color);
    }

    public static void outline(GuiGraphics guiGraphics, int x0, int y0, int x1, int y1, int color1, int color2) {
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

        Matrix4f transform = guiGraphics.pose().last().pose();
        innerHLine(transform, x0, x1, y0, color1);
        innerVLine(transform, x0, y0 + 1, y1, color1);
        innerHLine(transform, x0 + 1, x1, y1, color2);
        innerVLine(transform, x1, y0 + 1, y1 - 1, color2);
    }

    public static void hLine(GuiGraphics guiGraphics, int x0, int x1, int y, int color) {
        if (x1 < x0) {
            int m = x0;
            x0 = x1;
            x1 = m;
        }

        innerHLine(guiGraphics.pose().last().pose(), x0, x1, y, color);
    }

    protected static void innerHLine(Matrix4f transform, int x0, int x1, int y, int color) {
        innerFill(transform, x0, y, x1 + 1, y + 1, color);
    }

    public static void vLine(GuiGraphics guiGraphics, int x, int y0, int y1, int color) {
        if (y1 < y0) {
            int m = y0;
            y0 = y1;
            y1 = m;
        }
        innerVLine(guiGraphics.pose().last().pose(), x, y0, y1, color);
    }

    protected static void innerVLine(Matrix4f transform, int x, int y0, int y1, int color) {
        innerFill(transform, x, y0, x + 1, y1 + 1, color);
    }

    private static void innerFill(Matrix4f transform, int x0, int y0, int x1, int y1, int color) {
        float[] cl = ColorHelper.toFloatArrayRGBA(color);

        BufferBuilder bufferBuilder = Tesselator
                .getInstance()
                .begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        bufferBuilder.addVertex(transform, (float) x0, (float) y1, 0.0F).setColor(cl[0], cl[1], cl[2], cl[3]);
        bufferBuilder.addVertex(transform, (float) x1, (float) y1, 0.0F).setColor(cl[0], cl[1], cl[2], cl[3]);
        bufferBuilder.addVertex(transform, (float) x1, (float) y0, 0.0F).setColor(cl[0], cl[1], cl[2], cl[3]);
        bufferBuilder.addVertex(transform, (float) x0, (float) y0, 0.0F).setColor(cl[0], cl[1], cl[2], cl[3]);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    public static void renderImage(
            GuiGraphics guiGraphics,
            int left, int top,
            ResourceLocation location,
            Size resourceSize, Rectangle uvRect,
            float alpha
    ) {
        renderImage(guiGraphics, left, top, uvRect.width, uvRect.height, location, resourceSize, uvRect, alpha);
    }


    public static void renderImage(
            GuiGraphics guiGraphics,
            int left, int top,
            int width, int height,
            ResourceLocation location,
            Size resourceSize, Rectangle uvRect,
            float alpha
    ) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(
                location,
                left, top, width, height,
                uvRect.left, uvRect.top, uvRect.width, uvRect.height,
                resourceSize.width(),
                resourceSize.height()
        );
    }
}

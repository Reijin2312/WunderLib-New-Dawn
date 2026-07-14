package de.ambertation.wunderlib.ui;

import de.ambertation.wunderlib.WunderLib;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.io.File;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemHelper {
    private ItemHelper() {
    }

    public static void renderAll(
            @NotNull Stream<Item> items,
            @NotNull File folder
    ) {
        renderAll(items, 8.f, folder);
    }

    public static void renderAll(
            @NotNull Stream<Item> items,
            float scale,
            @NotNull File folder
    ) {
        folder.mkdirs();
        items.forEach(item -> {
            var id = BuiltInRegistries.ITEM.getKey(item);
            File subFolder = new File(folder, id.getNamespace());
            subFolder.mkdirs();
            ItemStack stack = new ItemStack(item);
            var file = new File(subFolder, id.getPath() + ".png");
            renderToFile(stack, scale, file);
        });
    }

    public static void renderToFile(
            @NotNull ItemLike item,
            @NotNull File file
    ) {
        renderToFile(new ItemStack(item), null, 8.f, file);
    }

    public static void renderToFile(
            @NotNull ItemStack stack,
            @NotNull File file
    ) {
        renderToFile(stack, null, 8.f, file);
    }

    public static void renderToFile(
            @NotNull ItemStack stack,
            float scale,
            @NotNull File file
    ) {
        renderToFile(stack, null, scale, file);
    }

    public static void renderToFile(
            @NotNull ItemStack stack,
            @Nullable String overlayText,
            float scale,
            @NotNull File file
    ) {
        RenderSystem.recordRenderCall(() -> {
            var framebuffer = createRenderContext((int) (16 * scale), (int) (16 * scale));
            renderToFramebuffer(stack, overlayText, scale, framebuffer);
            write(framebuffer, file);
        });
    }

    // Method to create a framebuffer and render context
    private static RenderTarget createRenderContext(int width, int height) {
        RenderTarget framebuffer = new TextureTarget(width, height, true, Minecraft.ON_OSX);
        framebuffer.setClearColor(1.0F, 1.0F, 1.0F, 0.0F);

        return framebuffer;
    }

    // Method to render the scene into the specified framebuffer
    private static void renderToFramebuffer(ItemStack stack, String text, float scale, RenderTarget framebuffer) {
        Minecraft minecraft = Minecraft.getInstance();

        RenderSystem.viewport(0, 0, framebuffer.viewWidth, framebuffer.viewHeight);
        // Set up rendering context
        framebuffer.bindWrite(true);

        RenderBuffers renderBuffers = minecraft.renderBuffers();
        RenderSystem.clear(GlConst.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
        Matrix4f matrix4f = new Matrix4f().setOrtho(
                0.0f,
                framebuffer.viewWidth / scale,
                framebuffer.viewHeight / scale,
                0.0f,
                1000.0f,
                21000.0f
        );
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        RenderSystem.disableDepthTest();
        Matrix4fStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushMatrix();
        poseStack.identity();
        poseStack.translate(0.0f, 0.0f, -11000.0f);
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();

        MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();
        GuiGraphics guiGraphics = new GuiGraphics(minecraft, bufferSource);
        guiGraphics.renderFakeItem(stack, 0, 0);
        if (stack.getCount() > 1 && text == null) text = String.valueOf(stack.getCount());
        if (text != null) {
            guiGraphics.renderItemDecorations(
                    Minecraft.getInstance().font,
                    stack, 0, 0, text
            );
        }
        guiGraphics.flush();
        poseStack.popMatrix();
        RenderSystem.applyModelViewMatrix();

        framebuffer.unbindWrite();

        // Bind the main framebuffer again
        minecraft.getMainRenderTarget().bindWrite(true);
    }

    // Same as Screenshots#takeScreenshot but this version
    // will keep the alpha channel
    private static NativeImage takeScreenshot(RenderTarget renderTarget) {
        NativeImage nativeImage = new NativeImage(renderTarget.width, renderTarget.height, false);
        RenderSystem.bindTexture(renderTarget.getColorTextureId());
        nativeImage.downloadTexture(0, false);
        nativeImage.flipY();
        return nativeImage;
    }

    private static void write(RenderTarget framebuffer, File file2) {
        NativeImage img = takeScreenshot(framebuffer);

        Util.ioPool().execute(() -> {
            try {
                img.writeToFile(file2);
            } catch (Exception exception) {
                WunderLib.LOGGER.warn("Couldn't save screenshot", exception);
            } finally {
                img.close();
            }
        });
    }
}

package de.ambertation.wunderlib.ui;

import de.ambertation.wunderlib.WunderLib;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GlyphRenderState;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.io.File;
import java.util.stream.Stream;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;

public class ItemHelper {
    private static @Nullable ProjectionMatrixBuffer ITEM_PROJECTION;
    private static @Nullable ProjectionMatrixBuffer GUI_PROJECTION;

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
        executeRender(stack, overlayText, scale, file);
    }

    private static void executeRender(ItemStack stack, String overlayText, float scale, File file) {
        // Calculate size based on scale - standard item is 16x16
        int size = (int) (16 * scale);

        RenderSystem.assertOnRenderThread();

        // Create a render target for our item
        RenderTarget framebuffer = new TextureTarget("wunderlib_item", size, size, GpuFormat.RGBA8_UNORM, GpuFormat.D32_FLOAT);

        try {
            clearRenderTarget(framebuffer);
            renderItemToFramebuffer(stack, overlayText, scale, framebuffer);
            writeFramebufferToFile(framebuffer, file);
        } finally {
            framebuffer.destroyBuffers();
        }
    }

    private static void renderItemToFramebuffer(ItemStack stack, String text, float scale, RenderTarget framebuffer) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.assertOnRenderThread();

        RenderSystem.backupProjectionMatrix();
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();

        var previousLights = RenderSystem.getShaderLights();

        try {
            RenderSystem.setProjectionMatrix(
                    itemProjectionBuffer().getBuffer(createOrthoProjection(framebuffer.width, framebuffer.height, -1000.0F, 1000.0F, true)),
                    ProjectionType.ORTHOGRAPHIC
            );

            TrackingItemStackRenderState renderState = new TrackingItemStackRenderState();
            minecraft.getItemModelResolver().updateForTopItem(renderState, stack, ItemDisplayContext.GUI, minecraft.level, null, 0);

            if (renderState.usesBlockLight()) {
                minecraft.gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_3D);
            } else {
                minecraft.gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_FLAT);
            }

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            float size = framebuffer.width;
            poseStack.translate(size / 2.0F, size / 2.0F, 0.0F);
            poseStack.scale(size, -size, size);

            RenderSystem.enableScissorForRenderTypeDraws(0, framebuffer.height - (int) size, (int) size, (int) size);
            SubmitNodeStorage submitNodeStorage = new SubmitNodeStorage();
            renderState.submit(poseStack, submitNodeStorage, 15728880, OverlayTexture.NO_OVERLAY, 0);
            try (
                    var frame = minecraft.gameRenderer.featureRenderDispatcher().prepareFrame(submitNodeStorage);
                    RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                            () -> "WunderLib item",
                            framebuffer.getColorTextureView(),
                            java.util.Optional.empty(),
                            framebuffer.getDepthTextureView(),
                            java.util.OptionalDouble.empty()
                    )
            ) {
                RenderSystem.bindDefaultUniforms(renderPass);
                net.minecraft.client.renderer.feature.FeatureRenderDispatcher.renderAllFeatures(renderPass, frame);
            }
            RenderSystem.disableScissorForRenderTypeDraws();
            poseStack.popPose();

            renderItemDecorationsToTarget(stack, text, scale, framebuffer);
        } finally {
            if (previousLights != null) {
                RenderSystem.setShaderLights(previousLights);
            }
            modelViewStack.popMatrix();
            RenderSystem.restoreProjectionMatrix();
        }
    }

    /**
     * Write the framebuffer contents to a file using the Screenshot API
     */
    private static void writeFramebufferToFile(RenderTarget framebuffer, File file) {
        try {
            // Use the Screenshot API to capture the framebuffer contents
            Screenshot.takeScreenshot(framebuffer, nativeImage -> Util.ioPool().execute(() -> {
                try {
                    // The NativeImage already contains exactly what we rendered
                    nativeImage.writeToFile(file);
                    WunderLib.LOGGER.info("Successfully saved item render to: " + file.getAbsolutePath());
                } catch (Exception exception) {
                    WunderLib.LOGGER.warn("Couldn't save item render", exception);
                } finally {
                    nativeImage.close();
                }
            }));
        } catch (Exception e) {
            WunderLib.LOGGER.error("Failed to capture item render", e);
        }
    }

    /**
     * Render an item within an existing GUI context (most reliable method)
     * Based on the renderSlot method from Gui class
     */
    public static void renderToExistingContext(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            ItemStack stack,
            @Nullable String overlayText,
            float scale,
            int x, int y
    ) {
        if (stack.isEmpty()) {
            return;
        }

        GuiGraphicsExtractor.pose().pushMatrix();
        GuiGraphicsExtractor.pose().translate((float) x, (float) y);
        GuiGraphicsExtractor.pose().scale(scale, scale);

        // Render the item using the same method as the hotbar
        GuiGraphicsExtractor.fakeItem(stack, 0, 0);

        // Render decorations (count, durability bar, cooldown overlay)
        String text = overlayText;
        if (stack.getCount() > 1 && text == null) text = String.valueOf(stack.getCount());
        if (text != null) {
            GuiGraphicsExtractor.itemDecorations(Minecraft.getInstance().font, stack, 0, 0, text);
        }

        GuiGraphicsExtractor.pose().popMatrix();
    }

    /**
     * Alternative method that renders to a specific area within an existing framebuffer
     * Useful for creating item grids or inventories
     */
    public static void renderItemGrid(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            ItemStack[] items,
            int startX, int startY,
            int itemSize, int spacing,
            int columns
    ) {
        for (int i = 0; i < items.length; i++) {
            if (!items[i].isEmpty()) {
                int col = i % columns;
                int row = i / columns;
                int x = startX + col * (itemSize + spacing);
                int y = startY + row * (itemSize + spacing);

                float scale = itemSize / 16.0f; // 16 is the standard item size
                renderToExistingContext(GuiGraphicsExtractor, items[i], null, scale, x, y);
            }
        }
    }

    /**
     * Utility method to render a single item at standard size (16x16)
     */
    public static void renderStandardItem(GuiGraphicsExtractor GuiGraphicsExtractor, ItemStack stack, int x, int y) {
        renderToExistingContext(GuiGraphicsExtractor, stack, null, 1.0f, x, y);
    }

    private static void clearRenderTarget(RenderTarget framebuffer) {
        var encoder = RenderSystem.getDevice().createCommandEncoder();
        encoder.clearColorAndDepthTextures(framebuffer.getColorTexture(), new Vector4f(), framebuffer.getDepthTexture(), 0.0);
    }

    private static ProjectionMatrixBuffer itemProjectionBuffer() {
        if (ITEM_PROJECTION == null) {
            ITEM_PROJECTION = new ProjectionMatrixBuffer("wunderlib_items");
        }
        return ITEM_PROJECTION;
    }

    private static ProjectionMatrixBuffer guiProjectionBuffer() {
        if (GUI_PROJECTION == null) {
            GUI_PROJECTION = new ProjectionMatrixBuffer("wunderlib_gui");
        }
        return GUI_PROJECTION;
    }

    private static void renderItemDecorationsToTarget(ItemStack stack, @Nullable String overlayText, float scale, RenderTarget framebuffer) {
        Minecraft minecraft = Minecraft.getInstance();

        GuiRenderState guiRenderState = new GuiRenderState();
        GuiGraphicsExtractor GuiGraphicsExtractor = new GuiGraphicsExtractor(minecraft, guiRenderState, 0, 0);

        GuiGraphicsExtractor.pose().pushMatrix();
        GuiGraphicsExtractor.pose().scale(scale, scale);

        String text = overlayText;
        if (stack.getCount() > 1 && text == null) text = String.valueOf(stack.getCount());
        GuiGraphicsExtractor.itemDecorations(minecraft.font, stack, 0, 0, text);

        GuiGraphicsExtractor.pose().popMatrix();

        guiRenderState.forEachText(textState -> textState.ensurePrepared().visit(new Font.GlyphVisitor() {
            @Override
            public void acceptGlyph(TextRenderable.Styled renderable) {
                guiRenderState.addGlyphToCurrentLayer(new GlyphRenderState(textState.pose, renderable, textState.scissor));
            }

            @Override
            public void acceptEffect(TextRenderable renderable) {
                guiRenderState.addGlyphToCurrentLayer(new GlyphRenderState(textState.pose, renderable, textState.scissor));
            }
        }));

        RenderSystem.setProjectionMatrix(
                guiProjectionBuffer().getBuffer(createOrthoProjection(framebuffer.width, framebuffer.height, 1000.0F, 11000.0F, true)),
                ProjectionType.ORTHOGRAPHIC
        );
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F));

        guiRenderState.forEachElement(
                element -> drawGuiElement(framebuffer, element, dynamicTransforms),
                GuiRenderState.TraverseRange.ALL
        );
    }

    private static void drawGuiElement(RenderTarget framebuffer, GuiElementRenderState element, GpuBufferSlice dynamicTransforms) {
        RenderPipeline pipeline = element.pipeline();
        TextureSetup textureSetup = element.textureSetup();

        ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(256);
        BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, pipeline.getPrimitiveTopology(), pipeline.getVertexFormatBinding(0));
        element.buildVertices(bufferBuilder);
        MeshData mesh = bufferBuilder.build();
        if (mesh == null) {
            byteBufferBuilder.close();
            return;
        }

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(
                        () -> "WunderLib GUI element",
                        framebuffer.getColorTextureView(),
                        java.util.Optional.empty(),
                        framebuffer.getDepthTextureView(),
                        java.util.OptionalDouble.empty()
                )) {
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            renderPass.setPipeline(RenderSystem.getCompiledPipeline(pipeline));

            if (textureSetup.texure0() != null) {
                renderPass.setUniform("Sampler0", textureSetup.texure0(), textureSetup.sampler0());
            }
            if (textureSetup.texure1() != null) {
                renderPass.setUniform("Sampler1", textureSetup.texure1(), textureSetup.sampler1());
            }
            if (textureSetup.texure2() != null) {
                renderPass.setUniform("Sampler2", textureSetup.texure2(), textureSetup.sampler2());
            }

            ScreenRectangle scissor = element.scissorArea();
            if (scissor != null) {
                int x = scissor.left();
                int y = framebuffer.height - scissor.bottom();
                renderPass.enableScissor(x, y, scissor.width(), scissor.height());
            } else {
                renderPass.disableScissor();
            }

            GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "WunderLib GUI vertices", GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer());
            GpuBuffer indexBuffer;
            IndexType indexType;
            boolean ownsIndexBuffer = false;
            if (mesh.indexBuffer() == null) {
                RenderSystem.AutoStorageIndexBuffer autoIndex = RenderSystem.getSequentialBuffer(mesh.drawState().primitiveTopology());
                indexBuffer = autoIndex.getBuffer(mesh.drawState().indexCount());
                indexType = autoIndex.type();
            } else {
                indexBuffer = RenderSystem.getDevice().createBuffer(() -> "WunderLib GUI indices", GpuBuffer.USAGE_INDEX, mesh.indexBuffer());
                indexType = mesh.drawState().indexType();
                ownsIndexBuffer = true;
            }

            try {
                renderPass.setVertexBuffer(0, vertexBuffer.slice());
                renderPass.setIndexBuffer(indexBuffer, indexType);
                renderPass.drawIndexed(mesh.drawState().indexCount(), 1, 0, 0, 0);
            } finally {
                vertexBuffer.close();
                if (ownsIndexBuffer) {
                    indexBuffer.close();
                }
            }
        } finally {
            mesh.close();
            byteBufferBuilder.close();
        }
    }

    private static Matrix4f createOrthoProjection(float width, float height, float zNear, float zFar, boolean invertY) {
        return new Matrix4f().setOrtho(0.0F, width, invertY ? height : 0.0F, invertY ? 0.0F : height, zNear, zFar);
    }
}

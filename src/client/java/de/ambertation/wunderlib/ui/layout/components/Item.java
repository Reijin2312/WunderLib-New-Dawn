package de.ambertation.wunderlib.ui.layout.components;

import de.ambertation.wunderlib.ui.layout.values.Rectangle;
import de.ambertation.wunderlib.ui.layout.values.Value;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class Item extends CustomRenderComponent {
    private ItemStack itemStack;
    private String decoration;

    public Item(Value width, Value height) {
        super(width, height);
    }

    public Item setItem(ItemLike item) {
        return setItem(new ItemStack(item));
    }

    public Item setItem(ItemStack item) {
        this.itemStack = item;
        return this;
    }

    public Item setDecoration(String decoration) {
        this.decoration = decoration;
        return this;
    }

    @Override
    protected void customRender(
            GuiGraphicsExtractor GuiGraphicsExtractor,
            int x,
            int y,
            float deltaTicks,
            Rectangle bounds,
            Rectangle clipRect
    ) {
        GuiGraphicsExtractor.fakeItem(
                itemStack,
                bounds.left,
                bounds.top
        );
        if (decoration != null) {
            GuiGraphicsExtractor.itemDecorations(
                    Minecraft.getInstance().font,
                    itemStack,
                    bounds.left,
                    bounds.top,
                    decoration
            );
        } else if (itemStack.getCount() > 1) {
            GuiGraphicsExtractor.itemDecorations(
                    Minecraft.getInstance().font,
                    itemStack,
                    bounds.left,
                    bounds.top,
                    "" + itemStack.getCount()
            );
        }
    }

    @Override
    public int getContentWidth() {
        return 16;
    }

    @Override
    public int getContentHeight() {
        return 16;
    }

    private boolean focused;

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean bl) {
        focused = bl;
    }
}


package com.github.x3rmination.common.blocks.tile_entities.archive.single_press;

import com.github.x3rmination.X3TECH;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class SinglePressScreen extends ContainerScreen<SinglePressContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(X3TECH.MOD_ID, "textures/gui/single_press.png");
    public SinglePressScreen(SinglePressContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, x, y, partialTicks);
        this.renderTooltip(matrixStack, x, y);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        assert minecraft != null;
        RenderSystem.blendColor(1, 1, 1, 1);
        minecraft.getTextureManager().bind(TEXTURE);

        int posX = (this.width - this.imageWidth)/2;
        int posY = (this.height - this.imageHeight)/2;

        blit(matrixStack, posX, posY, 0, 0, this.imageWidth, this.imageHeight);

        // Arrow
        blit(matrixStack, posX + 67, posY + 35, 176, 14, menu.getProgressArrowScale() + 1, 16);
        // RF meter
        if(menu.getRFMeterScale() != 0) {
            blit(matrixStack, posX + 10, posY + 19, 176, 32, 11, menu.getRFMeterScale() + 1);
        }
        // -1 For alignment
        if(mouseX > posX + 10 - 1 && mouseX < posX + 10 + 11 - 1 && mouseY > posY + 19 - 1 && mouseY < posY + 68 - 1) {
            renderTooltip(matrixStack, new StringTextComponent(menu.getRf() + "/" + menu.getMaxRf() + " RF"), mouseX, mouseY);
        }
    }
}

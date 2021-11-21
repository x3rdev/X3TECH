package com.github.x3rmination.common.blocks.powered_pulverizer;

import com.github.x3rmination.X3TECH;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PoweredPulverizerScreen extends ContainerScreen<PoweredPulverizerContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(X3TECH.MOD_ID, "textures/gui/powered_pulverizer.png");
    public PoweredPulverizerScreen(PoweredPulverizerContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, x, y, partialTicks);
        this.renderTooltip(matrixStack, x, y);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        assert minecraft != null;
        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(TEXTURE);

        int posX = (this.width - this.imageWidth)/2;
        int posY = (this.height - this.imageHeight)/2;

        blit(matrixStack, posX, posY, 0, 0, this.imageWidth, this.imageHeight);

        // Arrow
        blit(matrixStack, posX + 67, posY + 35, 176, 14, menu.getProgressArrowScale() + 1, 16);
    }
}

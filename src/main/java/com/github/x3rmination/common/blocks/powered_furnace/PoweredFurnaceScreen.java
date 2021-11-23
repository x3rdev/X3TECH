package com.github.x3rmination.common.blocks.powered_furnace;

import com.github.x3rmination.X3TECH;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class PoweredFurnaceScreen extends ContainerScreen<PoweredFurnaceContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(X3TECH.MOD_ID, "textures/gui/powered_furnace.png");
    public PoweredFurnaceScreen(PoweredFurnaceContainer container, PlayerInventory playerInventory, ITextComponent title) {
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
        blit(matrixStack, posX + 10,posY + 19, 176, 32, 11, menu.getRFMeterScale());
        System.out.println(menu.getRf());
        System.out.println(menu.getMaxRf());
        System.out.println(menu.getRFMeterScale());
        // -1 For alignment
        if(mouseX > posX + 10 - 1 && mouseX < posX + 10 + 11 - 1 && mouseY > posY + 19 - 1 && mouseY < posY + 68 - 1) {
            renderTooltip(matrixStack, new StringTextComponent(menu.getRf() + "/" + menu.getMaxRf() + " RF"), mouseX, mouseY);
        }
    }
}

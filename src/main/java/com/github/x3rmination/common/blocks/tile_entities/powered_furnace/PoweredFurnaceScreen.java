package com.github.x3rmination.common.blocks.tile_entities.powered_furnace;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.util.ScreenHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PoweredFurnaceScreen extends ContainerScreen<PoweredFurnaceContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(X3TECH.MOD_ID, "textures/gui/powered_furnace.png");
    private Button settingsButton;
    private Boolean settingsMenuOpen = false;
    private char north;
    private char east;
    private char south;
    private char west;
    private char up;
    private char down;

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
        if(menu.getRFMeterScale() != 0) {
            blit(matrixStack, posX + 10, posY + 19, 176, 32, 11, menu.getRFMeterScale() + 1);
        }
        // -1 For alignment
        if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 154, 59, 154 + 11, 59 + 11)) {
            blit(matrixStack, posX + 154, posY + 59, 189, 32, 11, 11);
        }
        if(Boolean.TRUE.equals(settingsMenuOpen)) {
            //Direction Menu
            blit(matrixStack, posX + 179, posY + 45, 201, 44, 38, 38);
            //Item Icon
            blit(matrixStack, posX + 179, posY + 33,229, 32, 11, 11);
            renderInterfacingSquares(matrixStack);
        }
        if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 179, 33, 179 + 11, 33 + 11)) {
            renderTooltip(matrixStack, new TranslationTextComponent("gui.machine.settings.item"), mouseX, mouseY);
        }
        if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 10, 19, 10 + 11, 19 + 50)) {
            renderTooltip(matrixStack, new StringTextComponent(menu.getRf() + "/" + menu.getMaxRf() + " RF"), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        int posX = (this.width - this.imageWidth)/2;
        int posY = (this.height - this.imageHeight)/2;
        if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 154, 59, 154 + 11, 59 + 11)) {
            settingsMenuOpen = Boolean.FALSE.equals(this.settingsMenuOpen);
            playButtonSound();
        }
        if(Boolean.TRUE.equals(settingsMenuOpen)) {
            if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 194, 60, 194 + 9, 60 + 9)) {
                north = toggleDirectionStates(north);
                Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 206, 60, 206 + 9, 60 + 9)) {
                east = toggleDirectionStates(east);
                Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 182, 72, 182 + 9, 72 + 9)) {
                south = toggleDirectionStates(south);
                Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 182, 60, 182 + 9, 60 + 9)) {
                west = toggleDirectionStates(west);
                Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 194, 48, 194 + 9, 48 + 9)) {
                up = toggleDirectionStates(up);
                Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            if(ScreenHelper.isBetween(mouseX, mouseY, posX, posY, 194, 72, 194 + 9, 72 + 9)) {
                down = toggleDirectionStates(down);
                Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }


        }
        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    private void playButtonSound(){
        Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private char toggleDirectionStates(char dir) {
        switch (dir) {
            case 1:
                return 2;
            case 2:
                return 3;
            default:
                return 1;
        }
    }

    private void renderInterfacingSquares(MatrixStack matrixStack) {
        int posX = (this.width - this.imageWidth)/2;
        int posY = (this.height - this.imageHeight)/2;
        switch (north) {
            case 1:
                blit(matrixStack, posX + 194, posY + 60, 189, 47, 9, 9);
                break;
            case 2:
                blit(matrixStack, posX + 194, posY + 60, 189, 57, 9, 9);
                break;
            default:
                blit(matrixStack, posX + 194, posY + 60, 189, 67, 9, 9);
                break;
        }
        switch (east) {
            case 1:
                blit(matrixStack, posX + 206, posY + 60, 189, 47, 9, 9);
                break;
            case 2:
                blit(matrixStack, posX + 206, posY + 60, 189, 57, 9, 9);
                break;
            default:
                blit(matrixStack, posX + 206, posY + 60, 189, 67, 9, 9);
                break;
        }
        switch (south) {
            case 1:
                blit(matrixStack, posX + 182, posY + 72, 189, 47, 9, 9);
                break;
            case 2:
                blit(matrixStack, posX + 182, posY + 72, 189, 57, 9, 9);
                break;
            default:
                blit(matrixStack, posX + 182, posY + 72, 189, 67, 9, 9);
                break;
        }
        switch (west) {
            case 1:
                blit(matrixStack, posX + 182, posY + 60, 189, 47, 9, 9);
                break;
            case 2:
                blit(matrixStack, posX + 182, posY + 60, 189, 57, 9, 9);
                break;
            default:
                blit(matrixStack, posX + 182, posY + 60, 189, 67, 9, 9);
                break;
        }
        switch (up) {
            case 1:
                blit(matrixStack, posX + 194, posY + 48, 189, 47, 9, 9);
                break;
            case 2:
                blit(matrixStack, posX + 194, posY + 48, 189, 57, 9, 9);
                break;
            default:
                blit(matrixStack, posX + 194, posY + 48, 189, 67, 9, 9);
                break;
        }
        switch (down) {
            case 1:
                blit(matrixStack, posX + 194, posY + 72, 189, 47, 9, 9);
                break;
            case 2:
                blit(matrixStack, posX + 194, posY + 72, 189, 57, 9, 9);
                break;
            default:
                blit(matrixStack, posX + 194, posY + 72, 189, 67, 9, 9);
                break;
        }
    }
}

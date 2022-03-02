package com.github.x3rmination.common.blocks.tile_entities.archive.energy_storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class EnergyStorageScreen extends ContainerScreen<EnergyStorageContainer> {
    public EnergyStorageScreen(EnergyStorageContainer menu, PlayerInventory playerInventory, ITextComponent title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {

    }
}

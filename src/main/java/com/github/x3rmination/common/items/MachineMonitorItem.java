package com.github.x3rmination.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class MachineMonitorItem extends Item {

    private double lastEnergy = 0.1;

    public MachineMonitorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(World pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pCount);
    }

    @Override
    public ActionResultType useOn(ItemUseContext pContext) {
        PlayerEntity player = pContext.getPlayer();
        assert player != null;
        BlockState block = player.level.getBlockState(pContext.getClickedPos());
        player.displayClientMessage(new StringTextComponent(block.toString()), true);
        if(block.hasTileEntity()) {
            TileEntity tile = player.level.getBlockEntity(pContext.getClickedPos());
            if(tile != null && tile.getCapability(CapabilityEnergy.ENERGY).isPresent() && !pContext.getLevel().isClientSide) {
                double energy = tile.getCapability(CapabilityEnergy.ENERGY).orElse(null).getEnergyStored();
                NewChatGui chat = Minecraft.getInstance().gui.getChat();
                chat.addMessage(new StringTextComponent(Double.toString(energy)));
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(new TranslationTextComponent("tooltip.machine_monitor.default").setStyle(Style.EMPTY.withColor(Color.fromRgb(0x48ff00))));
    }
}


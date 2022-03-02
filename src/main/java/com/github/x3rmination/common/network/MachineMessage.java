package com.github.x3rmination.common.network;

import com.github.x3rmination.common.blocks.tile_entities.powered_furnace.PoweredFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MachineMessage {

    public int key;
    public int value;

    public MachineMessage(int key, int state) {
        this.key = key;
        this.value = state;
    }

    public static void encode(MachineMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.key);
        buffer.writeInt(message.value);
    }

    public static MachineMessage decode(PacketBuffer buffer) {
        return new MachineMessage(buffer.readInt(), buffer.readInt());
    }

    public static void handle(MachineMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            assert player != null;
            World level = player.getCommandSenderWorld();
            BlockPos pos = ((BlockRayTraceResult)player.pick(10.0D, 0.0F, false)).getBlockPos();
            BlockState state = level.getBlockState(pos);
            if(!(state.getBlock() instanceof PoweredFurnaceBlock)) {
                return;
            }

            switch (message.key) {
                case 1:
                    level.setBlock(pos, level.getBlockState(pos).setValue(PoweredFurnaceBlock.ITEM_NORTH, message.value), 3);
                    return;
                case 2:
                    level.setBlock(pos, level.getBlockState(pos).setValue(PoweredFurnaceBlock.ITEM_EAST, message.value), 3);
                    return;
                case 3:
                    level.setBlock(pos, level.getBlockState(pos).setValue(PoweredFurnaceBlock.ITEM_SOUTH, message.value), 3);
                    return;
                case 4:
                    level.setBlock(pos, level.getBlockState(pos).setValue(PoweredFurnaceBlock.ITEM_WEST, message.value), 3);
                    return;
                case 5:
                    level.setBlock(pos, level.getBlockState(pos).setValue(PoweredFurnaceBlock.ITEM_UP, message.value), 3);
                    return;
                case 6:
                    level.setBlock(pos, level.getBlockState(pos).setValue(PoweredFurnaceBlock.ITEM_DOWN, message.value), 3);
                    return;
                default:
            }
        });
        context.setPacketHandled(true);
    }
}

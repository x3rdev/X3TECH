package com.github.x3rmination.common.network;

import com.github.x3rmination.common.blocks.tile_entities.archive.powered_furnace.PoweredFurnaceBlock;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_furnace.PoweredFurnaceTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MachineMessage {

    public int key;
    public byte value;

    public MachineMessage(int key, int state) {
        this.key = key;
        this.value = (byte) state;
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
            TileEntity tileEntity = level.getBlockEntity(pos);
            if(!(state.getBlock() instanceof PoweredFurnaceBlock) || tileEntity == null) {
                return;
            }

            switch (message.key) {
                case 1:
                    ((PoweredFurnaceTileEntity)tileEntity).setItemNorth(message.value);
                    return;
                case 2:
                    ((PoweredFurnaceTileEntity)tileEntity).setItemEast(message.value);
                    return;
                case 3:
                    ((PoweredFurnaceTileEntity)tileEntity).setItemSouth(message.value);
                    return;
                case 4:
                    ((PoweredFurnaceTileEntity)tileEntity).setItemWest(message.value);
                    return;
                case 5:
                    ((PoweredFurnaceTileEntity)tileEntity).setItemUp(message.value);
                    return;
                case 6:
                    ((PoweredFurnaceTileEntity)tileEntity).setItemDown(message.value);
                    return;
                default:
            }
        });
        context.setPacketHandled(true);
    }
}

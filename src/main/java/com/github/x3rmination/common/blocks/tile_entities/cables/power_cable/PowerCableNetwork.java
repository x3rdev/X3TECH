package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowerCableNetwork {

    private final ArrayList<BlockPos> cableList;
    //import into cable network
    private final ArrayList<BlockPos> importConnections;
    //export out of cable network
    private final ArrayList<BlockPos> outputConnections;

    public PowerCableNetwork(){
        this.cableList = new ArrayList<>();
        this.importConnections = new ArrayList<>();
        this.outputConnections = new ArrayList<>();
    }

    public List<BlockPos> getCableList(){
        return this.cableList;
    }

    public List<BlockPos> getImportConnections(){
        return this.importConnections;
    }

    public List<BlockPos> getOutputConnections(){
        return this.outputConnections;
    }

    public int getNetworkSize() {
        return this.cableList.size() + this.getImportConnections().size() + this.getOutputConnections().size();
    }

    public void addCable(BlockPos cablePos) {
        this.cableList.add(cablePos);
    }

    public void addImportConnection(BlockPos importPos) {
        if(importPos != null) {
            this.importConnections.add(importPos);
        }
    }

    public void addOutputConnection(BlockPos outputPos) {
        if(outputPos != null) {
            this.outputConnections.add(outputPos);
        }
    }

    public void pruneImportConnections(World world){
        for(int i = 0; i < importConnections.size(); i++) {
            if(world.getBlockEntity(importConnections.get(i)) != null) {
                LazyOptional<IEnergyStorage> cap = Objects.requireNonNull(world.getBlockEntity(importConnections.get(i))).getCapability(CapabilityEnergy.ENERGY);
                if(!cap.isPresent() && !cap.orElse(null).canExtract()) {
                    importConnections.remove(i);
                }
            }
        }
    }

    public void pruneOutputConnections(World world){
        for(int i = 0; i < outputConnections.size(); i++) {
            if(world.getBlockEntity(outputConnections.get(i)) != null) {
                LazyOptional<IEnergyStorage> cap = Objects.requireNonNull(world.getBlockEntity(outputConnections.get(i))).getCapability(CapabilityEnergy.ENERGY);
                if(!cap.isPresent() && !cap.orElse(null).canReceive()) {
                    outputConnections.remove(i);
                }
            }
        }
    }

    public void mergeNetworks(PowerCableNetwork network, World world) {
        for(int i = 0; i < network.getCableList().size(); i++) {
            System.out.println("merging networks");
            ((PowerCableBlock) world.getBlockState(network.getCableList().get(i)).getBlock()).setPowerCableNetwork(this);
            if (!this.cableList.contains(network.getCableList().get(i))) {
                this.addCable(network.getCableList().get(i));
            }
        }
        for(int i = 0; i < network.getOutputConnections().size(); i++) {
            this.addOutputConnection(network.getOutputConnections().get(i));
        }
        for(int i = 0; i < network.getImportConnections().size(); i++) {
            this.addImportConnection(network.getImportConnections().get(i));
        }
    }
}

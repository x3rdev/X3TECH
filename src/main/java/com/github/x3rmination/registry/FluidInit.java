package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit {

    public static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, X3TECH.MOD_ID);

    // Liquids
    public static final RegistryObject<FlowingFluid> COAL_TAR_CREOSOTE_FLUID = FLUIDS.register("coal_tar_creosote_fluid",
            () -> new ForgeFlowingFluid.Source(FluidInit.COAL_TAR_CREOSOTE_PROPERTIES));

    public static final RegistryObject<FlowingFluid> COAL_TAR_CREOSOTE_FLOWING = FLUIDS.register("coal_tar_creosote_flowing",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.COAL_TAR_CREOSOTE_PROPERTIES));


    // Liquid Properties
    public static final ForgeFlowingFluid.Properties COAL_TAR_CREOSOTE_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> COAL_TAR_CREOSOTE_FLUID.get(), () -> COAL_TAR_CREOSOTE_FLOWING.get(), FluidAttributes.builder(WATER_STILL, WATER_FLOWING)
            .density(100).luminosity(0).viscosity(100).sound(SoundEvents.BUCKET_FILL).overlay(WATER_OVERLAY)
            .color(0xFF202020)).slopeFindDistance(1).levelDecreasePerBlock(1).block(() -> FluidInit.COAL_TAR_CREOSOTE_BLOCK.get())
            .bucket(() -> ItemInit.COAL_TAR_CREOSOTE_BUCKET.get());

    // Liquid Blocks
    public static final RegistryObject<FlowingFluidBlock> COAL_TAR_CREOSOTE_BLOCK = BlockInit.BLOCKS.register("coal_tar_creosote",
            () -> new FlowingFluidBlock(() -> FluidInit.COAL_TAR_CREOSOTE_FLUID.get(),
                    AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100f).noDrops()));
}

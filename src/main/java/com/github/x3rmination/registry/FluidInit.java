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

    public static final ResourceLocation LIQUID_STILL = new ResourceLocation(X3TECH.MOD_ID,"block/liquid_still");
    public static final ResourceLocation LIQUID_FLOWING = new ResourceLocation(X3TECH.MOD_ID, "block/liquid_flow");
    public static final ResourceLocation LIQUID_OVERLAY = new ResourceLocation(X3TECH.MOD_ID,"block/liquid_overlay");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, X3TECH.MOD_ID);

    // Liquids
    public static final RegistryObject<FlowingFluid> CREOSOTE_FLUID = FLUIDS.register("creosote_fluid",
            () -> new ForgeFlowingFluid.Source(FluidInit.CREOSOTE_PROPERTIES));

    public static final RegistryObject<FlowingFluid> CREOSOTE_FLOWING = FLUIDS.register("creosote_flowing",
            () -> new ForgeFlowingFluid.Flowing(FluidInit.CREOSOTE_PROPERTIES));


    // Liquid Properties
    public static final ForgeFlowingFluid.Properties CREOSOTE_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> CREOSOTE_FLUID.get(), () -> CREOSOTE_FLOWING.get(), FluidAttributes.builder(LIQUID_STILL, LIQUID_FLOWING)
            .density(100).luminosity(0).viscosity(100).sound(SoundEvents.BUCKET_FILL).overlay(LIQUID_OVERLAY)
            .color(0xFF999206)).slopeFindDistance(1).levelDecreasePerBlock(1).block(() -> FluidInit.CREOSOTE_BLOCK.get())
            .bucket(() -> ItemInit.CREOSOTE_BUCKET.get());

    // Liquid Blocks
    public static final RegistryObject<FlowingFluidBlock> CREOSOTE_BLOCK = BlockInit.BLOCKS.register("creosote",
            () -> new FlowingFluidBlock(FluidInit.CREOSOTE_FLUID,
                    AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100f).noDrops()));
}

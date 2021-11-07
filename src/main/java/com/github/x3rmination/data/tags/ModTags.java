package com.github.x3rmination.data.tags;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModTags {

    public static final Map<String, ITag.INamedTag<Block>> blockTagLibrary = new HashMap<>();
    public static final Map<String, ITag.INamedTag<Item>> itemTagLibrary = new HashMap<>();

    public static final class Blocks{

        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Block> mod(String path) {
            return BlockTags.bind(new ResourceLocation(X3TECH.MOD_ID, path).toString());
        }

        public void createBlockTags() {
            for (MaterialBase materialBase : MaterialRegistry.materialList) {
                ITag.INamedTag<Block> blockTag = forge("blocks/" + materialBase.getName());
                blockTagLibrary.put(materialBase.getName() + "_block", blockTag);

                if(materialBase.materialHasOre()){
                    ITag.INamedTag<Block> oreTag = forge("ores/" + materialBase.getName());
                    blockTagLibrary.put(materialBase.getName() + "_ore", oreTag);
                }
            }
        }
    }



    public static final class Items{

        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.bind(new ResourceLocation(X3TECH.MOD_ID, path).toString());
        }

        public void createItemTags() {
            for (MaterialBase materialBase : MaterialRegistry.materialList) {
                ITag.INamedTag<Item> blockItemTag = forge("blocks/" + materialBase.getName());
                itemTagLibrary.put(materialBase.getName() + "_block", blockItemTag);

                if(materialBase.materialHasOre()){
                    ITag.INamedTag<Item> oreItemTag = forge("ores/" + materialBase.getName());
                    itemTagLibrary.put(materialBase.getName() + "_ore", oreItemTag);
                }

                ITag.INamedTag<Item> ingotTag = forge("ingots/" + materialBase.getName());
                itemTagLibrary.put(materialBase.getName() + "_ingot", ingotTag);

                ITag.INamedTag<Item> nuggetTag = forge("nuggets/" + materialBase.getName());
                itemTagLibrary.put(materialBase.getName() + "_nugget", nuggetTag);
            }
        }
    }
}

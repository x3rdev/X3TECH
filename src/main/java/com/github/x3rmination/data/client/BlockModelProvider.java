package com.github.x3rmination.data.client;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.MaterialInit.MBlockInit;
import com.github.x3rmination.core.material.MaterialUtil.MaterialBase;
import com.github.x3rmination.core.material.MaterialUtil.MaterialRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends BlockStateProvider {

    public BlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, X3TECH.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MBlockBuilder(materialBase.getName());
        }
    }

    private void MBlockBuilder(String name) {
//        models().singleTexture(name + "_block", mcLoc("cube")
        simpleBlock(MBlockInit.blocksLibrary.get(name), models().singleTexture(name + "_block", mcLoc("cube"), "all", ResourceLocation.tryParse("x3tech:block/base_block"))
                .element().face(Direction.NORTH).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.SOUTH).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.EAST).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.WEST).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.UP).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.DOWN).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .end().texture("particle", "#all"));

    }
}

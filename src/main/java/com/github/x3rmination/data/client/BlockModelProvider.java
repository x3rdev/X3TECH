package com.github.x3rmination.data.client;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.materialinit.MBlockInit;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
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
            if(materialBase.materialHasOre()){
                MOreBlockBuilder(materialBase.getName());
            }
        }
    }

    private void MBlockBuilder(String name) {
        simpleBlock(MBlockInit.blockLibrary.get(name + "_block"), models().singleTexture(name + "_block", mcLoc("cube_all"), "all", ResourceLocation.tryParse("x3tech:block/base_block"))
                .element()
                .face(Direction.NORTH).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.SOUTH).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.EAST).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.WEST).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.UP).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .face(Direction.DOWN).tintindex(0).uvs(0,0,16,16).texture("#all").end()
                .end().texture("particle", "#all"));

    }

    private void MOreBlockBuilder(String name) {
        simpleBlock(MBlockInit.blockLibrary.get(name + "_ore"), models().singleTexture(name + "_ore", mcLoc("cube_all"), "texture", ResourceLocation.tryParse("minecraft:block/stone"))
                .element()
                .face(Direction.NORTH).uvs(0,0,16,16).texture("#texture").end()
                .face(Direction.SOUTH).uvs(0,0,16,16).texture("#texture").end()
                .face(Direction.EAST).uvs(0,0,16,16).texture("#texture").end()
                .face(Direction.WEST).uvs(0,0,16,16).texture("#texture").end()
                .face(Direction.UP).uvs(0,0,16,16).texture("#texture").end()
                .face(Direction.DOWN).uvs(0,0,16,16).texture("#texture").end()
                .end()
                .element()
                .face(Direction.NORTH).tintindex(0).uvs(0,0,16,16).texture("#overlay").end()
                .face(Direction.SOUTH).tintindex(0).uvs(0,0,16,16).texture("#overlay").end()
                .face(Direction.EAST).tintindex(0).uvs(0,0,16,16).texture("#overlay").end()
                .face(Direction.WEST).tintindex(0).uvs(0,0,16,16).texture("#overlay").end()
                .face(Direction.UP).tintindex(0).uvs(0,0,16,16).texture("#overlay").end()
                .face(Direction.DOWN).tintindex(0).uvs(0,0,16,16).texture("#overlay").end()
                .end()
                .texture("overlay", "x3tech:block/base_ore")
                .texture("particle", "#overlay"));
    }
}

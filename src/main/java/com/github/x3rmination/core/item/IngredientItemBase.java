package com.github.x3rmination.core.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class IngredientItemBase extends Item {

    public IngredientItemBase(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(new TranslationTextComponent("tooltip.ingredient_item_base.default").setStyle(Style.EMPTY.withItalic(true).withColor(Color.fromRgb(0x3e4366))));
    }
}

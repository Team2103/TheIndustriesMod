package com.maks2103.industries.item;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.registry.ModGuis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemResearchBook extends Item {
    public ItemResearchBook() {
        setRegistryName("research_book");
        setUnlocalizedName("research_book");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        player.openGui(IndustriesMod.getInstance(), ModGuis.RESEARCH_BOOK.getId(), world, pos.getX(), pos.getY(), pos.getZ());
        return EnumActionResult.SUCCESS;
    }
}

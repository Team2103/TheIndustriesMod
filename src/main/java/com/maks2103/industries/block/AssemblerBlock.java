package com.maks2103.industries.block;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.registry.ModGuis;
import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AssemblerBlock extends BlockContainer {
    public AssemblerBlock(Material materialIn) {
        super(materialIn);
        setUnlocalizedName("assembler_block");
        setRegistryName("assembler_block");
        setCreativeTab(IndustriesMod.CREATIVE_TAB);
        isBlockContainer = true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new AssemblerTileEntity();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity != null) {
                tileEntity.markDirty();
                IBlockState blockState = worldIn.getBlockState(pos);
                worldIn.notifyBlockUpdate(pos, blockState, blockState, 3);
            }
            playerIn.openGui(IndustriesMod.getInstance(), ModGuis.ASSEMBLER.getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
}

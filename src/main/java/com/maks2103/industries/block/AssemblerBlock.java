package com.maks2103.industries.block;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.registry.ModGuis;
import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public final class AssemblerBlock extends BlockContainer {
    public AssemblerBlock() {
        super(Material.IRON);
        setUnlocalizedName("assembler_block");
        setRegistryName("assembler_block");
        setCreativeTab(IndustriesMod.CREATIVE_TAB);
        isBlockContainer = true;

        setHardness(15);
        setResistance(15);
        setHarvestLevel("pickaxe", ItemTool.ToolMaterial.STONE.getHarvestLevel());
        setSoundType(SoundType.METAL);
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        AssemblerTileEntity tileEntity = new AssemblerTileEntity();
        tileEntity.setState(AssemblerTileEntity.State.READY);
        return tileEntity;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            AssemblerTileEntity tileEntity = (AssemblerTileEntity) worldIn.getTileEntity(pos);
            if(tileEntity != null) {
                tileEntity.sync();

                if(tileEntity.getState() == AssemblerTileEntity.State.READY)
                    playerIn.openGui(IndustriesMod.getInstance(), ModGuis.ASSEMBLER.getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
                else if(tileEntity.getState() == AssemblerTileEntity.State.DONE)
                    playerIn.openGui(IndustriesMod.getInstance(), ModGuis.ASSEMBLER_OUTPUT.getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        AssemblerTileEntity tileEntity = (AssemblerTileEntity) worldIn.getTileEntity(pos);
        if(tileEntity != null)
            tileEntity.dropAllItems();
        super.breakBlock(worldIn, pos, state);
    }
}

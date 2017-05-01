package com.maks2103.industries.assembler;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AssemblerRecipeManagerTest {
    private static final int ID = 100;
    private static final TestRecipe assemblerRecipe = new TestRecipe();

    @BeforeClass
    public static void setUp() throws Exception {
        AssemblerRecipeManager.addRecipe(assemblerRecipe);
    }

    @Test
    public void addNormalRecipe() throws Exception {
        AssemblerRecipeManager.addRecipe(new TestRecipe1());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addExistingRecipe() throws Exception {
        AssemblerRecipeManager.addRecipe(new TestRecipe());
    }

    @Test
    public void getForId() throws Exception {
        assertSame(AssemblerRecipeManager.getForId(ID), assemblerRecipe);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getRecipes() throws Exception {
        assertTrue(AssemblerRecipeManager.getRecipes().contains(assemblerRecipe));
        AssemblerRecipeManager.getRecipes().add(assemblerRecipe);
    }

    @Test
    public void toAndFromNBT() throws Exception {
        NBTTagCompound compound = AssemblerRecipeManager.toNBT(assemblerRecipe);
        assertSame(AssemblerRecipeManager.fromNBT(compound), assemblerRecipe);
    }

    private static final class TestRecipe implements AssemblerRecipe {

        @Override
        public int getCraftingTime() {
            return 100;
        }

        @Nonnull
        @Override
        public ItemStack[] getCraftItems() {
            return new ItemStack[]{new ItemStack(Items.APPLE, 100), new ItemStack(Blocks.BRICK_BLOCK, 1)};
        }

        @Nonnull
        @Override
        public ItemStack getOutputItem() {
            return new ItemStack(Items.ARROW, 10);
        }

        @Nonnull
        @Override
        public AssemblerPreviewModel getPreviewModel() {
            return new AssemblerPreviewModel() {
                @SideOnly(Side.CLIENT)
                @Override
                public void render() {
                    //Ok
                }
            };
        }

        @Override
        public int getId() {
            return ID;
        }
    }

    private static final class TestRecipe1 implements AssemblerRecipe {

        @Override
        public int getCraftingTime() {
            return 100;
        }

        @Nonnull
        @Override
        public ItemStack[] getCraftItems() {
            return new ItemStack[]{new ItemStack(Items.APPLE, 100), new ItemStack(Blocks.BRICK_BLOCK, 1)};
        }

        @Nonnull
        @Override
        public ItemStack getOutputItem() {
            return new ItemStack(Items.ARROW, 10);
        }

        @Nonnull
        @Override
        public AssemblerPreviewModel getPreviewModel() {
            return new AssemblerPreviewModel() {
                @SideOnly(Side.CLIENT)
                @Override
                public void render() {
                    //Ok
                }
            };
        }

        @Override
        public int getId() {
            return ID + 1;
        }
    }
}
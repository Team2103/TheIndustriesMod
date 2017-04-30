package com.maks2103.industries.util;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UtilsTest {
    @BeforeClass
    public static void init() {
        Bootstrap.register();
    }

    @Test
    public void spliceItemStackList() throws Exception {
        List<ItemStack> list = Arrays.asList(new ItemStack(Items.APPLE, 10),
                new ItemStack(Items.ARROW, 90),
                new ItemStack(Items.APPLE, 100),
                new ItemStack(Blocks.BRICK_BLOCK, 10),
                ItemStack.EMPTY,
                new ItemStack(Blocks.AIR));
        List<ItemStack> n = Utils.spliceItemStackList(list);
        assertSame(n.get(0).getCount(), 110);
        assertSame(n.get(1).getCount(), 90);
        assertSame(n.get(2).getCount(), 10);

        assertEquals(n.get(0).getItem(), Items.APPLE);
        assertEquals(n.get(1).getItem(), Items.ARROW);
        assertEquals(n.get(2).getItem(), Item.getItemFromBlock(Blocks.BRICK_BLOCK));

        assertSame(n.size(), 3);
    }

    @Test
    public void allMatch() throws Exception {
        OreDictionary.registerOre("test", Items.APPLE);
        OreDictionary.registerOre("test", Items.ARROW);

        List<ItemStack> elements = Arrays.asList(new ItemStack(Items.APPLE, 10),
                new ItemStack(Blocks.BRICK_BLOCK, 1));
        List<ItemStack> craft = Arrays.asList(new ItemStack(Items.ARROW, 10),
                new ItemStack(Blocks.BRICK_BLOCK, 1));
        assertTrue(Utils.allMatch(elements, craft));
    }

    @Test
    public void containsAny() throws Exception {
        int[] a = new int[]{-1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] b = new int[]{10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        int[] c = new int[]{-1, -2, -3};
        int[] d = new int[]{-4, -5, -6};

        assertTrue(Utils.containsAny(a, b));
        assertTrue(Utils.containsAny(a, c));
        assertFalse(Utils.containsAny(a, d));
    }
}
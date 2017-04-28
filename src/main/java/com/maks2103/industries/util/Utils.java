package com.maks2103.industries.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private Utils() {
    }

    public static boolean allMatch(@Nonnull List<ItemStack> in, List<ItemStack> out) {
        List<ItemStack> fixedIn = fixItemStackList(in);
        List<ItemStack> fixedOut = fixItemStackList(out);

        for(ItemStack itemStack : fixedIn) {
            boolean ok = false;
            for(ItemStack stack : fixedOut) {
                int[] ids = OreDictionary.getOreIDs(itemStack);
                int[] stackIds = OreDictionary.getOreIDs(stack);
                if(containsAny(ids, stackIds)) {
                    fixedIn.remove(itemStack);
                    fixedOut.remove(stack);
                    ok = true;
                }
            }
            if(!ok) {
                return false;
            }
        }
        return true;
    }

    static boolean containsAny(int[] first, int[] second) {
        for(int i : first) {
            for(int j : second) {
                if(i == j)
                    return true;
            }
        }
        return false;
    }

    static List<ItemStack> fixItemStackList(List<ItemStack> inSrc) {
        List<ItemStack> in = new ArrayList<>(inSrc);
        boolean containsDuplicates = true;
        while(containsDuplicates) {
            containsDuplicates = false;
            for(int i = 0; i < in.size(); i++) {
                ItemStack stack = in.get(i);
                for(int j = 0; j < in.size(); j++) {
                    if(i == j) {
                        continue;
                    }
                    ItemStack stack1 = in.get(j);
                    if(stack.getItem().equals(stack1.getItem())) {
                        stack.setCount(stack.getCount() + stack1.getCount());
                        in.remove(stack1);
                        containsDuplicates = true;
                    }
                }
            }
        }
        return in;
    }
}

package com.maks2103.industries.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public final class Utils {
    private Utils() {
    }

    public static boolean allMatch(@Nonnull List<ItemStack> actual, List<ItemStack> excepted) {
        List<ItemStack> fixedIn = spliceItemStackList(actual);
        List<ItemStack> fixedOut = spliceItemStackList(excepted);

        ListIterator<ItemStack> inIterator = fixedIn.listIterator();
        ListIterator<ItemStack> outIterator = fixedOut.listIterator();

        while(inIterator.hasNext()) {
            ItemStack inElem = inIterator.next();
            int[] inElemOreDictIds = OreDictionary.getOreIDs(inElem);
            boolean ok = false;
            while(outIterator.hasNext()) {
                ItemStack outElem = outIterator.next();
                int[] outElemOreDictIds = OreDictionary.getOreIDs(outElem);
                if((inElem.getItem() == outElem.getItem() || containsAny(inElemOreDictIds, outElemOreDictIds)) && inElem.getCount() >= outElem.getCount()) {
                    inIterator.remove();
                    outIterator.remove();
                    ok = true;
                    break;
                }
            }
            if(!ok) return false;
        }
        return true;
//
//        for(ItemStack itemStack : fixedIn) {
//            boolean ok = false;
//            for(ItemStack stack : fixedOut) {
//                int[] ids = OreDictionary.getOreIDs(itemStack);
//                int[] stackIds = OreDictionary.getOreIDs(stack);
//                if(itemStack.getItem() == stack.getItem() || containsAny(ids, stackIds)) {
//                    fixedIn.remove(itemStack);
//                    fixedOut.remove(stack);
//                    ok = true;
//                }
//            }
//            if(!ok) {
//                return false;
//            }
//        }
//        return true;
    }

    public static boolean containsAny(int[] first, int[] second) {
        for(int i : first) {
            for(int j : second) {
                if(i == j)
                    return true;
            }
        }
        return false;
    }

    public static List<ItemStack> spliceItemStackList(List<ItemStack> inSrc) {
        List<ItemStack> in = inSrc.stream()
                .map(ItemStack::copy)
                .filter(itemStack -> !itemStack.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
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

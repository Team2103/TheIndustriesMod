package com.maks2103.industries.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

final class OreDictWriter {
    private static final Queue<Pair<String, Object>> registerQueue = new ConcurrentLinkedDeque<>();
    private static final AtomicBoolean isRegistered = new AtomicBoolean(false);

    private OreDictWriter() {
    }

    static void add(String name, Item item) {
        if(isRegistered.get())
            OreDictionary.registerOre(name, item);
        else
            registerQueue.add(Pair.of(name, item));
    }

    static void add(String name, Block block) {
        if(isRegistered.get())
            OreDictionary.registerOre(name, block);
        else
            registerQueue.add(Pair.of(name, block));
    }

    static void add(String name, ItemStack itemStack) {
        if(isRegistered.get())
            OreDictionary.registerOre(name, itemStack);
        else
            registerQueue.add(Pair.of(name, itemStack));
    }

    static void register() {
        while(!registerQueue.isEmpty()) {
            Pair<String, Object> pair = registerQueue.poll();
            Object elem = pair.getValue();
            if(elem instanceof Item) {
                OreDictionary.registerOre(pair.getKey(), (Item) elem);
            } else if(elem instanceof Block) {
                OreDictionary.registerOre(pair.getKey(), (Block) elem);
            } else if(elem instanceof ItemStack) {
                OreDictionary.registerOre(pair.getKey(), (ItemStack) elem);
            }
        }
        isRegistered.set(true);
    }
}

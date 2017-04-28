package com.maks2103.industries.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ListenableItemStackHandler extends ItemStackHandler {
    private Listener listener = null;

    public ListenableItemStackHandler() {
    }

    public ListenableItemStackHandler(int size) {
        super(size);
    }

    public ListenableItemStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onLoad() {
        if(listener != null)
            listener.onLoad();
    }

    @Override
    protected void onContentsChanged(int slot) {
        if(listener != null)
            listener.onContentsChanged(slot);
    }

    public interface Listener {
        void onLoad();

        void onContentsChanged(int slot);
    }
}

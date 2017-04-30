package com.maks2103.industries.assembler;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ThreadSafe
public final class AssemblerRecipeManager {
    private static final TIntObjectHashMap<AssemblerRecipe> recipes = new TIntObjectHashMap<AssemblerRecipe>();

    private AssemblerRecipeManager() {
    }

    public static void addRecipe(@Nonnull AssemblerRecipe assemblerRecipe) {
        if(recipes.containsKey(assemblerRecipe.getId())) {
            throw new IllegalArgumentException("Assembler recipe id " + assemblerRecipe.getId() + " already used!");
        }
        recipes.put(assemblerRecipe.getId(), assemblerRecipe);
    }

    @Nullable
    public static AssemblerRecipe getForId(int id) {
        return recipes.get(id);
    }

    @Nonnull
    public static List<AssemblerRecipe> getRecipes() {
        return Collections.unmodifiableList(new ArrayList<>(recipes.valueCollection()));
    }

    @Nonnull
    public static NBTTagCompound toNBT(@Nullable AssemblerRecipe recipe) {
        if(recipe == null) {
            return new NBTTagCompound();
        }
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("id", recipe.getId());
        return compound;
    }

    @Nullable
    public static AssemblerRecipe fromNBT(@Nonnull NBTTagCompound compound) {
        if(compound.hasKey("id"))
            return getForId(compound.getInteger("id"));
        else
            return null;
    }
}

package com.maks2103.industries.assembler;

import gnu.trove.map.hash.TIntObjectHashMap;

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

    private List<AssemblerRecipe> getRecipes() {
        return Collections.unmodifiableList(new ArrayList<AssemblerRecipe>(recipes.valueCollection()));
    }
}

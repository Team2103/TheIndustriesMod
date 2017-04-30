package com.maks2103.industries.assembler;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface AssemblerPreviewModel {
    @SideOnly(Side.CLIENT)
    void render();
}

package com.maks2103.industries.assembler;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class used by {@link com.maks2103.industries.gui.AssemblerGui} for render crafting recipe preview
 */
@SideOnly(Side.CLIENT)
public interface AssemblerPreviewModel {
    /**
     * Render preview. This method will be called after matrix translated on left top corner of preview screen.
     * This method will be called in new matrix
     */
    @SideOnly(Side.CLIENT)
    void render();
}

package org.libreflock.opencoolshit.common.item;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.item.ItemStack;

public class SocTemplate extends BaseItem {

    public SocTemplate(Properties properties) {
            super(properties);
            lore = "Template for creating SoC's\nI would make some joke here, but im too stoopid";
        }
    
    @Override
    public List<Pair<String, String>> getData(ItemStack stack) {
        throw new UnsupportedOperationException("Unimplemented method 'getData'");
    }

    @Override
    public int getTier(ItemStack stack) {
        String[] segs = stack.getItem().getRegistryName().toString().split("_");
        return Integer.valueOf(segs[segs.length-1]);
    }
    
}

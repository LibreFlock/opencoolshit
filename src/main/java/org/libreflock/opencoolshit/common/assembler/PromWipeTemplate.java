package org.libreflock.opencoolshit.common.assembler;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class PromWipeTemplate {

    public static boolean select(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_prom");
    }

    public static Object[] validate(IInventory inv){    
        return new Object[]{true, new StringTextComponent("Ready for wipe!")};
    }

    public static Object[] assemble(IInventory inv) {
        return new Object[]{};
    }
}

package org.libreflock.opencoolshit.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

public class CreativeTabs {
    public static final ItemGroup OSSM = new Tab("OpenSolidState", Items.FLASH);

    public static class Tab extends ItemGroup {
        private RegistryObject<Item> icon;

        @SuppressWarnings("unchecked")
        public Tab(String label, @SuppressWarnings("rawtypes") RegistryObject icon){
            super(label);
            this.icon = icon;
        }

        @Override
        public ItemStack makeIcon(){
        return this.icon.get().getDefaultInstance();
        }
    }
}

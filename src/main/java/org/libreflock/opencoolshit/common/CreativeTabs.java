package org.libreflock.opencoolshit.common;

import org.libreflock.opencoolshit.common.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

public class CreativeTabs {
    public static final ItemGroup OSSM = new Tab("OpenSolidState", Items.ITEMGROUP);

    public static class Tab extends ItemGroup {
        private RegistryObject<Item> icon;

        public Tab(String label, RegistryObject<Item> icon){
        super(label);
        this.icon = icon;
        }

        @Override
        public ItemStack makeIcon(){
        return this.icon.get().getDefaultInstance();
        }
    }
}

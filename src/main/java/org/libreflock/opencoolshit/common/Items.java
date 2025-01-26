package org.libreflock.opencoolshit.common;

import org.libreflock.opencoolshit.common.item.Flash;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "opencoolshit");

    // HORRIFIC but im too stupid to do it properly.. maybe this isn't horrific
    public static RegistryObject<Item> FLASH_0 = ITEMS.register("ossm_flash_0", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM), 0));
    public static RegistryObject<Item> FLASH_1 = ITEMS.register("ossm_flash_1", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM), 1));
    public static RegistryObject<Item> FLASH_2 = ITEMS.register("ossm_flash_2", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM), 2));
}

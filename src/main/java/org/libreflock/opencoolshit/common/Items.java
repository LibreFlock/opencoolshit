package org.libreflock.opencoolshit.common;

import org.libreflock.opencoolshit.common.item.Eeprom;
import org.libreflock.opencoolshit.common.item.Flash;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "opencoolshit");

    // HORRIFIC but im too stupid to do it properly.. maybe this isn't horrific
    public static RegistryObject<Item> FLASH_0 = ITEMS.register("ossm_flash_0", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> FLASH_1 = ITEMS.register("ossm_flash_1", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> FLASH_2 = ITEMS.register("ossm_flash_2", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM)));

    public static RegistryObject<Item> EEPROM_0 = ITEMS.register("ossm_prom_0", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> EEPROM_1 = ITEMS.register("ossm_prom_1", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> EEPROM_2 = ITEMS.register("ossm_prom_2", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));

    public static RegistryObject<Item> ITEMGROUP = ITEMS.register("ossm_item_group", () -> new Item(new Item.Properties()));
}

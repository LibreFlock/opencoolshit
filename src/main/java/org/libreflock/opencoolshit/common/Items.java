package org.libreflock.opencoolshit.common;

import org.libreflock.opencoolshit.common.item.Eeprom;
import org.libreflock.opencoolshit.common.item.Flash;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "opencoolshit");

    // TODO: no
    public static RegistryObject<Item> FLASH_0 = ITEMS.register("ossm_flash_0", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> FLASH_1 = ITEMS.register("ossm_flash_1", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> FLASH_2 = ITEMS.register("ossm_flash_2", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM)));

    public static RegistryObject<Item> EEPROM_0 = ITEMS.register("ossm_prom_0", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> EEPROM_1 = ITEMS.register("ossm_prom_1", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> EEPROM_2 = ITEMS.register("ossm_prom_2", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));

    public static RegistryObject<Item> EEPROM_UPGR_0 = ITEMS.register("ossm_prom_upgrade_0", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> EEPROM_UPGR_1 = ITEMS.register("ossm_prom_upgrade_1", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> EEPROM_UPGR_2 = ITEMS.register("ossm_prom_upgrade_2", () -> new Eeprom(new Item.Properties().tab(CreativeTabs.OSSM)));

    public static RegistryObject<Item> SOC_TEMPLATE_0 = ITEMS.register("ossm_soc_template_0", () -> new Item(new Item.Properties().tab(CreativeTabs.OSSM)));
    public static RegistryObject<Item> SOC_0 = ITEMS.register("ossm_soc_0", () -> new Item(new Item.Properties()));

    public static RegistryObject<Item> ITEMGROUP = ITEMS.register("ossm_item_group", () -> new Item(new Item.Properties()));
}

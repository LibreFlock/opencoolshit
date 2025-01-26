package org.libreflock.opencoolshit.common;

import org.libreflock.opencoolshit.common.item.Flash;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "opencoolshit");

    // HORRIFIC but im too stupid to do it properly
    public static RegistryObject<Flash> FLASH = ITEMS.register("ossm_flash", () -> new Flash(new Item.Properties().tab(CreativeTabs.OSSM)));
}

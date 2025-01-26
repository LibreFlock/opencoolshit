package org.libreflock.opencoolshit.common.item;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.RegistryEvent;

import org.libreflock.opencoolshit.common.utils.Utils;

public abstract class BaseItem extends Item {

    public BaseItem(Properties properties) {
        super(properties);
    }

    public abstract List<Pair<String, String>> getData(ItemStack stack);
    public abstract int getTier(ItemStack stack);

    public String lore;

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Utils.getRarity(getTier(stack));
    }

    public abstract void register(RegistryEvent.Register<Item> event);

    
}

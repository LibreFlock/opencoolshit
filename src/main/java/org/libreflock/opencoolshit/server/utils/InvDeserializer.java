package org.libreflock.opencoolshit.server.utils;

import java.util.ArrayList;
// import java.util.ArrayList;
import java.util.List;

import org.libreflock.opencoolshit.OpenCoolshit;

import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
// import net.minecraft.inventory.Inventory;
// import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
// import net.minecraft.nbt.CompoundNBT;
// import net.minecraft.nbt.ListNBT;
// import net.minecraft.util.ResourceLocation;
// import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.items.ItemStackHandler;
// import net.minecraftforge.registries.ForgeRegistries;

public class InvDeserializer {
    public class InvEntry {
        public ItemStack stack;
        public ManagedEnvironment env;
        public DriverItem driver;

        public InvEntry(ItemStack stack, ManagedEnvironment env, DriverItem driver) {
            this.stack = stack;
            this.env = env;
            this.driver = driver;
        }
    }

    public List<InvEntry> items = new ArrayList<>();

    public InvDeserializer(ItemStack stack, EnvironmentHost host) {
        ItemStackHandler inv = new ItemStackHandler();
        inv.deserializeNBT(stack.getTag().getCompound("components"));
        OpenCoolshit.LOGGER.info("deserializing");

        for (int i=0; i<inv.getSlots(); i++) {
            ItemStack stk = inv.getStackInSlot(i);
            if (stk.isEmpty()) continue;
            OpenCoolshit.LOGGER.info("Working on: {}", stk.toString());
            DriverItem driver = Driver.driverFor(stk);
            ManagedEnvironment env = driver.createEnvironment(stk, host);
            if (env==null) OpenCoolshit.LOGGER.warn("This shit has no env! {}", stk.toString());
            items.add(new InvEntry(stk, env, driver));
        }
    }
}

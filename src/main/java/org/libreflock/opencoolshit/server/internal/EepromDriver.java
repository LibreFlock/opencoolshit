package org.libreflock.opencoolshit.server.internal;

import org.libreflock.opencoolshit.server.components.Eeprom;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class EepromDriver extends DriverItem {

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_prom_");
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        this.dataTag(stack); // this is still probably not intended but i Do not Care:tm:
        return new Eeprom(tier(stack), host, this, stack);
    }

    @Override
    public int tier(ItemStack stack) {
        return ((org.libreflock.opencoolshit.common.item.Eeprom)stack.getItem()).getTier(stack);
    }

    @Override
    public String slot(ItemStack stack) {
        String[] segs = stack.getItem().getRegistryName().toString().split("_");
        switch(segs[segs.length-2]) {
            case "upgrade":
                return Slot.Upgrade;
            default:
                return Slot.HDD;
        }
    }

    @Override
    public CompoundNBT dataTag(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }
        CompoundNBT nbt = stack.getTag();
        // This is the suggested key under which to store item component data.
        // You are free to change this as you please.
        // This is the suggested key under which to store item component data.
        // You are free to change this as you please.
        if (!nbt.contains("oc:data")) {
            nbt.put("oc:data", new CompoundNBT());
        }
        return nbt.getCompound("oc:data");
    }


    
}

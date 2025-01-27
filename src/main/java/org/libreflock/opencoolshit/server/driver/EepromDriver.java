package org.libreflock.opencoolshit.server.driver;

import org.libreflock.opencoolshit.server.components.Eeprom;

import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.item.ItemStack;

public class EepromDriver extends DriverItem {

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_prom_");
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        return new Eeprom(tier(stack), host, this, stack);
    }

    @Override
    public int tier(ItemStack stack) {
        return ((org.libreflock.opencoolshit.common.item.Eeprom)stack.getItem()).getTier(stack);
    }

    @Override
    public String slot(ItemStack stack) {
        return Slot.HDD;
    }

    
}

package org.libreflock.opencoolshit.server.driver;

// import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.server.components.Flash;
// import org.libreflock.opencoolshit.common.item;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.item.ItemStack;

public class FlashDriver extends DriverItem {

    // THESE two methods are definitely horrific though
    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_flash_");
    }

    @Override
    public int tier(ItemStack stack) {
        return ((org.libreflock.opencoolshit.common.item.Flash)stack.getItem()).getTier(stack);
    }

    @Override
    public String slot(ItemStack stack) {
        return Slot.Card;
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        return new Flash(tier(stack), host, this, stack);
    }

    
}

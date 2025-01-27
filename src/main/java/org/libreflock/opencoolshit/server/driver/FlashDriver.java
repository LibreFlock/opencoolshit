package org.libreflock.opencoolshit.server.driver;

// import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.server.components.Flash;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

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
        this.dataTag(stack);
        return new Flash(tier(stack), host, this, stack);
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

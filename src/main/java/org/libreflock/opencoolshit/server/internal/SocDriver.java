package org.libreflock.opencoolshit.server.internal;

// import java.util.ArrayList;
// import java.util.List;
import java.util.Collection;

import org.libreflock.opencoolshit.Settings;
import org.libreflock.opencoolshit.server.components.Soc;
import org.libreflock.opencoolshit.server.utils.InvDeserializer;

import li.cil.oc.api.Driver;
// import li.cil.oc.api.Driver;
// import li.cil.oc.api.Items;
import li.cil.oc.api.Machine;
import li.cil.oc.api.driver.item.CallBudget;
import li.cil.oc.api.driver.item.MutableProcessor;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.machine.Architecture;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
// import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
// import net.minecraft.nbt.ListNBT;
// import net.minecraft.util.ResourceLocation;
// import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistries;

public class SocDriver extends DriverItem implements MutableProcessor, CallBudget {

    private DriverItem DriverCPU;
    public SocDriver() {
        super();
        Driver.driverFor(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("opencomputers:cpu0"))));

    }

    @Override
    public boolean worksWith(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_soc_");
    }

    @Override
    public int tier(ItemStack stack) {
        // return ((org.libreflock.opencoolshit.common.item.Soc)stack.getItem()).getTier(stack);
        String[] segs = stack.getItem().getRegistryName().toString().split("_");
        return Integer.valueOf(segs[segs.length-1]); // didnt want to do this but i got forced by schizo java
    }

    @Override
    public int supportedComponents(ItemStack stack) {
        int[] comps = new int[]{Settings.COMMON.SOC_COMPONENTS_TIER1.get(), Settings.COMMON.SOC_COMPONENTS_TIER2.get(), Settings.COMMON.SOC_COMPONENTS_TIER3.get()};
        return comps[tier(stack)];
    }

    @Override
    public Class<? extends Architecture> architecture(ItemStack stack) {
        CompoundNBT tag = stack.getTag();

        // this sux 100% but who cares lol
        if (tag.contains("oc:archClass")) {
            try {
                return (Class<? extends Architecture>) Class.forName(tag.getString("oc:archClass"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                return (Class<? extends Architecture>) Class.forName("li.cil.oc.server.machine.luac.NativeLua53Architecture");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
        InvDeserializer inv = new InvDeserializer(stack, host);

        return new Soc(tier(stack), host, this, stack, inv.items); // TODO: tiers
    }

    @Override
    public String slot(ItemStack stack) {
        return Slot.CPU;
    }

    @Override
    public double getCallBudget(ItemStack stack) {

        int[] budgets = new int[]{Settings.COMMON.SOC_CALLBUDGET_TIER1.get(), Settings.COMMON.SOC_CALLBUDGET_TIER2.get(), Settings.COMMON.SOC_CALLBUDGET_TIER3.get()};
        return budgets[tier(stack)];
    }

    @Override
    public Collection<Class<? extends Architecture>> allArchitectures() {
        return Machine.architectures();
    }

    @Override
    public void setArchitecture(ItemStack stack, Class<? extends Architecture> architecture) {
        stack.getTag().putString("oc:archClass", architecture.getName());
    }

}

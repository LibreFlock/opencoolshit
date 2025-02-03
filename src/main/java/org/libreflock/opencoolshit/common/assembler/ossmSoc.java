package org.libreflock.opencoolshit.common.assembler;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.common.Items;
import li.cil.oc.api.IMC;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import li.cil.oc.api.Driver;


public class ossmSoc {
    public static int[] upgradeSlots = new int[]{};
    public static int[] containerSlots = new int[]{};
    public static List<Pair<String,Integer>> cardSlots = Arrays.asList(
        Pair.of(Slot.None, 0),
        Pair.of(Slot.HDD, 0), // Eeprom
        Pair.of(Slot.None, 0),
        Pair.of(Slot.None, 0),
        Pair.of(Slot.CPU, 0),
        Pair.of(Slot.None, 0),
        Pair.of(Slot.None, 0),
        Pair.of(Slot.Card, 0)
    );

    public static String name = "ossm_soc";

    // @Override
    public static boolean select(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().equals("opencoolshit:ossm_soc_template_0");
    }

    // @Override
    public static Object[] validate(IInventory inv) {
        
        // OpenCoolshit.LOGGER.warn("PROM, CPU, cards: {} {} {} ",inv.getItem(14).toString(), inv.getItem(17).toString(), inv.getItem(20).toString());
        if (inv.getItem(17).isEmpty()) {
            return new Object[]{false, new StringTextComponent("No CPU!")};
        }

        if (!inv.getItem(14).isEmpty() && !inv.getItem(14).getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_prom_")) {
            return new Object[]{false, new StringTextComponent("No HDDs allowed!")};
        }

        if (!inv.getItem(14).isEmpty()) {
            return new Object[]{true, new StringTextComponent("ready!")};
        } else {
            return new Object[]{true, new StringTextComponent("PROM recommended.")};
        }
    }

    // @Override
    public static Object[] assemble(IInventory inv) {
        // ItemStack soc = new ItemStack()
        CompoundNBT nbt = new CompoundNBT();
        // ListNBT components = new ListNBT();
        ItemStackHandler handler = new ItemStackHandler();
        handler.setSize(inv.getContainerSize());
        for (int i=1;i<inv.getContainerSize();i++) {
            ItemStack item = inv.getItem(i);
            if((!item.isEmpty()) && (i != 17)) { // we're going to be the CPU, we dont want the CPU included in the components we're going to mount
                if(!item.hasTag()) { // this sucks but who cares
                    // OpenCoolshit.LOGGER.info("NO TAG FOUND, CREATING DATA");
                    Driver.driverFor(item).dataTag(item);
                    // OpenCoolshit.LOGGER.info("DATA TAG: {}", item.getTag());
                    ManagedEnvironment env = Driver.driverFor(item).createEnvironment(item, null);
                    env.saveData(item.getTag());
                    // OpenCoolshit.LOGGER.info("DATA TAG: {}", item.getTag());
                }

                handler.setStackInSlot(i, item);

                // OpenCoolshit.LOGGER.info("MAKING FUNNY: {}, {}", item.getItem().getRegistryName().toString(), item.getTag());
                // CompoundNBT ser = new CompoundNBT();
                // ser.putString("id", item.getItem().getRegistryName().toString());
                // ser.putByte("Count", (byte)1);
                // ser.put("tag", item.getOrCreateTag());

                // components.add(ser);
            }
        }
        
        if (inv.getItem(17).hasTag()) {
            CompoundNBT data = inv.getItem(17).getTag();
            nbt.putString("oc:archClass", data.getString("oc:archClass"));
            nbt.putString("oc:archName", data.getString("archName"));
        }

        int cputier = Integer.valueOf(inv.getItem(17).getItem().getRegistryName().toString().substring("opencomputers:cpu".length())); // should also work with apu because same length, creative tiers will break tho

        nbt.put("components", handler.serializeNBT());
        nbt.putInt("oc:cputier", cputier);
        // nbt.put("oc:data", ocdata);
        

        ItemStack soc = new ItemStack(Items.SOC_0.get(), 1);
        OpenCoolshit.LOGGER.info("NBT: {}", nbt.toString());
        soc.setTag(nbt);
        return new Object[]{soc}; // return the item gracefully
    }

    public static void register() {

        IMC.registerAssemblerTemplate(name,
            "org.libreflock.opencoolshit.common.assembler.ossmSoc.select",
            "org.libreflock.opencoolshit.common.assembler.ossmSoc.validate",
            "org.libreflock.opencoolshit.common.assembler.ossmSoc.assemble",
            null,
            containerSlots,
            upgradeSlots,
            cardSlots
        );
    }


}

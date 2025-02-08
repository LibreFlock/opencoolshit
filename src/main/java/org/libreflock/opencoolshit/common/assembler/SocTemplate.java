package org.libreflock.opencoolshit.common.assembler;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.common.Items;
import org.libreflock.opencoolshit.server.internal.SocHost;
import li.cil.oc.api.IMC;
import li.cil.oc.api.driver.item.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;


public class SocTemplate {
    public static int[][] upgradeSlots = new int[][]{
        {0},
        {1, 0},
        {2, 1}
    };
    // public static int[][] containerSlots = new int[][]{
    //     {},
    //     {},
    //     {},
    // };
    public static List<List<Pair<String,Integer>>> cardSlots = Arrays.asList(
        Arrays.asList(
            Pair.of(Slot.None, 0),
            Pair.of(Slot.HDD,0), // Eeprom
            Pair.of(Slot.None, 0),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.CPU, 0),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.Card, 0)
        ),
        Arrays.asList(
            Pair.of(Slot.None, 0),
            Pair.of(Slot.HDD, 1), // Eeprom
            Pair.of(Slot.None, 0),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.CPU, 1),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.Card, 1)
        ),
        Arrays.asList(
            Pair.of(Slot.None, 0),
            Pair.of(Slot.HDD, 2), // Eeprom
            Pair.of(Slot.None, 0),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.CPU, 2),
            Pair.of(Slot.None, 0),
            Pair.of(Slot.Card, 2),
            Pair.of(Slot.Card, 0)
        )
    );

    public static String name = "ossm_soc_";

    // @Override
    public static boolean select0(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().equals("opencoolshit:ossm_soc_template_0");
    }

    public static boolean select1(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().equals("opencoolshit:ossm_soc_template_1");
    }

    public static boolean select2(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().equals("opencoolshit:ossm_soc_template_2");
    }

    // @Override
    public static Object[] validate(IInventory inv) {
        
        // OpenCoolshit.LOGGER.warn("PROM, CPU, cards: {} {} {} ",inv.getItem(14).toString(), inv.getItem(17).toString(), inv.getItem(20).toString());
        if (inv.getItem(17).isEmpty()) {
            return new Object[]{false, new StringTextComponent("No CPU!").withStyle(TextFormatting.RED)};
        }

        if (!inv.getItem(14).isEmpty() && !inv.getItem(14).getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_prom_")) {
            return new Object[]{false, new StringTextComponent("No HDDs allowed!").withStyle(TextFormatting.RED)};
        }

        if (!inv.getItem(14).isEmpty()) {
            return new Object[]{true, new StringTextComponent("Ready!")};
        } else {
            return new Object[]{true, new StringTextComponent("Ready!"), new IFormattableTextComponent[]{new StringTextComponent("PROM recommended!").withStyle(TextFormatting.DARK_RED)}};
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
                // if(!item.hasTag()) { // this sucks but who cares
                //     // OpenCoolshit.LOGGER.info("NO TAG FOUND, CREATING DATA");
                //     Driver.driverFor(item).dataTag(item);
                //     // OpenCoolshit.LOGGER.info("DATA TAG: {}", item.getTag());
                //     // ManagedEnvironment env = Driver.driverFor(item).createEnvironment(item, new SocHost(null, ));
                //     // env.saveData(item.getTag());
                //     // OpenCoolshit.LOGGER.info("DATA TAG: {}", item.getTag());
                // }

                handler.setStackInSlot(i, item);

                // OpenCoolshit.LOGGER.info("MAKING FUNNY: {}, {}", item.getItem().getRegistryName().toString(), item.getTag());
                // CompoundNBT ser = new CompoundNBT();
                // ser.putString("id", item.getItem().getRegistryName().toString());
                // ser.putByte("Count", (byte)1);
                // ser.put("tag", item.getOrCreateTag());

                // components.add(ser);
            }
        }
        
        if (inv.getItem(17).hasTag() && inv.getItem(17).getTag().contains("oc:data")) {
            CompoundNBT data = inv.getItem(17).getTag().getCompound("oc:data");
            // OpenCoolshit.LOGGER.info("CPU NBT: {}", data.toString());
            if (nbt.contains("oc:archClass")) nbt.putString("oc:archClass", data.getString("oc:archClass"));
            if (nbt.contains("oc:archName")) nbt.putString("oc:archName", data.getString("oc:archName"));
        }

        // int cputier = Integer.valueOf(inv.getItem(17).getItem().getRegistryName().toString().substring("opencomputers:cpu".length())); // should also work with apu because same length, creative tiers will break tho
        
        String[] segs = inv.getItem(0).getItem().getRegistryName().toString().split("_");
        int soctier = Integer.valueOf(segs[segs.length-1]);

        Item item;
        switch(soctier) {
            case 0:
                item = Items.SOC_0.get();
                break;
            case 1:
                item = Items.SOC_1.get();
                break;
            case 2:
                item = Items.SOC_2.get();
                break;
            default:
                item = Items.SOC_0.get();
                break;
        }

        nbt.put("components", handler.serializeNBT());
        nbt.putString("oc:cpu", inv.getItem(17).getItem().getRegistryName().toString()); // might or might not use
        

        ItemStack soc = new ItemStack(item, 1);
        OpenCoolshit.LOGGER.info("NBT: {}", nbt.toString());
        soc.setTag(nbt);
        return new Object[]{soc}; // return the item gracefully
    }

    public static void register() {

        // Tier 1
        IMC.registerAssemblerTemplate(name+"0",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.select0",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.validate",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.assemble",
            SocHost.class,
            new int[]{},
            upgradeSlots[0],
            cardSlots.get(0)
        );

        // Tier 2
        IMC.registerAssemblerTemplate(name+"1",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.select1",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.validate",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.assemble",
            SocHost.class,
            new int[]{},
            upgradeSlots[1],
            cardSlots.get(1)
        );

        // Tier 3
        IMC.registerAssemblerTemplate(name+"2",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.select2",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.validate",
            "org.libreflock.opencoolshit.common.assembler.SocTemplate.assemble",
            SocHost.class,
            new int[]{},
            upgradeSlots[2],
            cardSlots.get(2)
        );
    }


}

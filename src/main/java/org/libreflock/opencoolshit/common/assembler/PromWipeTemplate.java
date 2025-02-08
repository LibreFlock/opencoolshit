package org.libreflock.opencoolshit.common.assembler;
import org.apache.commons.lang3.tuple.Pair;
import org.libreflock.opencoolshit.Settings;
import org.libreflock.opencoolshit.common.Items;
import org.libreflock.opencoolshit.common.item.Eeprom;
import org.libreflock.opencoolshit.server.utils.StorageDeviceManager;
import li.cil.oc.api.IMC;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import java.util.ArrayList;

public class PromWipeTemplate {

    public static boolean select(ItemStack stack) {
        return stack.getItem().getRegistryName().toString().startsWith("opencoolshit:ossm_prom");
    }

    public static Object[] validate(IInventory inv){    
        if ( inv.getItem(0).hasTag() && inv.getItem(0).getTag().contains("oc:data") && inv.getItem(0).getTag().getCompound("oc:data").contains("node")) {
            return new Object[]{true, new StringTextComponent("Ready for wipe!")};
        } else {
            return new Object[]{false, new StringTextComponent("This PROM is brand new!").withStyle(TextFormatting.RED)};
        }
    }

    public static Object[] assemble(IInventory inv) {
        ItemStack item = inv.getItem(0);
        String address = item.getTag().getCompound("oc:data").getCompound("node").getString("address");
        int[] sizes = new int[]{Settings.COMMON.EEPROM_SIZE_TIER1.get(), Settings.COMMON.EEPROM_SIZE_TIER2.get(),Settings.COMMON.EEPROM_SIZE_TIER3.get()};
        int tier = ((Eeprom)Items.EEPROM_0.get()).getTier(item);
        StorageDeviceManager sdev = new StorageDeviceManager(item, address, Settings.COMMON.EEPROM_BLOCKSIZE.get(), sizes[tier]);
        sdev.erase((byte)0xFF);
        return new Object[]{item};
    }

    public static void register() {

        // Tier 1
        IMC.registerAssemblerTemplate("ossm_prom",
            "org.libreflock.opencoolshit.common.assembler.PromWipeTemplate.select",
            "org.libreflock.opencoolshit.common.assembler.PromWipeTemplate.validate",
            "org.libreflock.opencoolshit.common.assembler.PromWipeTemplate.assemble",
            null,
            new int[]{},
            new int[]{},
            new ArrayList<Pair<String,Integer>>()
        );
    }
}

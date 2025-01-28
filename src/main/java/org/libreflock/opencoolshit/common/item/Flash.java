package org.libreflock.opencoolshit.common.item;

import java.util.Arrays;
import java.util.List;

import org.libreflock.opencoolshit.Settings;
// import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.common.Constants;
import org.libreflock.opencoolshit.common.utils.Utils;
// import org.libreflock.opencoolshit.server.driver.FlashDriver;

import com.mojang.datafixers.util.Pair;

// import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
// import net.minecraftforge.event.RegistryEvent.Register;

public class Flash extends BaseItem {

    public Flash(Properties properties) {
        super(properties);
        // this.tier = tier;
        lore = "A device with flash memory!\nBasically an eeprom but faster!";
    }
    
    // // public String ;
    
    // public int tier;

    @Override
    public List<Pair<String, String>> getData(ItemStack stack) {

        int[] sizes = new int[]{Settings.COMMON.FLASH_SIZE_TIER1.get(), Settings.COMMON.FLASH_SIZE_TIER2.get(), Settings.COMMON.FLASH_SIZE_TIER3.get()};

        int tier = getTier(stack);
        int blocks = sizes[tier];
        int blocksize = Settings.COMMON.FLASH_BLOCKSIZE.get();
        if (stack.getTagElement("oc:data").contains("blocks")) {
            blocks = stack.getTagElement("oc:data").getInt("blocks");
        }

        if (stack.getTagElement("oc:data").contains("blockSize")) {
            blocksize = stack.getTagElement("oc:data").getInt("blockSize");
        }

        return Arrays.asList(
            new Pair<String,String>(Constants.ObjectProperties.Storage, Utils.toBytes((double)(blocks*blocksize), 0)),
            new Pair<String,String>(Constants.ObjectProperties.SectorSize, Utils.toBytes((double)blocksize, 0))
        );
    }

    @Override
    public int getTier(ItemStack stack) {
        return Integer.valueOf(stack.getItem().getRegistryName().toString().substring("opencoolshit:ossm_flash_".length()));
    }


}

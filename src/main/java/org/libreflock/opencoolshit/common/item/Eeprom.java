package org.libreflock.opencoolshit.common.item;
import java.util.Arrays;
import java.util.List;
// import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.Settings;
import org.libreflock.opencoolshit.common.Constants;
import org.libreflock.opencoolshit.common.utils.Utils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.ItemStack;

public class Eeprom extends BaseItem {

    public Eeprom(Properties properties) {
        super(properties);
        lore = "A General Purpose PROM!\nTier 1 can't ELECTRICALLY erase.";
    }
    

    @Override
    public int getTier(ItemStack stack) {
        // return Integer.valueOf(stack.getItem().getRegistryName().toString().substring("opencoolshit:ossm_prom_".length()));
        String[] segs = stack.getItem().getRegistryName().toString().split("_");
        return Integer.valueOf(segs[segs.length-1]);
    }

    @Override
    public List<Pair<String, String>> getData(ItemStack stack) {

        int[] sizes = new int[]{Settings.COMMON.EEPROM_SIZE_TIER1.get(), Settings.COMMON.EEPROM_SIZE_TIER2.get(), Settings.COMMON.EEPROM_SIZE_TIER3.get()};

        int tier = getTier(stack);
        int blocks = sizes[tier];
        int blocksize = Settings.COMMON.EEPROM_BLOCKSIZE.get();
        if (stack.getTagElement("oc:data").contains("blocks")) {
            blocks = stack.getTagElement("oc:data").getInt("blocks");
        }

        if (stack.getTagElement("oc:data").contains("blockSize")) {
            blocksize = stack.getTagElement("oc:data").getInt("blockSize");
        }

        return Arrays.asList(
            new Pair<String,String>(Constants.ObjectProperties.Storage, Utils.toBytes((double)(blocks), 0)),
            new Pair<String,String>(Constants.ObjectProperties.SectorSize, Utils.toBytes((double)blocksize, 0))
        );
    }

    // @Override
    // public Rarity getRarity(ItemStack stack) {
    //     return Utils.getRarity(getTier(stack));
    // }
    
}

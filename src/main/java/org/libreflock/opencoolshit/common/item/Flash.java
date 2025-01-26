package org.libreflock.opencoolshit.common.item;

import java.util.Arrays;
import java.util.List;

import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.common.Constants;
import org.libreflock.opencoolshit.common.utils.Utils;
// import org.libreflock.opencoolshit.server.driver.FlashDriver;

import com.mojang.datafixers.util.Pair;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent.Register;

public class Flash extends BaseItem {

    public Flash(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }
    
    public String lore = "A flash device lets any device have writable memory! Perfect for saving your state!";
    public int tier;

    @Override
    public List<Pair<String, String>> getData(ItemStack stack) {
        // int tier = getTier(stack); // unused
        int blocks = 64; // TODO: make this part of config
        int blocksize = 64; // TODO: make this part of config
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

    @Override
    public void register(Register<Item> event) {
        OpenCoolshit.LOGGER.error("REGISTERED");
        // ItemModelsProperties.register(this, new ResourceLocation("opencoolshit:flash_0", "inventory"), null);
    }


}

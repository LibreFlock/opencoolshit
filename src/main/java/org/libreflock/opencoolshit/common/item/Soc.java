package org.libreflock.opencoolshit.common.item;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.server.internal.SocDriver;

import com.mojang.datafixers.util.Pair;

import li.cil.oc.api.Driver;
import li.cil.oc.api.Machine;
import li.cil.oc.api.machine.Architecture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class Soc extends BaseItem {

    private boolean bad = true; // bad means bad decisions, why does use get called twice :(

    public Soc(Properties properties) {
        super(properties);
        lore = "Allows you to integrate components\ndirectly into your CPU!";
    }

    @Override
    public String genLore(ItemStack stack) {
        SocDriver driver = (SocDriver)Driver.driverFor(stack);
        return lore+"\nMax components: §f"+String.valueOf(driver.supportedComponents(stack))+"§7\nArchitecture: §f"+Machine.getArchitectureName(driver.architecture(stack))+"§7";
    }
    

    @Override
    public int getTier(ItemStack stack) {
        // return Integer.valueOf(stack.getItem().getRegistryName().toString().substring("opencoolshit:ossm_prom_".length()));
        String[] segs = stack.getItem().getRegistryName().toString().split("_");
        return Integer.valueOf(segs[segs.length-1]);
    }


    @Override
    public List<Pair<String, String>> getData(ItemStack stack) {
        throw new UnsupportedOperationException("Unimplemented method 'getData'");
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (!bad) { bad = true; return ActionResult.pass(item); }
        OpenCoolshit.LOGGER.info("TESTICLES!");
        if (player.isCrouching()) {
            bad = false;
            OpenCoolshit.LOGGER.info("USED!");
            SocDriver driver = (SocDriver) Driver.driverFor(item);
            Collection<Class<? extends Architecture>> archs = driver.allArchitectures();
            Class<? extends Architecture> cpuarch = driver.architecture(item);
            Class<? extends Architecture> first = null;

            // this sucks, why did it HAVE to be a collection :(
            boolean found = false;
            for (Class<? extends Architecture> arch : archs) {
                if (first == null) {
                    first = arch;
                }

                if (found) {
                    driver.setArchitecture(item, arch);
                    player.sendMessage(new StringTextComponent("Architecture: "+Machine.getArchitectureName(driver.architecture(item))), UUID.randomUUID());
                    return ActionResult.success(item);
                }

                if (arch.equals(cpuarch)) {
                    found = true;
                }
            }
            driver.setArchitecture(item, first);
            player.sendMessage(new StringTextComponent("Architecture: "+Machine.getArchitectureName(driver.architecture(item))), UUID.randomUUID());
            return ActionResult.success(item);
        }
        return ActionResult.fail(item);
    }
    

    // @Override
    // public List<Pair<String, String>> getData(ItemStack stack) {

    //     int[] sizes = new int[]{Settings.COMMON.EEPROM_SIZE_TIER1.get(), Settings.COMMON.EEPROM_SIZE_TIER2.get(), Settings.COMMON.EEPROM_SIZE_TIER3.get()};

    //     int tier = getTier(stack);
    //     int blocks = sizes[tier];
    //     int blocksize = Settings.COMMON.EEPROM_BLOCKSIZE.get();
    //     if (stack.getTagElement("oc:data").contains("blocks")) {
    //         blocks = stack.getTagElement("oc:data").getInt("blocks");
    //     }

    //     if (stack.getTagElement("oc:data").contains("blockSize")) {
    //         blocksize = stack.getTagElement("oc:data").getInt("blockSize");
    //     }

    //     return Arrays.asList(
    //         new Pair<String,String>(Constants.ObjectProperties.Storage, Utils.toBytes((double)(blocks*blocksize), 0)),
    //         new Pair<String,String>(Constants.ObjectProperties.SectorSize, Utils.toBytes((double)blocksize, 0))
    //     );
    // }

    // @Override
    // public Rarity getRarity(ItemStack stack) {
    //     return Utils.getRarity(getTier(stack));
    // }
    
}

package org.libreflock.opencoolshit.server.components;

import java.util.HashMap;
import java.util.Map;

import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.Settings;
import org.libreflock.opencoolshit.server.internal.FlashDriver;
import org.libreflock.opencoolshit.server.utils.StorageDeviceManager;

import li.cil.oc.api.Network;

// import com.mojang.datafixers.types.templates.Product;

import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
// import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class Flash extends AbstractManagedEnvironment implements DeviceInfo{
    Node node = Network.newNode(this, Visibility.Neighbors).withComponent("ossm_flash", Visibility.Neighbors).withConnector().create();
    String uuid = "";
    StorageDeviceManager sdev;
    int capacity;

    int tier;
    ItemStack stack;

    public Flash(int tier, EnvironmentHost host, FlashDriver flashDriver, ItemStack stack) {
        setNode(node);
        this.stack = stack;
        this.tier = tier;
        // int[] sizes = new int[]{Settings.COMMON.FLASH_SIZE_TIER1.get(), Settings.COMMON.FLASH_SIZE_TIER2.get(),Settings.COMMON.FLASH_SIZE_TIER3.get()};
        // sdev = new StorageDeviceManager(stack, null, Settings.COMMON.FLASH_BLOCKSIZE.get(), sizes[tier]);
    }

    @Override
    public void onConnect(final Node node) {
        super.onConnect(node);
        loadData(this.stack.getTag());

        int[] sizes = new int[]{Settings.COMMON.EEPROM_SIZE_TIER1.get(), Settings.COMMON.EEPROM_SIZE_TIER2.get(),Settings.COMMON.EEPROM_SIZE_TIER3.get()};
        sdev = new StorageDeviceManager(stack, this.uuid, Settings.COMMON.EEPROM_BLOCKSIZE.get(), sizes[tier]);
        capacity = sdev.blks*sdev.blksize;
    }

    @Override
    public Map<String, String> getDeviceInfo() {
        return new HashMap<String, String>() {{
            put(DeviceInfo.DeviceAttribute.Class, DeviceInfo.DeviceClass.Disk);
            put(DeviceInfo.DeviceAttribute.Description, "Flash");
            put(DeviceInfo.DeviceAttribute.Vendor, "Shadow Kat Semiconductor");
            put(DeviceInfo.DeviceAttribute.Version, "Rev ${tier+1}");
            put(DeviceInfo.DeviceAttribute.Capacity, "$capacity");
            put(DeviceInfo.DeviceAttribute.Product, "FISH-${capacity/1024}");
        }};
    }

    @Callback(direct=true, doc = "readByte(offset:number):number -- Read a single byte at the specified offset.")
    public Object[] readByte(Context ctx, Arguments args) {
        int offset = args.checkInteger(0);
        if (offset < 1 || offset > sdev.blks*sdev.blksize) {
            return new Object[]{null, "invalid offset"};
        }

        byte[] blk = sdev.readBlk((offset-1)/sdev.blksize);
        return new Object[]{blk[(offset-1)%sdev.blksize]};
    }

    @Callback(direct = true, doc = "writeByte(offset:number, value:number):number -- Writes a single byte at the specified offset.")
    public Object[] writeByte(Context ctx, Arguments args) {
        int offset = args.checkInteger(0);
        int value = args.checkInteger(1);
        if (offset < 1 || offset > sdev.blks*sdev.blksize) {
            return new Object[]{null, "invalid offset"};
        }

        // byte[] blk = sdev.readBlk((offset - 1)/sdev.blksize);
        // blk[(offset - 1)%sdev.blksize] = (byte)(value & 0xFF);
        // sdev.writeBlk((offset - 1)/sdev.blksize, blk);
        sdev.writeBlk(offset-1, new byte[]{(byte)value});
        return new Object[]{};

    }
    @Callback(direct = true, doc = "readSector(offset:number):number -- Read the current contents of the specified sector.")
    public Object[] readSector(Context ctx, Arguments args) {
        int offset = args.checkInteger(0);
        if (offset < 1 || offset > sdev.blks*sdev.blksize) {
            return new Object[]{null, "invalid offset"};
        }
        byte[] blk = sdev.readBlk(offset-1);
        return new Object[]{blk};
    }


    @Callback(direct = true, doc = "writeSector(offset:number, value:string) -- Write the specified contents to the specified sector.")
    public Object[] writeSector(Context ctx, Arguments args) {
        int offset = args.checkInteger(0);
        byte[] data = args.checkByteArray(1);
        if (offset < 1 || offset > sdev.blks*sdev.blksize) {
            return new Object[]{null, "invalid offset"};
        }
        sdev.writeBlk((offset-1)*sdev.blksize, data);
        return new Object[]{};
    }

    @Callback(direct = true, doc = "getSectorSize():number -- Returns the size of a single sector on the drive, in bytes.")
    public Object[] getSectorSize(Context ctx, Arguments args) {
        return new Object[]{sdev.blksize};
    }

    @Callback(direct = true, doc = "getCapacity():number -- Returns the total capacity of the drive, in bytes.")
    public Object[] getCapacity(Context ctx, Arguments args) {
        return new Object[]{capacity};
    }

    @Override
    public void saveData(CompoundNBT nbt) {
        super.saveData(nbt);
        if (uuid == "") {
            if (node() != null) {
                if (node().address() == null) {
                    uuid = "";
                } else {
                    uuid = node().address();
                } // this literally never works (according to the original developer)
            }
            if (uuid == "") {
                uuid = nbt.getCompound("node").getString("address");
            }
            if (uuid != ""){
                sdev.uuid = uuid;}
            else
                OpenCoolshit.LOGGER.warn("node still has no uuid?");
        }
        // flush
    }

    @Override
    public void loadData(CompoundNBT nbt) {
        super.loadData(nbt);
        if (sdev == null) { return; } // this is so schizo

        if (uuid == "") {
            if (uuid == "") {
                if (node() != null) {
                    if (node().address() == null) {
                        uuid = "";
                    } else {
                        uuid = node().address();
                    } // this literally never works (according to the original developer) (it sorta does)
                }
                if (uuid == "") {
                    uuid = nbt.getCompound("oc:data").getCompound("node").getString("address");
                }
                if (uuid != "")
                    sdev.uuid = uuid;
                else
                    OpenCoolshit.LOGGER.warn("node still has no uuid?");
            }
        }
        if (uuid != "")
            sdev.uuid = uuid;
    }
    
}

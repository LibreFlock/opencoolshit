package org.libreflock.opencoolshit.server.components;

import java.util.HashMap;
import java.util.Map;

import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.server.driver.FlashDriver;
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

    public Flash(int tier, EnvironmentHost host, FlashDriver flashDriver, ItemStack stack) {
        setNode(node);
        sdev = new StorageDeviceManager(stack, null, 64, 64);
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
        OpenCoolshit.LOGGER.info("!!!!!!!!!!!!! READ, FIRST COUPLE BYTES: {}, {}, {}, {}, {}", (int)blk[0], (int)blk[1], (int)blk[2], (int)blk[3], (int)blk[4]);
        return new Object[]{blk[(offset-1)%sdev.blksize]};
    }

    @Callback(direct = true, doc = "writeByte(offset:number, value:number):number -- Writes a single byte at the specified offset.")
    public Object[] writeByte(Context ctx, Arguments args) {
        int offset = args.checkInteger(0);
        int value = args.checkInteger(1);
        if (offset < 1 || offset > sdev.blks*sdev.blksize) {
            return new Object[]{null, "invalid offset"};
        }

        // OpenCoolshit.LOGGER.info("WRITE BLOCK OFFSET >> {}", (offset - 1)/sdev.blksize);
        // byte[] blk = sdev.readBlk((offset - 1)/sdev.blksize);
        // blk[(offset - 1)%sdev.blksize] = (byte)(value & 0xFF);
        // sdev.writeBlk((offset - 1)/sdev.blksize, blk);
        sdev.writeBlk(offset-1, new byte[]{(byte)value});
        return new Object[]{};

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
        if (uuid == "") {
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

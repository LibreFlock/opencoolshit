package org.libreflock.opencoolshit.server.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.Settings;
import org.libreflock.opencoolshit.server.internal.EepromDriver;
import org.libreflock.opencoolshit.server.utils.StorageDeviceManager;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class Eeprom extends AbstractManagedEnvironment implements DeviceInfo {

    Node node;
    String uuid = "";
    StorageDeviceManager sdev;

    int tier;
    ItemStack stack;

    public Eeprom(int tier, EnvironmentHost host, EepromDriver eepromDriver, ItemStack stack) {
        node = Network.newNode(this, Visibility.Neighbors).withComponent("ossm_prom", Visibility.Neighbors).withConnector().create();
        setNode(node);
        this.stack = stack;
        this.tier = tier;

        // eraseable = tier > 0;
        // hc = tier > 1;
    }

    @Override
    public void onConnect(final Node node) {
        super.onConnect(node);
        loadData(this.stack.getTag());

        int[] sizes = new int[]{Settings.COMMON.EEPROM_SIZE_TIER1.get(), Settings.COMMON.EEPROM_SIZE_TIER2.get(),Settings.COMMON.EEPROM_SIZE_TIER3.get()};
        sdev = new StorageDeviceManager(stack, this.uuid, Settings.COMMON.EEPROM_BLOCKSIZE.get(), sizes[tier]);
    }

    @Override
    public Map<String, String> getDeviceInfo() {
        // TODO Auto-generated method stub
        return new HashMap<String,String>() {{
            put(DeviceInfo.DeviceAttribute.Class, DeviceInfo.DeviceClass.Disk);
            put(DeviceInfo.DeviceAttribute.Description, "PROM");
            put(DeviceInfo.DeviceAttribute.Vendor, "Shadow Kat Semiconductor");
            put(DeviceInfo.DeviceAttribute.Version, "Rev ${tier+1}");
            put(DeviceInfo.DeviceAttribute.Capacity, "$capacity");
            // put(DeviceInfo.DeviceAttribute.Product, "ROMCOM-${capacity/1024}${model_letters[formfactor]}${if (tier > 0) "K" else ""}");
            put(DeviceInfo.DeviceAttribute.Product, "ROMCOM-somethingsomething"); // TODO: FIX THIS
            put(DeviceInfo.DeviceAttribute.Clock, "${capacity/Settings.storage.eepromEEPROMTime}");
        }};
    }

    @Callback(direct = true, limit = 256, doc = "blockRead(block:number):string -- Returns the data in a block")
    public Object[] blockRead(Context ctx, Arguments args) {
        int blk = args.checkInteger(0);
        if (blk < 1 || blk > sdev.blks) {
            return new Object[]{"block out of bounds"};
        } else {
            return new Object[]{sdev.readBlk(blk-1)};
        }
    }

    @Callback(doc = "erase():boolean -- Erases the entire EEPROM")
    public Object[] erase(Context ctx, Arguments args) {
        if (tier == 0) {
            return new Object[]{false};
        }
        sdev.erase((byte)0xff);
        ctx.pause(Settings.COMMON.EEPROM_FLASH_TIME.get());
        return new Object[]{true};
    }

    @Callback(doc = "blockWrite(block:number, data:string):boolean -- Writes a block")
    public Object[] blockWrite(Context ctx, Arguments args) {
        int blk = args.checkInteger(0);
        byte[] data = args.checkByteArray(1);
        if (blk < 1 || blk > sdev.blks) {
            return new Object[]{"block out of bounds"};
        } else {
            if (tier==0) {
                byte[] readblk = sdev.readBlk((blk-1));
                byte[] empty = new byte[sdev.blksize];
                Arrays.fill(empty, (byte)0xFF);
                if (!Arrays.equals(readblk, empty)) {
                // if(!readblk.equals(empty)) {
                    return new Object[]{"block already written to"};
                }
            }
            sdev.writeBlk((blk-1)*sdev.blksize, data);
            ctx.pause(Settings.COMMON.EEPROM_FLASH_TIME.get()/sdev.blks);
            return new Object[]{true};
        }
    }

    @Callback(direct = true, doc = "numBlocks():number -- Returns the number of blocks.")
    public Object[] numBlocks(Context ctx, Arguments args) {
        return new Object[]{sdev.blks};
    }
    
    @Callback(direct = true, doc = "blockSize():number -- Returns the block size in bytes.")
    public Object[] blockSize(Context ctx, Arguments args) {
        return new Object[]{sdev.blksize};
    } 

    @Callback(direct = true, doc = "hasElectronicErase():boolean -- Returns if the PROM has electronic erase or not")
    public Object[] hasElectronicErase(Context ctx, Arguments args) {
        return new Object[]{tier > 0};
    }

    @Callback(direct = true, doc = "hc():boolean -- Returns if the PROM is a high capacity model")
    public Object[] hc(Context ctx, Arguments args) {
        return new Object[]{tier > 1}; 
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

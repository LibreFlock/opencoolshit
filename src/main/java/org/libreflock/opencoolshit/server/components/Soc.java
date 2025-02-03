package org.libreflock.opencoolshit.server.components;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.server.internal.SocDriver;
import org.libreflock.opencoolshit.server.utils.InvDeserializer.InvEntry;

import li.cil.oc.api.Driver;
import li.cil.oc.api.Items;
import li.cil.oc.api.Network;
import li.cil.oc.api.detail.Builder.ConnectorBuilder;
import li.cil.oc.api.detail.Builder.NodeBuilder;
import li.cil.oc.api.detail.ItemInfo;
import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Soc extends AbstractManagedEnvironment implements DeviceInfo {

    NodeBuilder node;
    ConnectorBuilder comp;
    String uuid = "";
    EnvironmentHost host;
    List<InvEntry> envs;

    int tier;
    ItemStack stack;
    ListNBT components;

    public Soc(int tier, EnvironmentHost host, SocDriver SocDriver, ItemStack stack, List<InvEntry> envs) {
        node = Network.newNode(this, Visibility.Neighbors);
        comp = node.withConnector();
        setNode(node.create());
        this.stack = stack;
        this.tier = tier;
        this.host = host;
        this.envs = envs;

    }

    @Override
    public void onConnect(final Node node) {
        super.onConnect(node);
        loadData(this.stack.getTag());
        for (int i=0; i<envs.size(); i++) {
            InvEntry item = envs.get(i);
            OpenCoolshit.LOGGER.info("CONNECTING THIS ONE {}", item.stack.toString());
            if (item.env.node() != null) {
                if (node != item.env.node()) {
                    node.network().connect(node, item.env.node());
                    OpenCoolshit.LOGGER.info("CONNECTED THIS ONE {}", item.stack.toString());
                }
            }
        }
    }

    @Override
    public void onDisconnect(final Node node) {
        super.onDisconnect(node);
        for (int i=0; i<envs.size(); i++) {
            InvEntry item = envs.get(i);
            item.env.node().remove();
        }
    }

    @Override
    public Map<String, String> getDeviceInfo() {
        // TODO Auto-generated method stub
        return new HashMap<String,String>() {{
            put(DeviceInfo.DeviceAttribute.Class, DeviceInfo.DeviceClass.Processor);
            put(DeviceInfo.DeviceAttribute.Description, "SoC");
            put(DeviceInfo.DeviceAttribute.Vendor, "Shadow Kat Semiconductor");
            put(DeviceInfo.DeviceAttribute.Product, "SubSpaceShip blahblah"); // TODO: FIX THIS
            put(DeviceInfo.DeviceAttribute.Clock, "${capacity/Settings.storage.eepromEEPROMTime}");
        }};
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
                    } // this literally never works (according to the original developer) (it sorta does)
                }
                if (uuid == "") {
                    uuid = nbt.getCompound("oc:data").getCompound("node").getString("address");
                }
                else
                    OpenCoolshit.LOGGER.warn("node still has no uuid?");
            }
        }
    }
    
}

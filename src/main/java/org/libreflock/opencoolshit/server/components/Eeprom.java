package org.libreflock.opencoolshit.server.components;

import java.util.HashMap;
import java.util.Map;

import org.libreflock.opencoolshit.server.driver.EepromDriver;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.ItemStack;

public class Eeprom extends AbstractManagedEnvironment implements DeviceInfo {

    Node node = Network.newNode(this, Visibility.Neighbors).withComponent("ossm_prom", Visibility.Neighbors).withConnector().create();

    public Eeprom(int tier, EnvironmentHost host, EepromDriver eepromDriver, ItemStack stack) {
        setNode(node);
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
            put(DeviceInfo.DeviceAttribute.Clock, "${capacity/Settings.storage.eepromFlashTime}");
        }};
    }
    
}

package org.libreflock.opencoolshit.server.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.libreflock.opencoolshit.OpenCoolshit;
import org.libreflock.opencoolshit.common.network.PacketAddNoteParticle;
import org.libreflock.opencoolshit.common.network.PacketHandler;
import org.libreflock.opencoolshit.common.network.PacketPlayNote;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DeviceInfo;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;


public class IronNoteblockTile extends TileEntity implements ITickableTileEntity, Environment, DeviceInfo {
    Node node;

    public IronNoteblockTile() {
        super(Tiles.IRONNOTEBLOCK_TILE.get());
        this.node = Network.newNode(this, Visibility.Network).withComponent("iron_noteblock").create();
    }

    @Override
    public void tick() {
        if (node != null && node.network() == null) {
            li.cil.oc.api.Network.joinOrCreateNetwork(this);
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (node != null) {
            node.remove();
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (node != null) node.remove();
    }

    @Override
    public Node node() {
        return node;
    }

    @Override
    public Map<String, String> getDeviceInfo() {
        return new HashMap<String,String>() {{
            put(DeviceInfo.DeviceAttribute.Class, DeviceInfo.DeviceClass.Multimedia);
            put(DeviceInfo.DeviceAttribute.Description, "Iron Noteblock");
            put(DeviceInfo.DeviceAttribute.Vendor, "OpenCoolshit the company");
            put(DeviceInfo.DeviceAttribute.Version, "1.0 but slightly better");
            put(DeviceInfo.DeviceAttribute.Product, "noteblock but it is iron or something");
            put(DeviceInfo.DeviceAttribute.Size, "2 inches");
        }}; // suggestions for change is accepted
    }

    @Callback(direct = true, limit = 10, doc = "function([instrument:number or string,] note:number [, volume:number]); "
		+ "Plays the specified note with the specified instrument or the default one; volume may be a number between 0 and 1")
    public Object[] playNote(Context context, Arguments args) {
        BlockPos pos = this.getBlockPos();
        final ArrayList<String> INSTRUMENTS = new ArrayList<String>(Arrays.asList("harp", "basedrum", "snare", "hat", "bass", "flute", "bell", "guitar", "chime", "xylophone", "pling", "cow_bell", "didgeridoo", "iron_xylophone", "bit", "banjo"));

        int note = args.checkInteger(args.count()==1?0:1);
        float vol = (float)(args.count()<3?1:args.checkDouble(2));
        int instrument = 0;
        if (args.count()>1) {
            try {
                instrument = args.isInteger(0)?args.checkInteger(0):INSTRUMENTS.indexOf(args.checkString(0));
            } catch(java.lang.ArrayIndexOutOfBoundsException e) {
                return new Object[]{null, "Instrument does not exist!"};
            }
            
        }


        PacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketAddNoteParticle(pos, note));
        PacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketPlayNote(pos, note, instrument, vol));
        return new Object[]{};
    }
    @Override
    public void onConnect(Node node) {
    }

    @Override
    public void onDisconnect(Node node) {
    }

    @Override
    public void onMessage(Message message) {
    }

}
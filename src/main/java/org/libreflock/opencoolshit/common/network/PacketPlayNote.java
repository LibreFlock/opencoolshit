package org.libreflock.opencoolshit.common.network;

import java.util.function.Supplier;
import org.libreflock.opencoolshit.OpenCoolshit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketPlayNote {
    // private final String id;
    private static final SoundEvent[] INSTRUMENTS = {
        SoundEvents.NOTE_BLOCK_HARP,
        SoundEvents.NOTE_BLOCK_BASEDRUM,
        SoundEvents.NOTE_BLOCK_SNARE,
        SoundEvents.NOTE_BLOCK_HAT,
        SoundEvents.NOTE_BLOCK_BASS,
        SoundEvents.NOTE_BLOCK_FLUTE,
        SoundEvents.NOTE_BLOCK_BELL,
        SoundEvents.NOTE_BLOCK_GUITAR,
        SoundEvents.NOTE_BLOCK_CHIME,
        SoundEvents.NOTE_BLOCK_XYLOPHONE,
        SoundEvents.NOTE_BLOCK_PLING,
        SoundEvents.NOTE_BLOCK_COW_BELL,
        SoundEvents.NOTE_BLOCK_DIDGERIDOO,
        SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE,
        SoundEvents.NOTE_BLOCK_BIT,
        SoundEvents.NOTE_BLOCK_BANJO
    };

    private final BlockPos pos;
    private final int note;
    private final int instrument;
    private final float vol;

    public PacketPlayNote(PacketBuffer buf) {
        pos = buf.readBlockPos();
        note = buf.readInt();
        instrument = buf.readInt();
        vol = buf.readFloat();

    }
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(note);
        buf.writeInt(instrument);
        buf.writeFloat(vol);
    }
    public PacketPlayNote(BlockPos pos, int note, int instrument, float vol) {
        this.pos = pos;
        this.note = note;
        this.instrument = instrument;
        this.vol = vol;
    }

    public void handle (Supplier<NetworkEvent.Context> ctx) {
        OpenCoolshit.LOGGER.info("RECEIVED PLAYSOUND EVENT");
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().level; // x, y, z, sound, category (blocks/__music__), volume, pitch, distanceDelay
            SoundEvent sound = INSTRUMENTS[instrument%INSTRUMENTS.length];
            OpenCoolshit.LOGGER.info("{} {}", note, Math.pow(2f, ((note-12)/12)));
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.RECORDS, vol, (float)Math.pow(2f,((note-12)/12f)), false);
        });
        ctx.get().setPacketHandled(true);
    }
}

package org.libreflock.opencoolshit.common.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketAddNoteParticle {
    // private final String id;
    private final BlockPos pos;
    private final int note;

    public PacketAddNoteParticle(PacketBuffer buf) {
        // id = buf.readString();
        pos = buf.readBlockPos();
        note = buf.readInt();

    }
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(note);
    }
    public PacketAddNoteParticle(BlockPos pos, int note) {
        this.pos = pos;
        this.note = note;
    }

    public void handle (Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().level;
            // BasicParticleType particle = ParticleTypes.NOTE
            // world.addParticle(ParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+1.5, pos.getZ()+0.5, 0d, 0.05d, 0d);
            world.addParticle(ParticleTypes.NOTE, pos.getX()+0.5D, pos.getY()+1.2D, pos.getZ()+0.5D, note / 24.0D, 0.0D, 0.0D);
        });
        ctx.get().setPacketHandled(true);
    }
}

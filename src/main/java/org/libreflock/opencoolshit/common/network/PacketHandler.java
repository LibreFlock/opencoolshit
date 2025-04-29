package org.libreflock.opencoolshit.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static SimpleChannel CHANNEL;
    private static int ID = 0;

    public static int incID() {
        return ID++;
    }

    public static void registerMessages() {
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("opencoolshit", "channel"), () -> "1.0", (s) -> true, (s) -> true); // no clue what the fuck those last things are but alright
        
        CHANNEL.registerMessage(incID(),
            PacketAddNoteParticle.class,
            PacketAddNoteParticle::toBytes,
            PacketAddNoteParticle::new,
            PacketAddNoteParticle::handle
        );

        CHANNEL.registerMessage(incID(),
            PacketPlayNote.class,
            PacketPlayNote::toBytes,
            PacketPlayNote::new,
            PacketPlayNote::handle
        );
    }
}

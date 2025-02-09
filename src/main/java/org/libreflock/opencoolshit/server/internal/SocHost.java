package org.libreflock.opencoolshit.server.internal;

import org.libreflock.opencoolshit.OpenCoolshit;

import li.cil.oc.api.network.EnvironmentHost;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SocHost implements EnvironmentHost { // I don't actually believe this class gets used, since that debug statement never shows up in the console
    PlayerEntity player;                          // Just going to keep it here, in case it will be needed
    ItemStack stack;

    public SocHost(ItemStack stack, PlayerEntity player) {
        this.stack = stack;
        this.player = player;

    }

    @Override
    public World world() {
        return this.player.level;
    }

    @Override
    public double xPosition() {
        OpenCoolshit.LOGGER.info(this.player.getX());
        return this.player.getX();
    }

    @Override
    public double yPosition() {
        return this.player.getY();
    }

    @Override
    public double zPosition() {
        return this.player.getZ();
    }

    @Override
    public void markChanged() { } // idk wtf to do here
    
}

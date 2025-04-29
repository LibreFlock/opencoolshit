package org.libreflock.opencoolshit.common.block;

import javax.annotation.Nullable;

import org.libreflock.opencoolshit.server.tile.IronNoteblockTile;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class IronNoteblock extends BaseBlock {
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new IronNoteblockTile();
    }
}

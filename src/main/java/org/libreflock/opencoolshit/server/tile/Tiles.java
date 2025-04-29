package org.libreflock.opencoolshit.server.tile;

import org.libreflock.opencoolshit.common.block.Blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Tiles {
    public static DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "opencoolshit");

    public static RegistryObject<TileEntityType<IronNoteblockTile>> IRONNOTEBLOCK_TILE = TILES.register("iron_noteblock", () -> TileEntityType.Builder.of(() -> new IronNoteblockTile(),Blocks.IRON_NOTEBLOCK.get()).build(null));
}

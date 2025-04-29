package org.libreflock.opencoolshit.common.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "opencoolshit");

    public static RegistryObject<Block> IRON_NOTEBLOCK = BLOCKS.register("iron_noteblock", () -> new IronNoteblock());
}

package org.libreflock.opencoolshit;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class Settings {
    public static class Common {
        public final ConfigValue<Integer> FLASH_BLOCKSIZE;

        public final ConfigValue<Integer> FLASH_SIZE_TIER1;
        public final ConfigValue<Integer> FLASH_SIZE_TIER2;
        public final ConfigValue<Integer> FLASH_SIZE_TIER3;
        // public final ConfigValue<int[]> FLASH_SIZES;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("OSSM_Storage");
            this.FLASH_BLOCKSIZE = builder.comment("Block size of Flash").define("flash_block_size", 64);
            // this.FLASH_SIZES = builder.comment("Storage capacity of Flashes in blocks").define("flash_sizes", new int[]{64, 128, 256});

            this.FLASH_SIZE_TIER1 = builder.comment("Block capacity of Flash card (Tier 1)").define("flash_size_1", 64);
            this.FLASH_SIZE_TIER2 = builder.comment("Block capacity of Flash card (Tier 2)").define("flash_size_2", 128);
            this.FLASH_SIZE_TIER3 = builder.comment("Block capacity of Flash card (Tier 3)").define("flash_size_3", 256);

            builder.pop();
        }
    }
    public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static //constructor
	{
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON = commonSpecPair.getLeft();
		COMMON_SPEC = commonSpecPair.getRight();
	}
}
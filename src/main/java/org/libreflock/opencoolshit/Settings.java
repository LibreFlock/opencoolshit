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

        public final ConfigValue<Integer> EEPROM_BLOCKSIZE;
        public final ConfigValue<Integer> EEPROM_SIZE_TIER1;
        public final ConfigValue<Integer> EEPROM_SIZE_TIER2;
        public final ConfigValue<Integer> EEPROM_SIZE_TIER3;

        public final ConfigValue<Integer> EEPROM_FLASH_TIME;
        public final ConfigValue<Integer> EEPROM_WIPE_COST;
        // public final ConfigValue<int[]> FLASH_SIZES;

        public final ConfigValue<Integer> SOC_ASSEMBLYCOST_TIER1;
        public final ConfigValue<Integer> SOC_ASSEMBLYCOST_TIER2;
        public final ConfigValue<Integer> SOC_ASSEMBLYCOST_TIER3;
        

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("OpenSolidState");
            builder.push("Flash");
            this.FLASH_BLOCKSIZE = builder.comment("Block size of Flash").define("flash_block_size", 64);
            this.FLASH_SIZE_TIER1 = builder.comment("Block capacity of Flash card (Tier 1)").define("flash_size_1", 64);
            this.FLASH_SIZE_TIER2 = builder.comment("Block capacity of Flash card (Tier 2)").define("flash_size_2", 128);
            this.FLASH_SIZE_TIER3 = builder.comment("Block capacity of Flash card (Tier 3)").define("flash_size_3", 256);
            builder.pop();

            builder.push("PROM");
            this.EEPROM_BLOCKSIZE = builder.comment("Block size of PROM").define("eeprom_block_size", 64);
            this.EEPROM_SIZE_TIER1 = builder.comment("Block capacity of PROM (Tier 1)").define("eeprom_size_1", 64);
            this.EEPROM_SIZE_TIER2 = builder.comment("Block capacity of PROM (Tier 2)").define("eeprom_size_2", 128);
            this.EEPROM_SIZE_TIER3 = builder.comment("Block capacity of PROM (Tier 3)").define("eeprom_size_3", 256);

            this.EEPROM_FLASH_TIME = builder.comment("Flash time of PROMs").define("eeprom_flash_time", 2);
            this.EEPROM_WIPE_COST = builder.comment("The energy cost of wiping a PROM").define("eeprom_wipe_cost", 600);
            builder.pop();

            builder.push("SoC");
            this.SOC_ASSEMBLYCOST_TIER1 = builder.comment("The Energy Cost of assembling an SoC (Tier 1)").define("soc_assemblyCost_1", 200);
            this.SOC_ASSEMBLYCOST_TIER2 = builder.comment("The Energy Cost of assembling an SoC (Tier 2)").define("soc_assemblyCost_2", 400);
            this.SOC_ASSEMBLYCOST_TIER3 = builder.comment("The Energy Cost of assembling an SoC (Tier 3)").define("soc_assemblyCost_3", 600);
            builder.pop();

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
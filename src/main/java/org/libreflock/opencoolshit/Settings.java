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
        // public final ConfigValue<int[]> FLASH_SIZES;

        public final ConfigValue<Integer> SOC_CALLBUDGET_TIER1;
        public final ConfigValue<Integer> SOC_CALLBUDGET_TIER2;
        public final ConfigValue<Integer> SOC_CALLBUDGET_TIER3;
        
        public final ConfigValue<Integer> SOC_COMPONENTS_TIER1;
        public final ConfigValue<Integer> SOC_COMPONENTS_TIER2;
        public final ConfigValue<Integer> SOC_COMPONENTS_TIER3;

        public final ConfigValue<Integer> SOC_COMPLEXITY_TIER1;
        public final ConfigValue<Integer> SOC_COMPLEXITY_TIER2;
        public final ConfigValue<Integer> SOC_COMPLEXITY_TIER3;
        

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
            builder.pop();

            builder.push("SoC");
            this.SOC_CALLBUDGET_TIER1 = builder.comment("Call budget of SoC (Tier 1)").define("soc_callbudget_1", 64);
            this.SOC_CALLBUDGET_TIER2 = builder.comment("Call budget of SoC (Tier 2)").define("soc_callbudget_2", 64);
            this.SOC_CALLBUDGET_TIER3 = builder.comment("Call budget of SoC (Tier 3)").define("soc_callbudget_3", 64);

            this.SOC_COMPONENTS_TIER1 = builder.comment("Max components of SoC (Tier 1)").define("soc_components_1", 64);
            this.SOC_COMPONENTS_TIER2 = builder.comment("Max components of SoC (Tier 2)").define("soc_components_2", 64);
            this.SOC_COMPONENTS_TIER3 = builder.comment("Max components of SoC (Tier 3)").define("soc_components_3", 64);

            this.SOC_COMPLEXITY_TIER1 = builder.comment("Max complexity of SoC (Tier 1)").define("soc_complexity_1", 1);
            this.SOC_COMPLEXITY_TIER2 = builder.comment("Max complexity of SoC (Tier 2)").define("soc_complexity_2", 2);
            this.SOC_COMPLEXITY_TIER3 = builder.comment("Max complexity of SoC (Tier 3)").define("soc_complexity_3", 5);
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
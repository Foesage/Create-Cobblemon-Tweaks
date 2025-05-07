package cobblemon.creatified.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static cobblemon.creatified.CobblemonCreatified.MODID;

public class ModComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SHINY_CHARGE =
            DATA_COMPONENTS.register("shiny_charge", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(Codec.INT) // ✅ use Codec.INT instead of IntTag.CODEC
                            .build()
            );
}

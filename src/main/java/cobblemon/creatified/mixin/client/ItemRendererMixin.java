package cobblemon.creatified.mixin.client;

import cobblemon.creatified.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
    BakedModel renderItemModel(BakedModel model, ItemStack stack, ItemDisplayContext displayContext) {
      if (Minecraft.getInstance().level == null) {
            // If the level is null, we are likely in a non-world context (e.g., GUI).
            // Return the model as is, or you can modify it if needed.
            return model;
        }

      if (displayContext == ItemDisplayContext.GUI) {
          BakedModel guiModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(ModItems.COPPER_BALL_BLANK.getId(), "inventory"));
          return guiModel.getOverrides().resolve(guiModel, stack, Minecraft.getInstance().level, Minecraft.getInstance().player, 0);
      }

        return model;
    }

//    @ModifyReturnValue(method = "getModel", at = @At("RETURN"))
//    private BakedModel getModel(BakedModel model, @Local(argsOnly = true) ItemStack stack) {
//        if (stack.is(ModItems.COPPER_BALL_BLANK.asItem()))
//        {
//            @Nullable ClientLevel clientLevel = level instanceof ClientLevel lv ? lv : null;
//            return
//        }
//        // This mixin is intended to modify the model returned by getModel.
//        // You can add custom logic here to modify the model if needed.
//        // For now, we just return the original model.
//        return model;
//    }
}
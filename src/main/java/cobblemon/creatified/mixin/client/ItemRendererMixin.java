package cobblemon.creatified.mixin.client;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import cobblemon.creatified.item.ModItems;
import net.minecraft.client.resources.model.ModelResourceLocation;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    BakedModel renderItemModel(BakedModel model, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand,
                               PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
      if (Minecraft.getInstance().level == null) {
            // If the level is null, we are likely in a non-world context (e.g., GUI).
            // Return the model as is, or you can modify it if needed.
            return model;
        }

      if (displayContext == ItemDisplayContext.GUI && stack.is(ModItems.COPPER_BALL_BLANK.asItem())) {
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
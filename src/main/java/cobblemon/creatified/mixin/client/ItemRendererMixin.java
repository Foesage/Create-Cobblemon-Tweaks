package cobblemon.creatified.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import cobblemon.creatified.item.BallItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, remap = false)
    private BakedModel cobblemoncreatified$overrideModel(
            BakedModel originalModel,
            ItemStack stack,
            ItemDisplayContext context,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay
    ) {
        if (Minecraft.getInstance().level != null &&
                stack.getItem() instanceof BallItem &&
                context == ItemDisplayContext.GUI) {

            ModelResourceLocation guiModel = new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath("cobblemoncreatified", "copper_ball_blank"),
                    "inventory"
            );

            return Minecraft.getInstance().getModelManager().getModel(guiModel);
        }

        return originalModel;
    }
}

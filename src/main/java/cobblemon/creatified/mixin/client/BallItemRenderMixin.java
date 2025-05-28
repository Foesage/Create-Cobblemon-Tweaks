package cobblemon.creatified.mixin.client;

import cobblemon.creatified.item.BallItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class BallItemRenderMixin {

    @Inject(
            method = "render(Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"), cancellable = true
    )
    private void cobblemoncreatified$renderBallItem(
            ItemStack stack,
            int x,
            int y,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci) {

        if (stack.getItem() instanceof BallItem ballItem) {
            ballItem.customRender(stack, x, y, poseStack, buffer, light, overlay, model);
            ci.cancel();
        }
    }
}

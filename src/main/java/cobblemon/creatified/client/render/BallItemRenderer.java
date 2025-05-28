package cobblemon.creatified.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class BallItemRenderer {

    // Static entry point called by BallItem#customRender
    public static void render(ItemStack stack, int x, int y, PoseStack poseStack,
                              MultiBufferSource buffer, int light, int overlay, BakedModel model) {

        // TODO: Add custom rendering logic here
        // For now, we just print a debug message.
        System.out.println("BallItemRenderer: Rendering " + stack);

        // Example: To render the vanilla baked model (placeholder until you add custom logic)
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        mc.getItemRenderer().render(stack, net.minecraft.world.item.ItemDisplayContext.NONE,
                false, poseStack, buffer, light, overlay, model);
    }
}

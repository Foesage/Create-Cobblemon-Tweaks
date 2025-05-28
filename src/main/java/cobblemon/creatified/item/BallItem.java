package cobblemon.creatified.item;

import cobblemon.creatified.client.render.BallItemRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BallItem extends Item {

    public BallItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        // Optional: add ground particle logic later
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void customRender(ItemStack stack, int x, int y, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel model) {
        // Delegate to your BallItemRenderer, or implement your rendering here
        BallItemRenderer.render(stack, x, y, poseStack, buffer, light, overlay, model);
    }
}

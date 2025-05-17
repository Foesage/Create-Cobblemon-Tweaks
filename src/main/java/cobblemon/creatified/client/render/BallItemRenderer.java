package cobblemon.creatified.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import cobblemon.creatified.CobblemonCreatified;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BallItemRenderer extends CustomRenderedItemModelRenderer {

    private static final PartialModel IN_HAND_MODEL =
            PartialModel.of(CobblemonCreatified.resource("item/copper_ball_blank/item_in_hand"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
                          ItemDisplayContext transformType, PoseStack poseStack,
                          MultiBufferSource buffer, int light, int overlay) {
        // Match Create: only GUI uses 2D, everything else is 3D
        renderer.render(transformType == ItemDisplayContext.GUI ? model.getOriginalModel() : IN_HAND_MODEL.get(), light);
    }
}

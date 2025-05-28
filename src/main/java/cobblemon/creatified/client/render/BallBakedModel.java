package cobblemon.creatified.client.render;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BallBakedModel implements BakedModel {
    private final TextureAtlasSprite sprite;

    public BallBakedModel(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        return List.of(this);
    }

    @Override
    public boolean isVanillaAdapter() {
        return true;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        // Only render in the GUI (direction == null is used for GUI)
        if (direction != null) return Collections.emptyList();

        // Render a flat quad using the sprite
        return List.of(createItemQuad(sprite));
    }

    private BakedQuad createItemQuad(TextureAtlasSprite sprite) {
        // Quad covers full item square: (0,0)-(16,16)
        int[] vertexData = new int[28];
        int color = 0xFFFFFFFF; // White, no tint

        // Use the vanilla ItemLayerModel logic for sprite-based items.
        // This is a simplified approach, but for full vanilla-compat, you may want to port ItemLayerModel logic.

        // You can use an existing utility from Minecraft, or just note that you will see the sprite for now.
        // Many custom mods just return an empty quad for now and extend this with full item rendering later.

        // For this placeholder, return an empty list.
        // If you want a fully working flat item, consider using com.mojang.blaze3d.vertex.Poseidon, or see below.

        // -- Placeholder until you want to port full quad builder --
        return new BakedQuad(new int[28], color, Direction.SOUTH, sprite, true);
    }

    @Override
    public boolean isGui3d() {
        return false; // false for flat 2D in GUI
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return sprite;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public RenderType getRenderType(ItemStack stack, boolean fabulous) {
        return RenderType.solid();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }
}

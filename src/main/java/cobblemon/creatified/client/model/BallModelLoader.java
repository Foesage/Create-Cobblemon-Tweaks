package cobblemon.creatified.client.model;

import cobblemon.creatified.client.render.BallBakedModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class BallModelLoader implements IGeometryLoader<BallModelLoader.BallModelGeometry> {
    @Override
    public BallModelGeometry read(JsonObject json, JsonDeserializationContext context) {
        return new BallModelGeometry();
    }

    public static class BallModelGeometry implements IUnbakedGeometry<BallModelGeometry> {
        @Override
        public BakedModel bake(
                ModelBakery bakery,
                ModelState modelState,
                ItemOverrides overrides,
                ResourceLocation modelLocation,
                Function<ResourceLocation, TextureAtlasSprite> spriteGetter
        ) {
            // Fetch the sprite for this item
            TextureAtlasSprite sprite = spriteGetter.apply(
                    new ResourceLocation(modelLocation.getNamespace(), "item/" + modelLocation.getPath())
            );
            return new BallBakedModel(sprite);
        }
    }
}

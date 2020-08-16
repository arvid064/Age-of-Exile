package com.robertx22.age_of_exile.mobs.zombies;

import com.robertx22.age_of_exile.mmorpg.Ref;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class DrownedLayerRenderer extends FeatureRenderer<BaseZombie, DrownedEntityModel<BaseZombie>> {
    private Identifier SKIN;
    private final DrownedEntityModel<BaseZombie> model = new DrownedEntityModel(0.25F, 0.0F, 64, 64);

    public DrownedLayerRenderer(ModZombieRenderer ctx, String id) {
        super(ctx);
        this.SKIN = new Identifier(Ref.MODID, "textures/entity/zombie/" + id);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, BaseZombie drownedEntity, float f, float g, float h, float j, float k, float l) {
        render(this.getContextModel(), this.model, SKIN, matrixStack, vertexConsumerProvider, i, drownedEntity, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
    }
}


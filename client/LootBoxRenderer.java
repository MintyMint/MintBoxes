package com.mint.mintboxes.client;

import com.mint.mintboxes.MintBoxes;
import com.mint.mintboxes.block.entity.LootBoxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class LootBoxRenderer implements BlockEntityRenderer<LootBoxBlockEntity> {

    // Vanilla chest parts
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    // Tier textures
    private static final Map<String, ResourceLocation> TIER_TEXTURES = Map.of(
            "stone",     ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "textures/block/loot_box_base.png"),
            "iron",      ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "textures/block/loot_box_iron.png"),
            "gold",      ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "textures/block/loot_box_gold.png"),
            "diamond",   ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "textures/block/loot_box_diamond.png"),
            "netherite", ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "textures/block/loot_box_netherite.png"),
            "special",   ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "textures/block/loot_box_special.png")
    );

    private static final ResourceLocation FALLBACK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "textures/block/loot_box_placeholder.png");

    public LootBoxRenderer(BlockEntityRendererProvider.Context ctx) {
        ModelPart root = ctx.bakeLayer(ModelLayers.CHEST);
        this.bottom = root.getChild("bottom");
        this.lid    = root.getChild("lid");
        this.lock   = root.getChild("lock");
    }

    @Override
    public void render(LootBoxBlockEntity be, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();

        // Animate lid
        float lidAngle = be.getLidAngle(partialTicks);
        this.lid.xRot = -(lidAngle * ((float)Math.PI / 2F));
        this.lock.xRot = this.lid.xRot;

        // Pick texture based on tier
        ResourceLocation tex = TIER_TEXTURES.getOrDefault(be.getTier(), FALLBACK_TEXTURE);

        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutout(tex));

        // Render chest parts
        this.bottom.render(poseStack, vc, light, overlay);
        this.lid.render(poseStack, vc, light, overlay);
        this.lock.render(poseStack, vc, light, overlay);

        poseStack.popPose();
    }
}

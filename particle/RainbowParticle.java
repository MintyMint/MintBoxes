package com.mint.mintboxes.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class RainbowParticle extends TextureSheetParticle {

    private final float[] color;
    private final float spinSpeed;

    public RainbowParticle(ClientLevel world, double x, double y, double z,
                           double dx, double dy, double dz, SpriteSet sprites) {
        super(world, x, y, z, dx, dy, dz);
        this.pickSprite(sprites);
        this.setSpriteFromAge(sprites);
        this.gravity = 0.5F;
        this.lifetime = 40 + this.random.nextInt(20);
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
        this.quadSize = 0.05F; // adjust size if too big/small

        float[][] colors = {
                {1.0f, 0.0f, 0.0f},   // Red
                {1.0f, 0.5f, 0.0f},   // Orange
                {1.0f, 1.0f, 0.0f},   // Yellow
                {0.0f, 1.0f, 0.0f},   // Green
                {0.0f, 0.0f, 1.0f},   // Blue
                {0.29f, 0.0f, 0.51f}, // Indigo
                {0.56f, 0.0f, 1.0f}   // Violet
        };

        this.color = colors[random.nextInt(colors.length)];
        this.setColor(color[0], color[1], color[2]);

        // Random rotation speed/direction
        this.spinSpeed = (random.nextFloat() - 0.1f) * 0.1f; // -0.2 to +0.2 radians/tick
        this.roll = random.nextFloat() * ((float) Math.PI * 2); // random initial angle

        // Initial velocity: outward in a random direction + upward
        double angle = this.random.nextDouble() * Math.PI * 2;
        double speed = 0.01 + this.random.nextDouble() * 0.1;
        this.xd = Math.cos(angle) * speed;
        this.zd = Math.sin(angle) * speed;
        this.yd = 0.2 + this.random.nextDouble() * 0.1;
    }

    @Override
    public void tick(){
        super.tick();

        float progress = (float) this.age / (float) this.lifetime;
        this.alpha = 1.0F - progress;

        this.oRoll = this.roll;
        this.roll += this.spinSpeed;
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new RainbowParticle(world, x, y, z, dx, dy, dz, this.spriteSet);
        }
    }
}

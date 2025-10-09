package com.mint.mintboxes.particle;

import com.mint.mintboxes.registry.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import com.mint.mintboxes.client.particle.RainbowParticle;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class RainbowParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public RainbowParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ModParticles.RAINBOW.get(), RainbowParticle.Provider::new);
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                   double x, double y, double z,
                                   double vx, double vy, double vz) {
        return new RainbowParticle(level, x, y, z, vx, vy, vz, sprites);
    }
}

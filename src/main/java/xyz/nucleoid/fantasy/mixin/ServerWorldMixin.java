package xyz.nucleoid.fantasy.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.fantasy.FantasyWorldAccess;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements FantasyWorldAccess {
    @Shadow public abstract List<ServerPlayerEntity> players();
    @Shadow public abstract ServerChunkProvider getChunkSource();
    private static final int TICK_TIMEOUT = 20 * 15;
    @Unique private boolean fantasy$tickWhenEmpty = true;
    @Unique private int fantasy$tickTimeout;

    @Override
    public void fantasy$setTickWhenEmpty(boolean tickWhenEmpty) {
        this.fantasy$tickWhenEmpty = tickWhenEmpty;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        boolean shouldTick = this.fantasy$tickWhenEmpty || !this.isWorldEmpty();
        if (shouldTick) {
            this.fantasy$tickTimeout = TICK_TIMEOUT;
        } else if (this.fantasy$tickTimeout-- <= 0) {
            ci.cancel();
        }
    }

    @Override
    public boolean fantasy$shouldTick() {
        boolean shouldTick = this.fantasy$tickWhenEmpty || this.isWorldEmpty();
        return shouldTick || this.fantasy$tickTimeout > 0;
    }

    private boolean isWorldEmpty() {
        return this.players().isEmpty() && this.getChunkSource().getLoadedChunksCount() <=0;
    }
}

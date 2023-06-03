package xyz.nucleoid.fantasy.mixin;

import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nucleoid.fantasy.FantasyWorldAccess;

@Mixin(ServerChunkProvider.class)
public class ServerChunkManagerMixin {
    @Shadow @Final public ServerWorld level;
    @Inject(method = "pollTask", at = @At("HEAD"), cancellable = true)
    private void onPollTask(CallbackInfoReturnable<Boolean> cir) {
        if (!((FantasyWorldAccess) this.level).fantasy$shouldTick()) cir.setReturnValue(false);
    }
}

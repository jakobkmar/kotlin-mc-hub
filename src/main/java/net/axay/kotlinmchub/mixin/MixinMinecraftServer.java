package net.axay.kotlinmchub.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftServer.class, remap = false)
public class MixinMinecraftServer {
    @Inject(
            method = "getServerModName",
            at = @At("HEAD"),
            cancellable = true
    )
    public void setServerName(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("fler");
    }
}

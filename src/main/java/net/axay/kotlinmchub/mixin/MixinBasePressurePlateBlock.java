package net.axay.kotlinmchub.mixin;

import net.axay.kotlinmchub.features.ElytraLauncherKt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasePressurePlateBlock.class)
public class MixinBasePressurePlateBlock {

    @Inject(
        method = "checkPressed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/BasePressurePlateBlock;playOnSound(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V",
            shift = At.Shift.BEFORE
        )
    )
    private void onPressed(Entity entity,
                           Level world,
                           BlockPos pos,
                           BlockState state,
                           int output,
                           CallbackInfo ci) {
        if (entity != null) {
            ElytraLauncherKt.launchElytra(entity, pos, state);
        }
    }
}

package xyz.nucleoid.fantasy.mixin.api;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.nucleoid.fantasy.api.ExtendedIntegerValue;

@Mixin(GameRules.IntegerValue.class)
public abstract class IntegerValueImpl implements ExtendedIntegerValue {
    @SuppressWarnings("unused") @Shadow private int value;
    public void setValue(int value) {
        this.value = value;
    }
}

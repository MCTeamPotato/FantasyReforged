package xyz.nucleoid.fantasy.mixin.registry;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.nucleoid.fantasy.RemoveFromRegistry;

import java.util.Map;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements RemoveFromRegistry<T> {
    @Shadow @Final private Object2IntMap<T> toId;
    @Shadow @Final private BiMap<ResourceLocation, T> storage;
    @Shadow @Final private BiMap<RegistryKey<T>, T> keyStorage;
    @Shadow @Final private Map<T, Lifecycle> lifecycles;
    @Shadow @Final private ObjectList<T> byId;
    @SuppressWarnings("unused")
    @Shadow protected Object[] randomCache;

    @Override
    public boolean fantasy$remove(T entry) {
        int rawId = this.toId.removeInt(entry);
        if (rawId == -1) return false;

        this.storage.inverse().remove(entry);
        this.keyStorage.inverse().remove(entry);
        this.lifecycles.remove(entry);

        this.byId.set(rawId, null);

        this.randomCache = null;

        return true;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean fantasy$remove(ResourceLocation key) {
        T entry = this.storage.get(key);
        return entry != null && this.fantasy$remove(entry);
    }

}

package xyz.nucleoid.fantasy;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@SuppressWarnings({"unused", "unchecked"})
public interface RemoveFromRegistry<T> {
    static <T> boolean remove(SimpleRegistry<T> registry, ResourceLocation key) {
        return ((RemoveFromRegistry<T>) registry).fantasy$remove(key);
    }

    static <T> boolean remove(SimpleRegistry<T> registry, T value) {
        return ((RemoveFromRegistry<T>) registry).fantasy$remove(value);
    }

    boolean fantasy$remove(T value);
    boolean fantasy$remove(ResourceLocation key);
}
package xyz.nucleoid.fantasy.util;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.fantasy.api.ExtendedIntegerValue;

@SuppressWarnings("unused")
public final class GameRuleStore {
    private final Reference2BooleanMap<GameRules.RuleKey<GameRules.BooleanValue>> booleanRules = new Reference2BooleanOpenHashMap<>();
    private final Reference2IntMap<GameRules.RuleKey<GameRules.IntegerValue>> intRules = new Reference2IntOpenHashMap<>();

    public void set(GameRules.RuleKey<GameRules.BooleanValue> key, boolean value) {
        this.booleanRules.put(key, value);
    }

    public void set(GameRules.RuleKey<GameRules.IntegerValue> key, int value) {
        this.intRules.put(key, value);
    }

    public boolean getBoolean(GameRules.RuleKey<GameRules.BooleanValue> key) {
        return this.booleanRules.getBoolean(key);
    }

    public int getInt(GameRules.RuleKey<GameRules.IntegerValue> key) {
        return this.intRules.getInt(key);
    }

    public boolean contains(GameRules.RuleKey<?> key) {
        return this.booleanRules.containsKey(key) || this.intRules.containsKey(key);
    }

    public void applyTo(GameRules rules, @Nullable MinecraftServer server) {
        Reference2BooleanMaps.fastForEach(this.booleanRules, entry -> {
            GameRules.BooleanValue rule = rules.getRule(entry.getKey());
            rule.set(entry.getBooleanValue(), server);
        });

        Reference2IntMaps.fastForEach(this.intRules, entry -> {
            GameRules.IntegerValue rule = rules.getRule(entry.getKey());
            ((ExtendedIntegerValue)rule).setValue(entry.getIntValue());
        });
    }
}

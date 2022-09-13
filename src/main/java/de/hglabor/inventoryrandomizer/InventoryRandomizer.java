package de.hglabor.inventoryrandomizer;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.literal;

public class InventoryRandomizer implements ModInitializer {
    public static int DAMAGE_TIMES = 1;
    public static final int MAX_STACK_SIZE = 1000000;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("multiplier").then(getMultiplier().executes(ctx -> {
                DAMAGE_TIMES = ctx.getArgument("multiplier", Integer.class);
                ctx.getSource().sendFeedback(Text.of("Set to " + getMultiplier()), true);
                return 1;
            })));
        }));
    }

    public RequiredArgumentBuilder<ServerCommandSource, Integer> getMultiplier() {
        return RequiredArgumentBuilder.argument("multiplier", IntegerArgumentType.integer(0, 500000));
    }
}

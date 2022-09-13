package de.hglabor.inventoryrandomizer.mixin;

import com.mojang.authlib.GameProfile;
import de.hglabor.inventoryrandomizer.InventoryRandomizer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public abstract void sendMessage(Text message);
    @Unique private final List<Item> mooz$nope = List.of(
            Items.ENDER_EYE,
            Items.BLAZE_ROD,
            Items.BLAZE_POWDER,
            Items.ENDER_PEARL
    );

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        InventoryRandomizer.DAMAGE_TIMES++;
        var inventory = getInventory();
        mooz$processInventory(inventory.main, inventory);
        mooz$processSlot(EquipmentSlot.OFFHAND);
        mooz$processSlot(EquipmentSlot.HEAD);
        mooz$processSlot(EquipmentSlot.CHEST);
        mooz$processSlot(EquipmentSlot.LEGS);
        mooz$processSlot(EquipmentSlot.FEET);
    }

    private void mooz$processInventory(DefaultedList<ItemStack> inventoryList, Inventory inventory) {
        for (ItemStack itemStack : inventoryList) {
            if (!itemStack.isEmpty() && !mooz$nope.contains(itemStack.getItem())) {
                var count = itemStack.getCount()*InventoryRandomizer.DAMAGE_TIMES;
                var newStack = Registry.ITEM.getRandom(random).get().value().getDefaultStack();
                while (mooz$nope.contains(newStack.getItem())) {
                    newStack = Registry.ITEM.getRandom(random).get().value().getDefaultStack();
                }
                var slot = inventoryList.indexOf(itemStack);
                newStack.setCount(count);
                inventory.setStack(slot, newStack);
                currentScreenHandler.syncState();
            }
        }
    }

    private void mooz$processSlot(EquipmentSlot eSlot) {
        var itemStack = getEquippedStack(eSlot);
        if (!itemStack.isEmpty() && !mooz$nope.contains(itemStack.getItem())) {
            var count = itemStack.getCount()*InventoryRandomizer.DAMAGE_TIMES;
            var newStack = Registry.ITEM.getRandom(random).get().value().getDefaultStack();
            while (mooz$nope.contains(newStack.getItem())) {
                newStack = Registry.ITEM.getRandom(random).get().value().getDefaultStack();
            }
            newStack.setCount(count);
            equipStack(eSlot, newStack);
            currentScreenHandler.syncState();
        }
    }

}

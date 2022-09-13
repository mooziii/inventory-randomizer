package de.hglabor.inventoryrandomizer.mixin;

import de.hglabor.inventoryrandomizer.InventoryRandomizer;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getMaxCount", at = @At("RETURN"), cancellable = true)
    private void modifyMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(InventoryRandomizer.MAX_STACK_SIZE);
    }
}

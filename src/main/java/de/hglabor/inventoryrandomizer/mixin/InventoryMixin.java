package de.hglabor.inventoryrandomizer.mixin;

import de.hglabor.inventoryrandomizer.InventoryRandomizer;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public interface InventoryMixin {
    @Inject(method = "getMaxCountPerStack", at = @At("RETURN"), cancellable = true)
    private void modifyMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(InventoryRandomizer.MAX_STACK_SIZE);
    }
}

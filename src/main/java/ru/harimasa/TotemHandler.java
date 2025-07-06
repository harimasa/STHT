package ru.harimasa;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import ru.harimasa.configuration.STHTConfig;

import java.lang.reflect.Field;

public class TotemHandler {
    private static final int OFFHAND_SLOT_ID = 45;
    private long hoverStartTime = -1;
    private Slot hoveredSlot = null;
    private boolean isMoving = false;

    public void handleTick(MinecraftClient client) {
        if (!STHTConfig.CONFIG.instance().isEnabled) {
            resetState();
            return;
        }

        if (client.player == null || client.currentScreen == null) {
            resetState();
            return;
        }

        if (client.currentScreen instanceof HandledScreen<?> screen) {
            Slot currentSlot = getFocusedSlot(screen);
            handleHoveredSlot(screen, client, currentSlot);
        }
    }

    private Slot getFocusedSlot(HandledScreen<?> screen) {
        try {
            Field focusedSlotField = HandledScreen.class.getDeclaredField("focusedSlot");
            focusedSlotField.setAccessible(true);
            return (Slot) focusedSlotField.get(screen);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleHoveredSlot(HandledScreen<?> screen, MinecraftClient client, Slot currentSlot) {
        if (client.player == null || currentSlot == null) {
            resetState();
            return;
        }

        if (!isPlayerInventorySlot(screen, currentSlot)) {
            resetState();
            return;
        }

        ItemStack cursorStack = client.player.currentScreenHandler.getCursorStack();
        ItemStack offhandStack = client.player.getOffHandStack();

        if (isTotem(offhandStack)) {
            resetState();
            return;
        }

        if (!isTotem(currentSlot.getStack()) || !cursorStack.isEmpty()) {
            resetState();
            return;
        }

        if (!offhandStack.isEmpty() && !STHTConfig.CONFIG.instance().isIgnoreItemInSecondaryHand) {
            resetState();
            return;
        }

        ItemStack currentStack = currentSlot.getStack();

        if (!STHTConfig.CONFIG.instance().isUsageEnchantedTotems && currentStack.hasEnchantments()) {
            resetState();
            return;
        }

        if (!STHTConfig.CONFIG.instance().isUsageRenamedTotems && currentStack.getName() != Items.TOTEM_OF_UNDYING.getName()) {
            resetState();
            return;
        }

        if (hoveredSlot != currentSlot) {
            hoverStartTime = System.currentTimeMillis();
            hoveredSlot = currentSlot;
        }

        float elapsed = (System.currentTimeMillis() - hoverStartTime) / 1000f;

        if (elapsed >= STHTConfig.CONFIG.instance().delay && !isMoving) {
            ItemStack currentCursor = client.player.currentScreenHandler.getCursorStack();
            ItemStack currentOffhand = client.player.getOffHandStack();
            ItemStack currentSlotStack = currentSlot.getStack();

            if (!currentCursor.isEmpty() || isTotem(currentOffhand)) {
                resetState();
                return;
            }

            if (!currentOffhand.isEmpty() && !STHTConfig.CONFIG.instance().isIgnoreItemInSecondaryHand) {
                resetState();
                return;
            }

            if (!STHTConfig.CONFIG.instance().isUsageEnchantedTotems && currentSlotStack.hasEnchantments()) {
                resetState();
                return;
            }

            if (!STHTConfig.CONFIG.instance().isUsageRenamedTotems && currentStack.getName() != Items.TOTEM_OF_UNDYING.getName()) {
                resetState();
                return;
            }

            if (currentOffhand.isEmpty()) {
                moveTotemToOffhand(screen, client, currentSlot);
            } else {
                swapTotemWithOffhand(screen, client, currentSlot);
            }
        }
    }

    private boolean isPlayerInventorySlot(HandledScreen<?> screen, Slot slot) {
        if (!(screen.getScreenHandler() instanceof PlayerScreenHandler)) {
            return false;
        }

        if (!(slot.inventory instanceof PlayerInventory)) {
            return false;
        }

        return slot.id != OFFHAND_SLOT_ID;
    }

    private void moveTotemToOffhand(HandledScreen<?> screen, MinecraftClient client, Slot slot) {
        isMoving = true;

        assert client.interactionManager != null;
        client.interactionManager.clickSlot(
                screen.getScreenHandler().syncId,
                slot.id,
                0,
                SlotActionType.PICKUP,
                client.player
        );

        client.interactionManager.clickSlot(
                screen.getScreenHandler().syncId,
                OFFHAND_SLOT_ID,
                0,
                SlotActionType.PICKUP,
                client.player
        );

        resetState();
    }

    private void swapTotemWithOffhand(HandledScreen<?> screen, MinecraftClient client, Slot slot) {
        isMoving = true;

        assert client.interactionManager != null;
        client.interactionManager.clickSlot(
                screen.getScreenHandler().syncId,
                slot.id,
                0,
                SlotActionType.PICKUP,
                client.player
        );

        client.interactionManager.clickSlot(
                screen.getScreenHandler().syncId,
                OFFHAND_SLOT_ID,
                0,
                SlotActionType.PICKUP,
                client.player
        );

        client.interactionManager.clickSlot(
                screen.getScreenHandler().syncId,
                slot.id,
                0,
                SlotActionType.PICKUP,
                client.player
        );
        resetState();
    }

    private boolean isTotem(ItemStack stack) {
        return stack.isOf(Items.TOTEM_OF_UNDYING);
    }

    private void resetState() {
        hoverStartTime = -1;
        hoveredSlot = null;
        isMoving = false;
    }
}

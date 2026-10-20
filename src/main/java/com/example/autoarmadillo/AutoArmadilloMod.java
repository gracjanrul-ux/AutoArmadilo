package com.example.autoarmadillo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AutoArmadilloMod implements ClientModInitializer {
    private static KeyBinding toggleKey;
    private boolean enabled = false;
    private int repairTimer = 0;
    private int interactCooldown = 0;

    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(
                Identifier.of("autoarmadillo", "main")
        );

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autoarmadillo.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null || client.interactionManager == null) {
                return;
            }

            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                repairTimer = 0;
                client.player.sendMessage(
                        Text.literal("AutoArmadillo: " + (enabled ? "§aWŁĄCZONY" : "§cWYŁĄCZONY")),
                        true
                );
            }

            if (!enabled) {
                repairTimer = 0;
                interactCooldown = 0;
                return;
            }

            if (interactCooldown > 0) {
                interactCooldown--;
            }

            HitResult hit = client.crosshairTarget;
            if (interactCooldown == 0 && hit instanceof EntityHitResult entityHit
                    && entityHit.getEntity() instanceof ArmadilloEntity armadillo) {
                client.interactionManager.interactEntity(client.player, armadillo, Hand.MAIN_HAND);
                client.player.swingHand(Hand.MAIN_HAND);
                // Ogranicza wysyłanie interakcji do jednej na 10 ticków.
                interactCooldown = 10;
            }

            repairTimer++;
            if (repairTimer >= 40) {
                client.player.networkHandler.sendChatCommand("repair");
                repairTimer = 0;
            }
        });
    }
}

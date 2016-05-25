package de.randombyte.holograms.commands

import de.randombyte.holograms.config.ConfigManager
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors

class UpdateHologramsCommand : PlayerCommandExecutor() {
    override fun executedByPlayer(player: Player, args: CommandContext): CommandResult {
        ConfigManager.getHolograms(player.world).forEach { hologram ->
            hologram.second.forEach { line ->
                player.world.getEntity(line.first).ifPresent { it.offer(Keys.DISPLAY_NAME, line.second) }
            }
        }
        player.sendMessage(Text.of(TextColors.GREEN, "Updated Holograms!"))
        return CommandResult.success()
    }
}
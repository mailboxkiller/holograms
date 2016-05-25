package de.randombyte.holograms.commands

import de.randombyte.holograms.Hologram
import de.randombyte.holograms.config.ConfigManager
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

class SpawnMultiLineTextHologramCommand : PlayerCommandExecutor() {
    override fun executedByPlayer(player: Player, args: CommandContext): CommandResult {
        val numberLines = args.getOne<Int>("numberOfLines").get()
        val hologram = Hologram.spawn(textListOfSize(numberLines), player.location)
        return if (hologram.isPresent) {
            ConfigManager.addHologram(player.world, hologram.get())
            CommandResult.success()
        } else {
            throw CommandException(Text.of("Couldn't spawn ArmorStand!"))
        }
    }

    fun textListOfSize(size: Int): List<Text> {
        val list = mutableListOf<Text>()
        (0..(size - 1)).forEach { list.add(Text.of("$it")) }
        return list
    }
}
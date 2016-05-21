package de.randombyte.holograms.commands

import de.randombyte.holograms.Hologram
import de.randombyte.holograms.config.ConfigManager
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.service.pagination.PaginationService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions
import org.spongepowered.api.text.format.TextColors

class ListNearbyHologramsCommand : PlayerCommandExecutor() {
    override fun executedByPlayer(player: Player, args: CommandContext): CommandResult {
        sendHologramList(player)
        return CommandResult.success()
    }

    companion object {
        fun sendHologramList(player: Player) {
            val hologramTextList = getHologramTextList(getNearbyHolograms(player, 10), deleteCallback = { hologram ->
                hologram.remove()
                ConfigManager.deleteHologramByArmorStandUUID(hologram.armorStandUUID!!)
                player.sendMessage(Text.of(TextColors.YELLOW, "Hologram deleted!"))
                sendHologramList(player) //Display new list
            })
            if (hologramTextList.size > 0) {
                Sponge.getServiceManager().provide(PaginationService::class.java).ifPresent {
                    it.builder()
                            .header(Text.of(TextColors.GREEN, "In 10 blocks range nearby Holograms:"))
                            .contents(hologramTextList)
                            .sendTo(player)
                }
            } else {
                player.sendMessage(Text.of(TextColors.YELLOW, "No Holograms in 10 blocks range!"))
            }
        }

        /**
         * @return A list of [Text] elements which represent the given [holograms]
         */
        fun getHologramTextList(holograms: List<Hologram>, deleteCallback: (Hologram) -> Unit) = holograms.map { hologram ->
            Text.builder("- \"").append(hologram.text).append(Text.of("\""))
                    .append(Text.builder(" [DELETE]")
                    .color(TextColors.RED)
                    .onClick(TextActions.executeCallback { deleteCallback.invoke(hologram) })
                    .build())
            .build()
        }

        fun getNearbyHolograms(player: Player, range: Int) = ConfigManager.getItemHolograms()
                .filter { it.location.position.distance(player.location.position) < range }
    }
}
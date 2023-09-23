package moe.paring.textdisplay.handler

import moe.paring.textdisplay.manager.TextDisplayManager
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import org.bukkit.entity.Player

class PlayerHandler(
    private val player: Player,
    private val manager: TextDisplayManager,
    private val plugin: TextDisplayPlugin
) {
    fun update() {
        manager.displays.values.forEach { display ->
            if (display.location.world === player.location.world) {
                val spawnDistanceSquared = plugin.spawnDistance.let { it * it }
                val despawnDistanceSquared = plugin.despawnDistance.let { it * it }

                val distanceSquared = display.location.distanceSquared(player.location)

                if (distanceSquared < despawnDistanceSquared) {
                    if (distanceSquared < spawnDistanceSquared && display.players.add(player)) {
                        display.spawnTo(player)
                    }

                    return
                }
            }

            if (display.players.remove(player)) {
                display.despawnTo(player)
            }
        }
    }
}

package moe.paring.textdisplay.handler

import moe.paring.textdisplay.manager.TextDisplayManager
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerHandler(
    private val player: Player,
    private val manager: TextDisplayManager,
    private val plugin: TextDisplayPlugin
) {
    fun update() {
        manager.displays.values.forEach { display ->
            run {
                if (display.config.location.world === player.location.world) {
                    val spawnDistanceSquared = plugin.spawnDistance.let { it * it }
                    val despawnDistanceSquared = plugin.despawnDistance.let { it * it }

                    val distanceSquared = display.config.location.distanceSquared(player.location)

                    if (distanceSquared < despawnDistanceSquared) {
                        if (distanceSquared < spawnDistanceSquared) {
                            if (display.players.add(player)) {
                                display.spawnTo(player)
                            } else if (display.config.updateInterval > 0 && Bukkit.getServer().currentTick % display.config.updateInterval == 0) {
                                display.updateFor(player)
                            }
                        }
                    }

                    return@run
                }

                if (display.players.remove(player)) {
                    display.despawnTo(player)
                }
            }
        }
    }
}

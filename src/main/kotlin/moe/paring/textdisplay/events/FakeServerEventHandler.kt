package moe.paring.textdisplay.events

import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class FakeServerEventHandler(private val fakeServer: FakeEntityServer) : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        fakeServer.addPlayer(e.player)
    }


    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        fakeServer.removePlayer(e.player)
    }
}
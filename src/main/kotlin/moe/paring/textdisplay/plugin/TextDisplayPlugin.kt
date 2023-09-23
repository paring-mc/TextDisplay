package moe.paring.textdisplay.plugin

import io.github.monun.tap.fake.FakeEntityServer
import moe.paring.textdisplay.command.registerCommands
import moe.paring.textdisplay.events.FakeServerEventHandler
import moe.paring.textdisplay.manager.TextDisplayManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class TextDisplayPlugin : JavaPlugin() {
    val textManager = TextDisplayManager(this)
//    lateinit var fake: FakeEntityServer

    override fun onEnable() {
//        fake = FakeEntityServer.create(this)

//        registerExistingPlayers()

//        server.pluginManager.registerEvents(FakeServerEventHandler(fake), this)

//        server.scheduler.runTaskTimer(this, Runnable {
//            fake.update()
//        }, 0L, 1L)

        registerCommands()
    }

//    private fun registerExistingPlayers() {
//        Bukkit.getOnlinePlayers().forEach { player ->
//            fake.addPlayer(player)
//        }
//    }
}

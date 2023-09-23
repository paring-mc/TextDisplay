package moe.paring.textdisplay.plugin

import io.github.monun.tap.util.UpToDateException
import io.github.monun.tap.util.updateFromGitHub
import me.clip.placeholderapi.PlaceholderAPI
import moe.paring.textdisplay.command.registerCommands
import moe.paring.textdisplay.events.DisplayManagerEventHandler
import moe.paring.textdisplay.manager.TextDisplayManager
import moe.paring.textdisplay.persistence.DisplayConfig
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.function.Consumer

class TextDisplayPlugin : JavaPlugin() {
    val textManager = TextDisplayManager(this)

    val spawnDistance: Double = 160.0
    val despawnDistance: Double = 180.0

    lateinit var textsDir: File

    companion object {
        lateinit var instance: TextDisplayPlugin
        var hasPlaceholderAPI: Boolean = false
    }

    override fun onEnable() {
        logger.info("Checking for updates...")
        updateFromGitHub("pikokr", "TextDisplay", "TextDisplay.jar") {
            onSuccess { logger.info("Update available! Download at $it") }
                .onFailure {
                    if (it is UpToDateException) return@onFailure
                    logger.info("Failed to check update: $it")
                }
        }

        instance = this
        hasPlaceholderAPI = server.pluginManager.getPlugin("PlaceholderAPI") != null

        textsDir = dataFolder.resolve("texts").apply {
            if (!exists()) mkdirs()
        }

        registerCommands()

        server.scheduler.runTaskTimer(this, Consumer { textManager.update() }, 0, 1)

        server.pluginManager.registerEvents(DisplayManagerEventHandler(textManager), this)

        loadTexts()
        registerOnlinePlayers()
    }

    fun loadTexts() {
        textsDir.listFiles()?.forEach {
            logger.info("Loading text from $it")
            val conf = YamlConfiguration.loadConfiguration(it)
            val config = DisplayConfig().apply { load(conf) }
            textManager.add(it.nameWithoutExtension, config)
        }
    }

    private fun unloadAll() {
        textManager.unloadAll()
    }

    private fun registerOnlinePlayers() {
        server.onlinePlayers.forEach { player ->
            textManager.addPlayer(player)
        }
    }

    fun reload() {
        unloadAll()
        loadTexts()
        registerOnlinePlayers()
    }

    override fun onDisable() {
        unloadAll()
    }
}

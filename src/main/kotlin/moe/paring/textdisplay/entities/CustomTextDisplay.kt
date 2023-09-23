package moe.paring.textdisplay.entities

import io.github.monun.tap.fake.FakeSupport
import io.github.monun.tap.fake.createSpawnPacket
import io.github.monun.tap.fake.tap
import io.github.monun.tap.loader.LibraryLoader
import io.github.monun.tap.protocol.PacketSupport
import io.github.monun.tap.protocol.sendPacket
import moe.paring.textdisplay.persistence.DisplayConfig
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import moe.paring.textdisplay.util.displayAttributes
import moe.paring.textdisplay.util.setPlaceholders
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f

class CustomTextDisplay(
    private val plugin: TextDisplayPlugin, val name: String, val config: DisplayConfig
) {
    val players = hashSetOf<Player>()

    private var loaded = false

    private val fakeSupportNMS = LibraryLoader.loadNMS(FakeSupport::class.java)

    var entity: TextDisplay? = null

    fun load() {
        require(!loaded)
        loaded = true

        entity = fakeSupportNMS.createEntity<TextDisplay>(TextDisplay::class.java, config.world).apply {
            tap().location = location
            backgroundColor = Color.fromARGB(0)

            transformation = Transformation(
                config.translation,
                config.leftRotation,
                config.scale,
                config.rightRotation
            )

            displayAttributes.forEach { attr ->
                attr.set(this, config.attributes[attr.name])
            }
        }
    }

    fun spawnTo(player: Player) {
        entity?.let { entity ->
            entity.createSpawnPacket().forEach {
                player.sendPacket(it)
            }

            player.sendPacket(PacketSupport.entityTeleport(entity, config.location))
        }
        updateFor(player)
    }

    fun updateFor(player: Player) {
        entity?.let { entity ->
            player.sendPacket(PacketSupport.entityMetadata(entity.apply {
                val content = config.text.setPlaceholders(player)
                text(runCatching { JSONComponentSerializer.json().deserialize(content) }.getOrElse {
                    LegacyComponentSerializer.legacyAmpersand().deserialize(content.replace("\\n", "\n"))
                })
            }))
        }
    }

    fun despawnTo(player: Player) {
        entity?.let {
            player.sendPacket(PacketSupport.removeEntity(it.entityId))
        }
    }

    fun unload() {
        players.forEach { despawnTo(it) }
        players.clear()
        entity?.remove()
        entity = null
        loaded = false
    }

    fun applyAndReload() {
        unload()
        load()

        // save
        saveConfig()
    }

    fun saveConfig() {
        val file = plugin.textsDir.resolve("$name.yml")
        YamlConfiguration().apply { config.store(this) }.save(file)
    }
}

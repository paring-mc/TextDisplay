package moe.paring.textdisplay.entities

import io.github.monun.tap.fake.FakeSupport
import io.github.monun.tap.fake.createSpawnPacket
import io.github.monun.tap.fake.tap
import io.github.monun.tap.loader.LibraryLoader
import io.github.monun.tap.protocol.PacketSupport
import io.github.monun.tap.protocol.sendPacket
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import moe.paring.textdisplay.util.setPlaceholders
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

class CustomTextDisplay(
    private val plugin: TextDisplayPlugin, val name: String, val location: Location, private val text: String
) {
    val players = hashSetOf<Player>()

    private var loaded = false

    private val fakeSupportNMS = LibraryLoader.loadNMS(FakeSupport::class.java)

    private var entity: TextDisplay? = null

    fun load() {
        require(!loaded)
        loaded = true

        entity = fakeSupportNMS.createEntity<TextDisplay>(TextDisplay::class.java, location.world).apply {
            tap().location = location
            billboard = Display.Billboard.CENTER
        }
    }

    fun spawnTo(player: Player) {
        entity?.let { entity ->
            entity.createSpawnPacket().forEach {
                player.sendPacket(it)
            }

            player.sendPacket(PacketSupport.entityTeleport(entity, location))
            player.sendPacket(PacketSupport.entityMetadata(entity.apply {
                val content = this@CustomTextDisplay.text.setPlaceholders(player)
                text(runCatching { JSONComponentSerializer.json().deserialize(content) }.getOrElse {
                    LegacyComponentSerializer.legacyAmpersand().deserialize(content)
                })
            }))
        }
    }

    fun despawnTo(player: Player) {
        entity?.let {
            player.sendPacket(PacketSupport.removeEntity(it.entityId))
        }
    }
}

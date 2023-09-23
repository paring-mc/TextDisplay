package moe.paring.textdisplay.entities

import io.github.monun.tap.fake.FakeEntity
import io.github.monun.tap.fake.FakeSupport
import io.github.monun.tap.fake.createSpawnPacket
import io.github.monun.tap.loader.LibraryLoader
import io.github.monun.tap.protocol.PacketContainer
import io.github.monun.tap.protocol.PacketSupport
import io.github.monun.tap.protocol.sendPacket
import moe.paring.textdisplay.plugin.TextDisplayPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

class CustomTextDisplay(
    private val plugin: TextDisplayPlugin, val name: String, private val location: Location, private val text: String
) {
    private var loaded = false

    private val fakeSupportNMS = LibraryLoader.loadNMS(FakeSupport::class.java)

    fun load() {
        require(!loaded)
        loaded = true

//        entity = plugin.fake.spawnEntity(location, TextDisplay::class.java).apply {
//            bukkitEntity.apply {
//                this.text(Component.text(this@CustomTextDisplay.text))
//            }
//        }

        addOnlinePlayers()
    }

    private fun addOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach { player ->
            plugin.logger.info("Adding $player")
            addPlayer(player)
        }
    }

    private fun addPlayer(player: Player) {
        buildCreatePacket(player).forEach { packet ->
            player.sendPacket(packet)
        }
    }

    private fun buildCreatePacket(player: Player): Array<out PacketContainer> {
        val targetLoc = this@CustomTextDisplay.location
        val entity = fakeSupportNMS.createEntity<TextDisplay>(TextDisplay::class.java, location.world).apply {
            location.x = targetLoc.x
            location.y = targetLoc.y
            location.z = targetLoc.z
            location.pitch = targetLoc.pitch
            location.yaw = targetLoc.yaw

            text(Component.text("wow this is test something"))
        }

        return entity.createSpawnPacket()
    }
}

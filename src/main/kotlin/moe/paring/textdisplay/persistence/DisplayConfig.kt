package moe.paring.textdisplay.persistence

import moe.paring.textdisplay.util.displayAttributes
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.joml.AxisAngle4f
import org.joml.Vector3f
import org.joml.Vector4f

class DisplayConfig {
    var text: String = ""

    var worldName: String = ""
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    var yaw: Float = 0f
    var pitch: Float = 0f
    var updateInterval: Int = 0
    var scale: Vector3f = Vector3f(1f, 1f, 1f)
    var translation: Vector3f = Vector3f(0f, 0f, 0f)
    var leftRotation: AxisAngle4f = AxisAngle4f(0f, 0f, 0f, 0f)
    var rightRotation: AxisAngle4f = AxisAngle4f(0f, 0f, 0f, 0f)

    val attributes = mutableMapOf<String, Any>()

    val world: World
        get() = Bukkit.getWorld(worldName) ?: error("world $worldName not found")
    val location: Location
        get() = Location(world, x, y, z, yaw, pitch)

    init {
        displayAttributes.forEach { attr -> attributes[attr.name] = attr.defaultValue as Any }
    }

    fun load(config: YamlConfiguration) {
        text = config.getString("text") ?: error("text is not set")
        worldName = config.getString("world") ?: error("world is not set")
        x = config.getDouble("x")
        y = config.getDouble("y")
        z = config.getDouble("z")
        yaw = config.getDouble("yaw").toFloat()
        pitch = config.getDouble("pitch").toFloat()
        updateInterval = config.getInt("updateInterval", 20)

        scale = config.getFloatList("scale").toVector3f(Vector3f(1f, 1f, 1f))
        translation = config.getFloatList("translation").toVector3f()
        leftRotation = config.getFloatList("leftRotation").toAxisAngle4f()
        rightRotation = config.getFloatList("rightRotation").toAxisAngle4f()

        displayAttributes.forEach { attr ->
            config.get(attr.name)?.let { attr.decode(it) }?.let { attributes[attr.name] = it }
        }
    }

    fun store(config: YamlConfiguration) {
        config.set("text", text)
        config.set("world", worldName)
        config.set("x", x)
        config.set("y", y)
        config.set("z", z)
        config.set("yaw", yaw)
        config.set("pitch", pitch)
        config.set("updateInverval", updateInterval)

        config.set("scale", scale.toFloatList())
        config.set("translation", translation.toFloatList())
        config.set("leftRotation", leftRotation.toFloatList())
        config.set("rightRotation", rightRotation.toFloatList())

        displayAttributes.forEach {
            config.set(it.name, it.encodeAny(attributes[it.name]!!))
        }
    }

    // utils
    private fun List<Float>.toVector3f(default: Vector3f = Vector3f(0f, 0f, 0f)): Vector3f {
        if (size != 3) {
            return default
        }
        return Vector3f(this[0], this[1], this[2])
    }

    private fun List<Float>.toAxisAngle4f(default: AxisAngle4f = AxisAngle4f(0f, 0f, 0f, 0f)): AxisAngle4f {
        if (size != 4) {
            return default
        }
        return AxisAngle4f(this[0], this[1], this[2], this[3])
    }

    private fun Vector3f.toFloatList(): List<Float> {
        return listOf(x, y, z)
    }

    private fun AxisAngle4f.toFloatList(): List<Float> {
        return listOf(x, y, z, angle)
    }
}

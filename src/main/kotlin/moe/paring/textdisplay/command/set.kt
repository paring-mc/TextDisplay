package moe.paring.textdisplay.command

import io.github.monun.kommand.KommandSource
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import io.github.monun.kommand.node.RootNode
import io.github.monun.kommand.wrapper.Position3D
import io.github.monun.kommand.wrapper.Rotation
import moe.paring.textdisplay.entities.CustomTextDisplay
import moe.paring.textdisplay.util.display
import moe.paring.textdisplay.util.displayAttributes
import moe.paring.textdisplay.util.displayInfo
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.joml.AxisAngle4f
import org.joml.Vector3f

fun RootNode.set() {
    then("set") {
        requires { sender.hasPermission("textdisplay.command.set") }

        then("display" to display) {
            fun KommandSource.sendUpdateLog(attr: String, display: CustomTextDisplay, value: String) {
                broadcast(
                    Component
                        .text()
                        .append(Component.text("Changed "))
                        .append(Component.text(attr).color(NamedTextColor.YELLOW)
                            .hoverEvent(HoverEvent.showText(Component.text("Copy value to clipboard"))).clickEvent(ClickEvent.copyToClipboard(value)))
                        .append(Component.text(" of display "))
                        .append(Component.text(display.name).displayInfo(display))
                        .append(Component.text("."))
                ) { isOp }
            }

            then("text") {
                then("value" to string(StringType.GREEDY_PHRASE)) {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val value: String by context

                        display.config.text = value

                        display.applyAndReload()

                        sendUpdateLog("text", display, value)
                    }
                }
            }

            then("position") {
                then("value" to position()) {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val value: Position3D by context

                        display.config.x = value.x
                        display.config.y = value.y
                        display.config.z = value.z

                        display.applyAndReload()

                        sendUpdateLog("position", display, "${value.x} ${value.y} ${value.z}")
                    }
                }
            }

            then("rotation") {
                then("value" to rotation()) {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val value: Rotation by context

                        display.config.pitch = value.pitch
                        display.config.yaw = value.yaw

                        display.applyAndReload()

                        sendUpdateLog("rotation", display, "(${value.pitch}, ${value.yaw})")
                    }
                }
            }

            then("updateInterval") {
                then("value" to int()) {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val value: Int by context

                        display.config.updateInterval = value

                        sendUpdateLog("updateInterval", display, "$value")

                        display.saveConfig()
                    }
                }
            }

            fun KommandNode.v3f(init: KommandNode.() -> Unit) {
                then("x" to float()) {
                    then("y" to float()) {
                        then("z" to float()) {
                            init()
                        }
                    }
                }
            }

            fun KommandNode.a4f(init: KommandNode.() -> Unit) {
                v3f {
                    then("angle" to float()) {
                        init()
                    }
                }
            }

            then("scale") {
                v3f {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val x: Float by context
                        val y: Float by context
                        val z: Float by context

                        display.config.scale = Vector3f(x, y, z)

                        display.applyAndReload()

                        sendUpdateLog("scale", display, "($x, $y, $z)")
                    }
                }
            }

            then("translation") {
                v3f {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val x: Float by context
                        val y: Float by context
                        val z: Float by context

                        display.config.translation = Vector3f(x, y, z)

                        display.applyAndReload()

                        sendUpdateLog("translation", display, "($x, $y, $z)")
                    }
                }
            }

            then("leftRotation") {
                a4f {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val x: Float by context
                        val y: Float by context
                        val z: Float by context
                        val angle: Float by context

                        display.config.leftRotation = AxisAngle4f(x, y, z, angle)

                        display.applyAndReload()

                        sendUpdateLog("leftRotation", display, "($x, $y, $z, $angle)")
                    }
                }
            }

            then("rightRotation") {
                a4f {
                    executes { context ->
                        val display: CustomTextDisplay by context
                        val x: Float by context
                        val y: Float by context
                        val z: Float by context
                        val angle: Float by context

                        display.config.rightRotation = AxisAngle4f(x, y, z, angle)

                        display.applyAndReload()

                        sendUpdateLog("rightRotation", display, "($x, $y, $z, $angle)")
                    }
                }
            }

            displayAttributes.forEach { attr ->
                then(attr.name) {
                    then("value" to attr.argType) {
                        executes { context ->
                            val display: CustomTextDisplay by context
                            val value = context.get<Any>("value")
                            display.config.attributes[attr.name] = value
                            attr.set(display.entity!!, value)

                            display.applyAndReload()

                            sendUpdateLog(attr.name, display, "$value")
                        }
                    }
                }
            }
        }
    }
}

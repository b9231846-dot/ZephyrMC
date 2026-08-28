package com.project.zephyr.client.game.module.impl.motion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.project.zephyr.client.game.InterceptablePacket
import com.project.zephyr.client.constructors.Element
import com.project.zephyr.client.constructors.CheatCategory
import com.project.zephyr.client.util.AssetManager
import org.cloudburstmc.protocol.bedrock.packet.UpdateAbilitiesPacket

class MotionVarElement : Element("_var_", CheatCategory.Motion) {

    init {
        isEnabled = true
    }

    companion object {
        var lastUpdateAbilitiesPacket: UpdateAbilitiesPacket? by mutableStateOf(null)
    }

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        if (interceptablePacket.packet is UpdateAbilitiesPacket) {
            lastUpdateAbilitiesPacket = interceptablePacket.packet
        }
    }

}
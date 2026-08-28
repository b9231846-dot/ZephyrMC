package com.project.zephyr.client.game.module.impl.motion

import com.project.zephyr.client.R
import com.project.zephyr.client.game.InterceptablePacket
import com.project.zephyr.client.constructors.Element
import com.project.zephyr.client.constructors.CheatCategory
import com.project.zephyr.client.util.AssetManager
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket


class SprintElement(iconResId: Int = AssetManager.getAsset("ic_run_fast_black_24dp")) : Element(
    name = "Sprint",
    category = CheatCategory.Motion,
    iconResId,
    displayNameResId = AssetManager.getString("module_sprint_display_name")
) {
    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        val packet = interceptablePacket.packet

        if (packet is PlayerAuthInputPacket && isEnabled) {
            packet.inputData.add(PlayerAuthInputData.SPRINTING)
            packet.inputData.add(PlayerAuthInputData.START_SPRINTING)
        } else if (packet is PlayerAuthInputPacket && !isEnabled) {
            packet.inputData.add(PlayerAuthInputData.STOP_SPRINTING)
        }
    }
}
package com.project.zephyr.relay.listener

import com.project.zephyr.relay.ZephyrRelaySession
import com.project.zephyr.relay.definition.CameraPresetDefinition
import com.project.zephyr.relay.definition.Definitions
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
import org.cloudburstmc.protocol.bedrock.packet.CameraPresetsPacket
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket

@Suppress("MemberVisibilityCanBePrivate")
class GamingPacketHandler(
    val ZephyrRelaySession: ZephyrRelaySession
) : ZephyrRelayPacketListener {

    override fun beforeServerBound(packet: BedrockPacket): Boolean {
        if (packet is StartGamePacket) {
            Definitions.itemDefinitions = SimpleDefinitionRegistry.builder<ItemDefinition>()
                .addAll(packet.itemDefinitions)
                .build()

            ZephyrRelaySession.client!!.peer.codecHelper.itemDefinitions = Definitions.itemDefinitions
            ZephyrRelaySession.server.peer.codecHelper.itemDefinitions = Definitions.itemDefinitions

            if (packet.isBlockNetworkIdsHashed) {
                ZephyrRelaySession.client!!.peer.codecHelper.blockDefinitions = Definitions.blockDefinitionsHashed
                ZephyrRelaySession.server.peer.codecHelper.blockDefinitions = Definitions.blockDefinitionsHashed
            } else {
                ZephyrRelaySession.client!!.peer.codecHelper.blockDefinitions = Definitions.blockDefinitions
                ZephyrRelaySession.server.peer.codecHelper.blockDefinitions = Definitions.blockDefinitions
            }
        }
        if (packet is CameraPresetsPacket) {
            val cameraDefinitions =
                SimpleDefinitionRegistry.builder<NamedDefinition>()
                    .addAll(List(packet.presets.size) {
                        CameraPresetDefinition.fromCameraPreset(packet.presets[it], it)
                    })
                    .build()

            ZephyrRelaySession.client!!.peer.codecHelper.cameraPresetDefinitions = cameraDefinitions
            ZephyrRelaySession.server.peer.codecHelper.cameraPresetDefinitions = cameraDefinitions
        }
        return false
    }

}
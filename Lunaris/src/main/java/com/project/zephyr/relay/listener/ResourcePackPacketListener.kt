package com.project.zephyr.relay.listener

import com.project.zephyr.relay.ZephyrRelaySession
import com.project.zephyr.relay.ZephyrLogger
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackChunkDataPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackChunkRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackClientResponsePacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackDataInfoPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackStackPacket

class ResourcePackPacketListener(
    val ZephyrRelaySession: ZephyrRelaySession
) : ZephyrRelayPacketListener {

    companion object {
        private const val TAG = "ResourcePackListener"
    }

    private val logger: ZephyrLogger
        get() = ZephyrRelaySession.logger

    override fun beforeClientBound(packet: BedrockPacket): Boolean {
        return when (packet) {
            is ResourcePacksInfoPacket -> {
                logger.i(TAG, "ResourcePacksInfoPacket: ${packet.resourcePackInfos.size} packs")
                false
            }
            is ResourcePackDataInfoPacket -> {
                logger.i(TAG, "ResourcePackDataInfoPacket")
                false
            }
            is ResourcePackChunkDataPacket -> {
                logger.i(TAG, "ResourcePackChunkData: chunkIndex=${packet.chunkIndex}")
                false
            }
            is ResourcePackStackPacket -> {
                logger.i(TAG, "ResourcePackStackPacket: ${packet.resourcePacks.size} packs")
                false
            }
            else -> false
        }
    }

    override fun beforeServerBound(packet: BedrockPacket): Boolean {
        return when (packet) {
            is ResourcePackChunkRequestPacket -> {
                logger.i(TAG, "Client chunk request: ${packet.packId}")
                false
            }
            is ResourcePackClientResponsePacket -> {
                logger.i(TAG, "Client response: ${packet.status}")
                false
            }
            else -> false
        }
    }

    override fun onDisconnect(reason: String) {
        logger.w(TAG, "Disconnected: $reason")
    }
}

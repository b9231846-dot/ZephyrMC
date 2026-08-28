package com.project.zephyr.relay.listener

import com.project.zephyr.relay.ZephyrRelaySession
import com.project.zephyr.relay.definition.Definitions
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.BiomeDefinitionListSerializer_v313
import org.cloudburstmc.protocol.bedrock.codec.v729.serializer.InventoryContentSerializer_v729
import org.cloudburstmc.protocol.bedrock.codec.v729.serializer.InventorySlotSerializer_v729
import org.cloudburstmc.protocol.bedrock.data.EncodingSettings
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm
import org.cloudburstmc.protocol.bedrock.packet.*

@Suppress("MemberVisibilityCanBePrivate")
class AutoCodecPacketListener(
    val ZephyrRelaySession: ZephyrRelaySession,
    val patchCodec: Boolean = true
) : ZephyrRelayPacketListener {

    companion object {
        init {
            System.loadLibrary("lunaris")
        }

        @JvmStatic external fun pickProtocolCodec(protocolVersion: Int): BedrockCodec
    }

    private fun patchCodecIfNeeded(codec: BedrockCodec): BedrockCodec {
        return if (patchCodec && codec.protocolVersion > 729) {
            codec.toBuilder()
                .updateSerializer(InventoryContentPacket::class.java, InventoryContentSerializer_v729.INSTANCE)
                .updateSerializer(InventorySlotPacket::class.java, InventorySlotSerializer_v729.INSTANCE)
                .updateSerializer(BiomeDefinitionListPacket::class.java, BiomeDefinitionListSerializer_v313.INSTANCE)
                .build()
        } else {
            codec
        }
    }

    override fun beforeClientBound(packet: BedrockPacket): Boolean {
        if (packet is RequestNetworkSettingsPacket) {
            val protocolVersion = packet.protocolVersion
            val bedrockCodec = patchCodecIfNeeded(pickProtocolCodec(protocolVersion))
            ZephyrRelaySession.server.codec = bedrockCodec
            ZephyrRelaySession.server.peer.codecHelper.apply {
                itemDefinitions = Definitions.itemDefinitions
                blockDefinitions = Definitions.blockDefinitions
                cameraPresetDefinitions = Definitions.cameraPresetDefinitions
                encodingSettings = EncodingSettings.builder()
                    .maxListSize(Int.MAX_VALUE)
                    .maxByteArraySize(Int.MAX_VALUE)
                    .maxNetworkNBTSize(Int.MAX_VALUE)
                    .maxItemNBTSize(Int.MAX_VALUE)
                    .maxStringLength(Int.MAX_VALUE)
                    .build()
            }

            val networkSettingsPacket = NetworkSettingsPacket()
            networkSettingsPacket.compressionThreshold = 0
            networkSettingsPacket.compressionAlgorithm = PacketCompressionAlgorithm.ZLIB

            ZephyrRelaySession.clientBoundImmediately(networkSettingsPacket)
            ZephyrRelaySession.server.setCompression(PacketCompressionAlgorithm.ZLIB)
            return true
        }
        return false
    }
}
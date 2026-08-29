package com.project.zephyr.relay.listener

import com.project.zephyr.relay.ZephyrRelaySession
import com.project.zephyr.relay.definition.Definitions
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec
import org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291
import org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313
import org.cloudburstmc.protocol.bedrock.codec.v332.Bedrock_v332
import org.cloudburstmc.protocol.bedrock.codec.v340.Bedrock_v340
import org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361
import org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388
import org.cloudburstmc.protocol.bedrock.codec.v390.Bedrock_v390
import org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407
import org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419
import org.cloudburstmc.protocol.bedrock.codec.v422.Bedrock_v422
import org.cloudburstmc.protocol.bedrock.codec.v428.Bedrock_v428
import org.cloudburstmc.protocol.bedrock.codec.v431.Bedrock_v431
import org.cloudburstmc.protocol.bedrock.codec.v440.Bedrock_v440
import org.cloudburstmc.protocol.bedrock.codec.v448.Bedrock_v448
import org.cloudburstmc.protocol.bedrock.codec.v465.Bedrock_v465
import org.cloudburstmc.protocol.bedrock.codec.v471.Bedrock_v471
import org.cloudburstmc.protocol.bedrock.codec.v486.Bedrock_v486
import org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503
import org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527
import org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534
import org.cloudburstmc.protocol.bedrock.codec.v554.Bedrock_v554
import org.cloudburstmc.protocol.bedrock.codec.v557.Bedrock_v557
import org.cloudburstmc.protocol.bedrock.codec.v568.Bedrock_v568
import org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575
import org.cloudburstmc.protocol.bedrock.codec.v622.Bedrock_v622
import org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630
import org.cloudburstmc.protocol.bedrock.codec.v712.Bedrock_v712
import org.cloudburstmc.protocol.bedrock.codec.v729.Bedrock_v729
import org.cloudburstmc.protocol.bedrock.codec.v748.Bedrock_v748
import org.cloudburstmc.protocol.bedrock.codec.v766.Bedrock_v766
import org.cloudburstmc.protocol.bedrock.codec.v776.Bedrock_v776
import org.cloudburstmc.protocol.bedrock.codec.v786.Bedrock_v786
import org.cloudburstmc.protocol.bedrock.codec.v800.Bedrock_v800
import org.cloudburstmc.protocol.bedrock.codec.v818.Bedrock_v818
import org.cloudburstmc.protocol.bedrock.codec.v819.Bedrock_v819
import org.cloudburstmc.protocol.bedrock.codec.v827.Bedrock_v827
import org.cloudburstmc.protocol.bedrock.codec.v844.Bedrock_v844
import org.cloudburstmc.protocol.bedrock.codec.v859.Bedrock_v859
import org.cloudburstmc.protocol.bedrock.codec.v860.Bedrock_v860
import org.cloudburstmc.protocol.bedrock.codec.v898.Bedrock_v898
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

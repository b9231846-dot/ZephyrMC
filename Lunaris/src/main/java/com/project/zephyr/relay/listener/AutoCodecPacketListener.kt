package com.project.zephyr.relay.listener

import com.project.zephyr.relay.ZephyrRelaySession
import com.project.zephyr.relay.definition.Definitions
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec
import org.cloudburstmc.protocol.bedrock.codec.v291.Bedrock_v291
import org.cloudburstmc.protocol.bedrock.codec.v313.Bedrock_v313
import org.cloudburstmc.protocol.bedrock.codec.v332.Bedrock_v332
import org.cloudburstmc.protocol.bedrock.codec.v340.Bedrock_v340
import org.cloudburstmc.protocol.bedrock.codec.v354.Bedrock_v354
import org.cloudburstmc.protocol.bedrock.codec.v361.Bedrock_v361
import org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388
import org.cloudburstmc.protocol.bedrock.codec.v389.Bedrock_v389
import org.cloudburstmc.protocol.bedrock.codec.v390.Bedrock_v390
import org.cloudburstmc.protocol.bedrock.codec.v407.Bedrock_v407
import org.cloudburstmc.protocol.bedrock.codec.v408.Bedrock_v408
import org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419
import org.cloudburstmc.protocol.bedrock.codec.v422.Bedrock_v422
import org.cloudburstmc.protocol.bedrock.codec.v428.Bedrock_v428
import org.cloudburstmc.protocol.bedrock.codec.v431.Bedrock_v431
import org.cloudburstmc.protocol.bedrock.codec.v440.Bedrock_v440
import org.cloudburstmc.protocol.bedrock.codec.v448.Bedrock_v448
import org.cloudburstmc.protocol.bedrock.codec.v465.Bedrock_v465
import org.cloudburstmc.protocol.bedrock.codec.v471.Bedrock_v471
import org.cloudburstmc.protocol.bedrock.codec.v475.Bedrock_v475
import org.cloudburstmc.protocol.bedrock.codec.v486.Bedrock_v486
import org.cloudburstmc.protocol.bedrock.codec.v503.Bedrock_v503
import org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527
import org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534
import org.cloudburstmc.protocol.bedrock.codec.v544.Bedrock_v544
import org.cloudburstmc.protocol.bedrock.codec.v545.Bedrock_v545
import org.cloudburstmc.protocol.bedrock.codec.v554.Bedrock_v554
import org.cloudburstmc.protocol.bedrock.codec.v557.Bedrock_v557
import org.cloudburstmc.protocol.bedrock.codec.v560.Bedrock_v560
import org.cloudburstmc.protocol.bedrock.codec.v567.Bedrock_v567
import org.cloudburstmc.protocol.bedrock.codec.v568.Bedrock_v568
import org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575
import org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582
import org.cloudburstmc.protocol.bedrock.codec.v589.Bedrock_v589
import org.cloudburstmc.protocol.bedrock.codec.v594.Bedrock_v594
import org.cloudburstmc.protocol.bedrock.codec.v618.Bedrock_v618
import org.cloudburstmc.protocol.bedrock.codec.v622.Bedrock_v622
import org.cloudburstmc.protocol.bedrock.codec.v630.Bedrock_v630
import org.cloudburstmc.protocol.bedrock.codec.v649.Bedrock_v649
import org.cloudburstmc.protocol.bedrock.codec.v662.Bedrock_v662
import org.cloudburstmc.protocol.bedrock.codec.v671.Bedrock_v671
import org.cloudburstmc.protocol.bedrock.codec.v685.Bedrock_v685
import org.cloudburstmc.protocol.bedrock.codec.v686.Bedrock_v686
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
import org.cloudburstmc.protocol.bedrock.codec.v897.Bedrock_v897
import org.cloudburstmc.protocol.bedrock.codec.v924.Bedrock_v924
import org.cloudburstmc.protocol.bedrock.codec.v944.Bedrock_v944
import org.cloudburstmc.protocol.bedrock.codec.v975.Bedrock_v975
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
        fun pickProtocolCodec(protocolVersion: Int): BedrockCodec {
            return when (protocolVersion) {
                975 -> Bedrock_v975.CODEC
                944 -> Bedrock_v944.CODEC
                924 -> Bedrock_v924.CODEC
                897 -> Bedrock_v897.CODEC
                860 -> Bedrock_v860.CODEC
                859 -> Bedrock_v859.CODEC
                844 -> Bedrock_v844.CODEC
                827 -> Bedrock_v827.CODEC
                819 -> Bedrock_v819.CODEC
                818 -> Bedrock_v818.CODEC
                800 -> Bedrock_v800.CODEC
                786 -> Bedrock_v786.CODEC
                776 -> Bedrock_v776.CODEC
                766 -> Bedrock_v766.CODEC
                748 -> Bedrock_v748.CODEC
                729 -> Bedrock_v729.CODEC
                712 -> Bedrock_v712.CODEC
                686 -> Bedrock_v686.CODEC
                685 -> Bedrock_v685.CODEC
                671 -> Bedrock_v671.CODEC
                662 -> Bedrock_v662.CODEC
                649 -> Bedrock_v649.CODEC
                630 -> Bedrock_v630.CODEC
                622 -> Bedrock_v622.CODEC
                618 -> Bedrock_v618.CODEC
                594 -> Bedrock_v594.CODEC
                589 -> Bedrock_v589.CODEC
                582 -> Bedrock_v582.CODEC
                575 -> Bedrock_v575.CODEC
                568 -> Bedrock_v568.CODEC
                567 -> Bedrock_v567.CODEC
                560 -> Bedrock_v560.CODEC
                557 -> Bedrock_v557.CODEC
                554 -> Bedrock_v554.CODEC
                545 -> Bedrock_v545.CODEC
                544 -> Bedrock_v544.CODEC
                534 -> Bedrock_v534.CODEC
                527 -> Bedrock_v527.CODEC
                503 -> Bedrock_v503.CODEC
                486 -> Bedrock_v486.CODEC
                475 -> Bedrock_v475.CODEC
                471 -> Bedrock_v471.CODEC
                465 -> Bedrock_v465.CODEC
                448 -> Bedrock_v448.CODEC
                440 -> Bedrock_v440.CODEC
                431 -> Bedrock_v431.CODEC
                428 -> Bedrock_v428.CODEC
                422 -> Bedrock_v422.CODEC
                419 -> Bedrock_v419.CODEC
                408 -> Bedrock_v408.CODEC
                407 -> Bedrock_v407.CODEC
                390 -> Bedrock_v390.CODEC
                389 -> Bedrock_v389.CODEC
                388 -> Bedrock_v388.CODEC
                361 -> Bedrock_v361.CODEC
                354 -> Bedrock_v354.CODEC
                340 -> Bedrock_v340.CODEC
                332 -> Bedrock_v332.CODEC
                313 -> Bedrock_v313.CODEC
                291 -> Bedrock_v291.CODEC
                else -> Bedrock_v975.CODEC
            }
        }
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

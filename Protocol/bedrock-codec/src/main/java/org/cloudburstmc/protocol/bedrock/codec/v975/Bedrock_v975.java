package org.cloudburstmc.protocol.bedrock.codec.v975;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v898.Bedrock_v898;
import org.cloudburstmc.protocol.bedrock.codec.v898.BedrockCodecHelper_v898;

public class Bedrock_v975 extends Bedrock_v898 {

    public static final BedrockCodec CODEC = Bedrock_v898.CODEC.toBuilder()
            .protocolVersion(975)
            .minecraftVersion("1.26.20")
            .helper(() -> new BedrockCodecHelper_v975(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS))
            .build();
}

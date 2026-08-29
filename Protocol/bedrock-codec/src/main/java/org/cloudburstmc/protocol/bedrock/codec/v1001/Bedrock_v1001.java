package org.cloudburstmc.protocol.bedrock.codec.v1001;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v975.Bedrock_v975;
import org.cloudburstmc.protocol.bedrock.codec.v975.BedrockCodecHelper_v975;

public class Bedrock_v1001 extends Bedrock_v975 {

    public static final BedrockCodec CODEC = Bedrock_v975.CODEC.toBuilder()
            .protocolVersion(1001)
            .minecraftVersion("1.26.30")
            .helper(() -> new BedrockCodecHelper_v1001(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS))
            .build();
}

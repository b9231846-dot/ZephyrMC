package org.cloudburstmc.protocol.bedrock.codec.v2168;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.v1001.Bedrock_v1001;
import org.cloudburstmc.protocol.bedrock.codec.v1001.BedrockCodecHelper_v1001;

public class Bedrock_v2168 extends Bedrock_v1001 {

    public static final BedrockCodec CODEC = Bedrock_v1001.CODEC.toBuilder()
            .protocolVersion(2168)
            .minecraftVersion("1.26.40")
            .helper(() -> new BedrockCodecHelper_v2168(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS))
            .build();
}

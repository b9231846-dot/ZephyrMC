package com.project.zephyr.relay.handler

import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler

class SessionCloseHandler(private val onSessionClose: (String) -> Unit) : BedrockPacketHandler {

    override fun onDisconnect(reason: CharSequence) {
        // Convert CharSequence to String
        onSessionClose(reason.toString())
    }
}
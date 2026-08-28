package com.project.zephyr.relay.util

import com.project.zephyr.relay.ZephyrRelay
import com.project.zephyr.relay.ZephyrRelaySession
import com.project.zephyr.relay.address.ZephyrAddress
import org.cloudburstmc.protocol.bedrock.BedrockPong

/**
 * Creates a ZephyrRelay instance in capture mode that will connect to the specified server.
 * This function automatically sets up the relay to capture packets between the client and server.
 * 
 * @param advertisement The server advertisement details used for client connections
 * @param localAddress The local address the relay will bind to
 * @param remoteAddress The remote server address to connect to
 * @param onSessionCreated Callback executed when a relay session is created
 * @return A configured ZephyrRelay instance
 */
fun captureZephyrRelay(
    advertisement: BedrockPong = ZephyrRelay.createNativeAdvertisement(),
    localAddress: ZephyrAddress = ZephyrAddress("0.0.0.0", 19132),
    remoteAddress: ZephyrAddress,
    onSessionCreated: ZephyrRelaySession.() -> Unit
): ZephyrRelay {
    return ZephyrRelay(
        localAddress = localAddress,
        advertisement = advertisement
    ).capture(
        remoteAddress = remoteAddress,
        onSessionCreated = onSessionCreated
    )
}
package com.project.zephyr.relay.listener

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.project.zephyr.relay.ZephyrRelaySession
import com.project.zephyr.relay.util.*
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm
import org.cloudburstmc.protocol.bedrock.data.auth.AuthType
import org.cloudburstmc.protocol.bedrock.data.auth.CertificateChainPayload
import org.cloudburstmc.protocol.bedrock.packet.*
import org.cloudburstmc.protocol.bedrock.util.EncryptionUtils
import java.security.KeyPair
import java.util.Base64

open class EncryptedLoginPacketListener : ZephyrRelayPacketListener {

    protected var keyPair: KeyPair = EncryptionUtils.createKeyPair()
    protected var loginPacket: LoginPacket? = null
    lateinit var ZephyrRelaySession: ZephyrRelaySession

    override fun beforeClientBound(packet: BedrockPacket): Boolean {
        if (packet is LoginPacket) {
            try {

                val extraData = extractExtraData(packet)

                val newChain = forgeAuthData(keyPair, extraData)

                packet.authPayload = CertificateChainPayload(listOf(newChain), AuthType.SELF_SIGNED)

                val skinData = extractSkinData(packet)
                packet.clientJwt = forgeSkinData(keyPair, skinData)

            } catch (e: Exception) {
                e.printStackTrace()
                ZephyrRelaySession.server.disconnect("Authentication failed")
                return false
            }

            loginPacket = packet
            connectServer()
            return true
        }
        return false
    }

    override fun beforeServerBound(packet: BedrockPacket): Boolean {
        when (packet) {
            is NetworkSettingsPacket -> {
                val threshold = packet.compressionThreshold
                if (threshold > 0) {
                    ZephyrRelaySession.client!!.setCompression(packet.compressionAlgorithm)
                } else {
                    ZephyrRelaySession.client!!.setCompression(PacketCompressionAlgorithm.NONE)
                }


                loginPacket?.let {
                    ZephyrRelaySession.serverBoundImmediately(it)
                } ?: run {
                    ZephyrRelaySession.server.disconnect("LoginPacket is null")
                }
                return true
            }

            is ServerToClientHandshakePacket -> {
                try {

                    val jwtSplit = packet.jwt.split(".")
                    val headerObject = JsonParser.parseString(
                        base64Decode(jwtSplit[0]).toString(Charsets.UTF_8)
                    ).asJsonObject
                    val payloadObject = JsonParser.parseString(
                        base64Decode(jwtSplit[1]).toString(Charsets.UTF_8)
                    ).asJsonObject

                    val serverKey = EncryptionUtils.parseKey(headerObject.get("x5u").asString)


                    val key = EncryptionUtils.getSecretKey(
                        keyPair.private,
                        serverKey,
                        base64Decode(payloadObject.get("salt").asString)
                    )


                    ZephyrRelaySession.client!!.enableEncryption(key)


                    ZephyrRelaySession.serverBoundImmediately(ClientToServerHandshakePacket())
                } catch (e: Exception) {
                    e.printStackTrace()
                    ZephyrRelaySession.server.disconnect("Encryption handshake failed")
                }
                return true
            }
        }
        return false
    }

    protected fun connectServer() {
        ZephyrRelaySession.zephyrRelay.connectToServer {

            val packet = RequestNetworkSettingsPacket()
            packet.protocolVersion = ZephyrRelaySession.server.codec.protocolVersion
            ZephyrRelaySession.serverBoundImmediately(packet)
        }
    }


    private fun extractExtraData(packet: LoginPacket): JsonObject {
        val extraData = JsonObject()

        if (packet.authPayload is CertificateChainPayload) {
            val chain = (packet.authPayload as CertificateChainPayload).chain


            chain.forEach { jwt ->
                val payload = jwtPayload(jwt)
                if (payload?.has("extraData") == true) {
                    val data = payload.getAsJsonObject("extraData")

                    data.entrySet().forEach { (key, value) ->
                        extraData.add(key, value)
                    }
                    return extraData
                }
            }
        }


        extraData.addProperty("displayName", "ZephyrUser")
        extraData.addProperty("identity", "00000000-0000-0000-0000-000000000000")

        return extraData
    }


    private fun extractSkinData(packet: LoginPacket): JsonObject {
        return jwtPayload(packet.clientJwt ?: "")
            ?: JsonObject().apply {
                addProperty("SkinId", "")
                addProperty("SkinData", "")
            }
    }
}
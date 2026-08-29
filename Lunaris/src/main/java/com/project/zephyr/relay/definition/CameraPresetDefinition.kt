package com.project.zephyr.relay.definition

import org.cloudburstmc.protocol.bedrock.data.camera.CameraPreset


data class CameraPresetDefinition(
    private val identifier: String,
    private val runtimeId: Int
) : NamedDefinition {

    companion object {
        fun fromCameraPreset(preset: CameraPreset, index: Int): CameraPresetDefinition {
            return CameraPresetDefinition(preset.identifier, index)
        }
    }

    override fun getRuntimeId(): Int {
        return runtimeId
    }

    override fun getIdentifier(): String {
        return identifier
    }

}
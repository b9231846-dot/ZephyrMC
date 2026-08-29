package com.project.zephyr.relay.definition

import com.project.zephyr.relay.util.BlockPaletteUtils
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition


class NbtBlockDefinitionRegistry(
    definitions: List<NbtMap>,
    hashed: Boolean
) : DefinitionRegistry<BlockDefinition> {

    private val definitions = Int2ObjectOpenHashMap<NbtBlockDefinition>()

    init {
        var counter = 0
        for (definition in definitions) {
            val runtimeId = if (hashed) BlockPaletteUtils.createHash(definition) else counter++
            this.definitions.put(runtimeId, NbtBlockDefinition(runtimeId, definition))
        }
    }

    override fun getDefinition(runtimeId: Int): BlockDefinition? {
        return definitions.get(runtimeId)
    }

    override fun getDefinition(identifier: String): BlockDefinition? {
        return definitions.values.find { it.identifier == identifier }
    }

    override fun isRegistered(definition: BlockDefinition): Boolean {
        return definitions.get(definition.runtimeId) == definition
    }

    @JvmRecord
    data class NbtBlockDefinition(val runtimeId: Int, val tag: NbtMap, val identifier: String = "") : BlockDefinition {
        override fun getRuntimeId(): Int {
            return runtimeId
        }
    }

}
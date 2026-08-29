package com.project.zephyr.relay.definition

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.cloudburstmc.protocol.common.Definition as CloudburstDefinition
import org.cloudburstmc.protocol.common.DefinitionRegistry as CloudburstDefinitionRegistry

class SimpleDefinitionRegistry<D : CloudburstDefinition> private constructor(
    private val runtimeMap: Int2ObjectMap<D>
) : CloudburstDefinitionRegistry<D> {

    override fun getDefinition(runtimeId: Int): D? = runtimeMap.get(runtimeId)

    override fun isRegistered(definition: D): Boolean {
        return runtimeMap.get(definition.runtimeId) == definition
    }

    fun toBuilder(): Builder<D> = Builder<D>().addAll(runtimeMap.values)

    class Builder<D : CloudburstDefinition> {
        private val runtimeMap: Int2ObjectMap<D> = Int2ObjectOpenHashMap()

        fun addAll(definitions: Collection<out D>): Builder<D> {
            for (definition in definitions) {
                add(definition)
            }
            return this
        }

        fun add(definition: D): Builder<D> {
            require(!runtimeMap.containsKey(definition.runtimeId)) {
                "Runtime ID is already registered: ${definition.runtimeId}"
            }
            runtimeMap.put(definition.runtimeId, definition)
            return this
        }

        fun build(): SimpleDefinitionRegistry<D> {
            return SimpleDefinitionRegistry(this.runtimeMap)
        }
    }

    companion object {
        fun <D : CloudburstDefinition> builder(): Builder<D> = Builder()
    }
}

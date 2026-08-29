package com.project.zephyr.relay.definition

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.util.HashMap

class SimpleDefinitionRegistry<D : Definition> private constructor(
    private val runtimeMap: Int2ObjectMap<D>,
    private val identifierMap: Map<String, D>
) : DefinitionRegistry<D> {

    override fun getDefinition(runtimeId: Int): D? = runtimeMap.get(runtimeId)

    override fun getDefinition(identifier: String): D? = identifierMap[identifier]

    override fun isRegistered(definition: D): Boolean {
        return runtimeMap.get(definition.getRuntimeId()) == definition
    }

    fun toBuilder(): Builder<D> = Builder<D>().addAll(runtimeMap.values)

    class Builder<D : Definition> {
        private val runtimeMap: Int2ObjectMap<D> = Int2ObjectOpenHashMap()
        private val identifierMap: MutableMap<String, D> = HashMap()

        fun addAll(definitions: Collection<out D>): Builder<D> {
            for (definition in definitions) {
                add(definition)
            }
            return this
        }

        fun add(definition: D): Builder<D> {
            require(!runtimeMap.containsKey(definition.getRuntimeId())) {
                "Runtime ID is already registered: ${definition.getRuntimeId()}"
            }
            runtimeMap.put(definition.getRuntimeId(), definition)
            if (definition is NamedDefinition) {
                identifierMap[definition.getIdentifier()] = definition
            }
            return this
        }

        fun build(): SimpleDefinitionRegistry<D> {
            return SimpleDefinitionRegistry(this.runtimeMap, this.identifierMap)
        }
    }

    companion object {
        fun <D : Definition> builder(): Builder<D> = Builder()
    }
}

interface DefinitionRegistry<D : Definition> {
    fun getDefinition(runtimeId: Int): D?
    fun getDefinition(identifier: String): D?
    fun isRegistered(definition: D): Boolean
}

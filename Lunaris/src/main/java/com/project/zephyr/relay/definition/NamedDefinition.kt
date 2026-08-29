package com.project.zephyr.relay.definition

interface NamedDefinition : Definition {
    fun getIdentifier(): String
}

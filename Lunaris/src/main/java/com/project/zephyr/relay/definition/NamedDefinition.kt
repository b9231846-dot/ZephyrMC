package com.project.zephyr.relay.definition

import org.cloudburstmc.protocol.common.NamedDefinition as CloudburstNamedDefinition

interface NamedDefinition : CloudburstNamedDefinition {
    override fun getIdentifier(): String
}

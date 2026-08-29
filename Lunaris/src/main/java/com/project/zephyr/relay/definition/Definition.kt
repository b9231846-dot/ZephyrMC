package com.project.zephyr.relay.definition

import org.cloudburstmc.protocol.common.Definition as CloudburstDefinition

interface Definition : CloudburstDefinition {
    override fun getRuntimeId(): Int
}

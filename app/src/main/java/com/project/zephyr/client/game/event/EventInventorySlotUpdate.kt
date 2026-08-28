package com.project.zephyr.client.game.event

import com.project.zephyr.client.constructors.NetBound
import com.project.zephyr.client.game.inventory.AbstractInventory

class EventInventorySlotUpdate(
    session: NetBound,
    val inventory: AbstractInventory,
    val slot: Int
) : GameEvent(session, "InventorySlotUpdate")

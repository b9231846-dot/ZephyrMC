package com.project.zephyr.relay.address

import java.net.InetSocketAddress

data class ZephyrAddress(val hostName: String, val port: Int)

inline val ZephyrAddress.inetSocketAddress
    get() = InetSocketAddress(hostName, port)

inline val InetSocketAddress.zephyrAddress
    get() = ZephyrAddress(hostName, port)
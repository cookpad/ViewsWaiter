package org.cookpad.views_waiter.internal

internal data class BroadcasterMessage<out T>(val key: String, val value: T)
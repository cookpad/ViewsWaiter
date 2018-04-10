package org.cookpad.rxbroadcaster

internal data class RxBroadcasterMessage<out T>(val key: String, val value: T)
package org.cookpad.rxbroadcaster

internal class RxBroadcasterBuffer<T>(private val bufferSize: Int) {
    private var buffer = mutableListOf<RxBroadcasterMessage<T>>()

    fun add(message: RxBroadcasterMessage<T>) {
        // store only the latest object emitted for a specific key
        buffer.removeAll { it.key == message.key }
        buffer.add(0, message)

        // keep only the last N elements from buffer to avoid it growing endlessly
        buffer = buffer.take(bufferSize).toMutableList()
    }

    fun get(filter: String): RxBroadcasterMessage<T>? = buffer.find { it.key == filter }
    fun all() = buffer.toList()
}
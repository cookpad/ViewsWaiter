package org.cookpad.rxbroadcaster

internal class RxBroadcasterBuffer<T>(private val bufferSize: Int) {
    private var buffer = mutableListOf<RxBroadcasterMessage<T>>()

    fun add(message: RxBroadcasterMessage<T>) {
        buffer.removeAll { it.key == message.key }
        buffer.add(0, message)
        if (buffer.size > bufferSize) {
            buffer = buffer.subList(0, bufferSize)
        }
    }

    fun get(id: String): RxBroadcasterMessage<T>? = buffer.find { it.key == id }
    // fun get(ids: List<String>): List<RxBroadcasterMessage<T>>? = buffer.filter { ids.contains(it.key) }

    fun all() = buffer.toList()
}
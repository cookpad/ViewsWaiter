package org.cookpad.views_waiter

import org.junit.Test

class ViewsWaiterTest {

    @Test
    fun verifyConsumerNoChannelAndProducerNoChannel() {
        val pipeline = ViewsWaiter<String>()
        val testObserver = pipeline.stream().test()

        pipeline.apply {
            emit("1")
            emit("2")
            emit("3")
        }

        testObserver
                .assertValueCount(3)
                .assertNoErrors()
                .assertValues("1", "2", "3")
    }

    @Test
    fun verifyConsumerNoChannelAndProducerChannel() {
        val pipeline = ViewsWaiter<String>()
        val testObserver = pipeline.stream().test()

        pipeline.apply {
            channel("1").emit("1")
            channel("2").emit("2")
            channel("3").emit("3")
        }

        testObserver
                .assertValueCount(3)
                .assertNoErrors()
                .assertValues("1", "2", "3")
    }

    @Test
    fun verifyConsumerChannelAndProducerNoChannel() {
        val pipeline = ViewsWaiter<String>()
        val testObserver = pipeline.channel("1").stream().test()

        pipeline.apply {
            emit("1")
            emit("2")
            emit("3")
        }

        testObserver
                .assertNoErrors()
                .assertNoValues()
    }

    @Test
    fun verifyConsumerChannelAndProducerChannel() {
        val pipeline = ViewsWaiter<String>()

        val channel1 = pipeline.channel("1")
        val testObserver = channel1.stream().test()

        channel1.emit("1")
        pipeline.channel("2").emit("2")
        pipeline.channel("3").emit("3")

        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValues("1")
    }
}
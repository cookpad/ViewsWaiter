package org.cookpad.rxbroadcaster

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RxBroadcasterTest {
    data class SampleItem(val name: String, var description: String, val type: String)

    private val sampleItemPipeline = RxBroadcaster<SampleItem>()

    private val item1 = SampleItem("1", "not changed", "1")
    private val item2 = SampleItem("2", "not changed", "1")
    private val item3 = SampleItem("3", "not changed", "2")

    @Test
    fun listenAfterSubscribeUpdateItem() {
        val itemsList = mutableListOf(item1)

        sampleItemPipeline.listen().subscribe { item ->
            itemsList.find { it.name == item.name }?.description = item.description
        }

        sampleItemPipeline.emit(item1.copy(description = "changed"))
        assertThat(itemsList).hasSize(1)
        assertThat(itemsList[0].description).isEqualTo("changed")
    }

    @Test
    fun listenAfterDisposeDoNothing() {
        val itemsList = mutableListOf(item1)

        val itemsListUpdateDisposable = sampleItemPipeline.listen().subscribe { item ->
            itemsList.find { it.name == item.name }?.description = item.description
        }
        itemsListUpdateDisposable.dispose()

        sampleItemPipeline.emit(item1.copy(description = "changed"))
        assertThat(itemsList).hasSize(1)
        assertThat(itemsList[0].description).isEqualTo("not changed")
    }

    @Test
    fun listenAfterSubscribeWithFilterUpdateItem() {
        val itemsList = mutableListOf(item1, item2, item3)

        val itemType = "2"
        sampleItemPipeline.filter(itemType).listen().subscribe { item ->
            itemsList.find { it.name == item.name }?.description = item.description
        }

        sampleItemPipeline.filter(item1.type).emit(item1.copy(description = "changed"))
        sampleItemPipeline.filter(item2.type).emit(item2.copy(description = "changed"))
        sampleItemPipeline.filter(item3.type).emit(item3.copy(description = "changed"))

        assertThat(itemsList).hasSize(3)
        assertThat(itemsList[0].description).isEqualTo("not changed")
        assertThat(itemsList[1].description).isEqualTo("not changed")
        assertThat(itemsList[2].description).isEqualTo("changed")
    }

    @Test
    fun checkOnlyFilteredEventsCalled() {
        val sampleItemPipelineObservable = sampleItemPipeline.filter("filter").listen().test()

        sampleItemPipeline.filter("not filter").emit(item1)
        sampleItemPipeline.filter("filter").emit(item2)
        sampleItemPipeline.filter("not filter").emit(item3)
        sampleItemPipeline.filter("not filter").emit(item1)

        sampleItemPipelineObservable
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue {
                    it == item2
                }
    }

    @Test
    fun checkGetLastEventAfterSubscribe() {
        sampleItemPipeline.emit(item3)
        sampleItemPipeline.emit(item2)
        sampleItemPipeline.emit(item1)

        val sampleItemPipelineObservable = sampleItemPipeline.listen(getLast = true).test()

        sampleItemPipelineObservable
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue {
                    it == item1
                }
    }

    @Test
    fun checkGetFilteredLastEventAfterSubscribe() {
        sampleItemPipeline.filter("not filter").emit(item3)
        sampleItemPipeline.filter("filter").emit(item2)
        sampleItemPipeline.filter("not filter").emit(item1)
        sampleItemPipeline.filter("not filter").emit(item1)

        val sampleItemPipelineObservable = sampleItemPipeline.filter("filter").listen(getLast = true).test()

        sampleItemPipelineObservable
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue {
                    it == item2
                }
    }
}
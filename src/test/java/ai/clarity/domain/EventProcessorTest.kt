package ai.clarity.domain

import ai.clarity.domain.EventProcessorTestListener.eventHandler
import ai.clarity.domain.EventProcessorTestListener.map
import ai.clarity.domain.EventProcessorTestListener.view1
import ai.clarity.domain.EventProcessorTestListener.view2
import ai.clarity.query.WindowedView
import ai.clarity.report.Reporter
import io.kotlintest.Description
import io.kotlintest.be
import io.kotlintest.extensions.TestListener
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec
import java.time.Duration

class EventProcessorTest : BehaviorSpec() {

    override fun listeners(): List<TestListener> = listOf(EventProcessorTestListener)

    init {
        Given("""two subscribers to an EventDispatcher""") {

            When("A NetConnectionEvent is handled") {

                val event1 = NetConnectionEvent(
                        Host("from"),
                        Host("to"),
                        Timestamp(1366815793))

                Then("""Each subscriber should get notified about events"""){
                    eventHandler.handle(event1)
                    map["target1"]!! should be(1)
                    map["target2"]!! should be(1)
                }
            }

            When("""An NetConnectionEvent outside the bucket is received,
                |but still in the grace period""") {

                val ts1 = Timestamp("2018-07-01T12:30:40Z[UTC]")
                val ts2 = Timestamp("2018-07-01T13:01:40Z[UTC]")
                val event1 = NetConnectionEvent(Host("from"), Host("to"),ts1)
                val event2 = NetConnectionEvent(Host("from"), Host("to"),ts2)

                Then("""Each subscriber should get notified ONLY about
                    |in-bucket events""".trimMargin()){
                    eventHandler.handle(event1)
                    eventHandler.handle(event2)
                    map["target1"]!! should be(1)
                    map["target2"]!! should be(1)
                }
            }

            When("""An NetConnectionEvent outside the bucket is received,
                but still in the grace period""") {

                val ts1 = Timestamp("2018-07-01T12:30:40Z[UTC]")
                val ts2 = Timestamp("2018-07-01T13:01:40Z[UTC]")
                val event1 = NetConnectionEvent(Host("from"), Host("to"),ts1)
                val event2 = NetConnectionEvent(Host("from"), Host("to"),ts2)

                Then("""Each subscriber should get notified ONLY about
                    |in-bucket events""".trimMargin()){
                    eventHandler.handle(event1)
                    eventHandler.handle(event2)
                    map["target1"]!! should be(1)
                    map["target2"]!! should be(1)
                }
            }


            When("""An NetConnectionEvent outside the bucket is received,
                |but beyound in the grace period""") {

                val ts1 = Timestamp("2018-07-01T12:30:40Z[UTC]")
                val ts2 = Timestamp("2018-07-01T13:01:40Z[UTC]")
                val ts3 = Timestamp("2018-07-01T13:05:01Z[UTC]")

                val event1 = NetConnectionEvent(Host("from"), Host("to"),ts1)
                val event2 = NetConnectionEvent(Host("from"), Host("to"),ts2)
                val event3 = NetConnectionEvent(Host("from"), Host("to"),ts3)

                Then("""Each subscriber should get a reset call
                    |and next-bucket events""".trimMargin()){
                    eventHandler.handle(event1)
                    eventHandler.handle(event2)

                    map["target1"]!! should be(1)
                    map["target2"]!! should be(1)
                    eventHandler.handle(event3)
                    view1.countReset should be(1)
                    view2.countReset should be(1)
                    map["target1"]!! should be(2)
                    map["target2"]!! should be(2)
                }
            }
        }
    }
}


class WindowedViewStub(private val target: String) : WindowedView {

    var countReset:Int =0

    override fun reportStatus():String {
        countReset++
        map.clear()
        return ""
    }

    override fun applyEvents(vararg events: Event) {
        events.forEach {
            map[target] = map.computeIfAbsent(target) { 0 } + 1
        }

    }
}

object EventProcessorTestListener : TestListener {

    lateinit var eventHandler : BucketedEventHandler
    private val reporter = Reporter()
    lateinit var map: MutableMap<String, Int>


    val view1 = WindowedViewStub("target1")
    val view2 = WindowedViewStub("target2")

    override fun beforeTest(description: Description){


        eventHandler = BucketedEventHandler(
                Duration.ofHours(1),
                Duration.ofMinutes(5),
                reporter)
        eventHandler.subscribe(view1)
        eventHandler.subscribe(view2)
        map= mutableMapOf()
    }


}

package ai.clarity.domain

import ai.clarity.query.WindowedView
import ai.clarity.report.Reporter
import java.time.Duration
import java.time.ZonedDateTime

interface EventHandler{
    val capacity:Duration
    val watermark: Duration
    fun handle(event: Event)
}

class BucketedEventHandler(
        override val capacity: Duration = Duration.ofHours(1),
        override val watermark: Duration = Duration.ofMinutes(5),
        val reporter: Reporter
    ):EventHandler{

    private var nextBucketBuffer = mutableListOf<Event>()
    private var reportDateTime: ZonedDateTime = ZonedDateTime.now()
    private var currentHour: Int = Int.MAX_VALUE

    override fun handle(event: Event){
        val events = getEventsForCurrentBucket(event)
        views.forEach {
            it.applyEvents(*events.toTypedArray())
        }
    }

    private fun getEventsForCurrentBucket(event: Event): List<Event> {
        val eventsForCurrentBucket = emptyList<Event>()
        if (currentHour == Int.MAX_VALUE) {
            currentHour = event.dateTime.hour
            reportDateTime = event.dateTime
        }

        if (isEventInCurrentBucket(event)) {
            return listOf(event)
        } else if (isEventInNextBucket(event)) {
            nextBucketBuffer.add(event)
            if (isGracePeriodOver(event)) {
                flushCompletedBucket()
                val nextBucketEventsHeldUp = moveStateToNextBucket(event)
                return nextBucketEventsHeldUp
            }
        }

        return eventsForCurrentBucket
    }

    private val isEventInNextBucket:(Event)->Boolean =
            {it.dateTime.hour != currentHour}

    private val isEventInCurrentBucket:(Event)->Boolean=
            {it.dateTime.hour==currentHour}

    private fun moveStateToNextBucket(event: Event):List<Event> {
        val events = nextBucketBuffer
        currentHour = event.dateTime.hour
        reportDateTime = event.dateTime
        nextBucketBuffer = mutableListOf()
        return events
    }

    private fun flushCompletedBucket() {
        val map = views.map { it.reportStatus() }
        reporter.printBucketReport(map,reportDateTime)
    }

    private fun isGracePeriodOver(event: Event) =
            event.dateTime.minute >= watermark.toMinutes()


    private val views = mutableListOf<WindowedView>()


    fun subscribe(vararg observers: WindowedView) {
        observers.forEach { views.add(it) }
    }

}


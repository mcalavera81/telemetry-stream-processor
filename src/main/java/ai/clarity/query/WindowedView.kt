package ai.clarity.query

import ai.clarity.domain.Event
import java.time.Instant


interface WindowedView{
    fun applyEvents(vararg events: Event)
    fun reportStatus(): String
}

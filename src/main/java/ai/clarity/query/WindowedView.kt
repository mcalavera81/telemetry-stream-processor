package ai.clarity.query

import ai.clarity.domain.Event


interface WindowedView{
    fun applyEvents(vararg events: Event)
    fun reportStatus(): String
}

package ai.clarity.query

import ai.clarity.domain.Event
import ai.clarity.domain.Host
import ai.clarity.domain.NetConnectionEvent
import java.time.Instant


class MaxConnectionsWindowedView():WindowedView {

    override fun applyEvents(vararg events: Event) {

        val updateState: (MaxConnectionsState, Event) -> MaxConnectionsState = { state, event ->
            when (event) {
                is NetConnectionEvent -> {
                    if (event.fromHost != event.toHost) {
                        state.hostMapConns.put(
                                event.fromHost,
                                state.hostMapConns
                                        .getOrDefault(event.fromHost, 0) + 1)
                        state.hostMapConns.put(
                                event.toHost,
                                state.hostMapConns
                                        .getOrDefault(event.toHost, 0) + 1)
                    }
                    state
                }
                else -> state
            }
        }

        state = events.fold(state, updateState)

    }

    override fun reportStatus(): String {
            val message =if (maxHostCount != null) {
                "Host ${maxHostCount?.first} generated max conns (${maxHostCount?.second})"
            } else {
                "Max connections: No host found"
            }
            state = MaxConnectionsState()
            return message
    }

    val maxHostCount:Pair<Host, Int>?
        get(){
            return state.hostMapConns.maxBy { it.value }?.toPair()
        }

    private var state = MaxConnectionsState()


    open class MaxConnectionsState(
            val hostMapConns: MutableMap<Host, Int> = mutableMapOf())



}
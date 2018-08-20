package ai.clarity.query

import ai.clarity.domain.Event
import ai.clarity.domain.Host
import ai.clarity.domain.NetConnectionEvent
import java.time.Instant

class OutboundConnectionsWindowedView(val targetHost: Host):WindowedView {


    override fun applyEvents(vararg events: Event) {
        val updateState: (OutboundConnectionsState, Event) -> OutboundConnectionsState = {
            state, event ->

            when(event) {
                is NetConnectionEvent -> {
                    when (event.fromHost) {
                        targetHost -> state.connections.add(event.toHost)
                    }
                }
            }
            state

        }

        state = events.fold(state, updateState)

    }

    override fun reportStatus():String {
        val message = "Outbound connections: ${state.connections} for Host: $targetHost"
        state = OutboundConnectionsState()
        return message
    }

    val connections: MutableSet<Host>
        get() = state.connections

    private var state = OutboundConnectionsState()

    private data class OutboundConnectionsState(val connections:MutableSet<Host> = mutableSetOf())


}
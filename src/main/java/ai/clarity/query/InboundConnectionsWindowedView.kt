package ai.clarity.query

import ai.clarity.domain.Event
import ai.clarity.domain.Host
import ai.clarity.domain.NetConnectionEvent

class InboundConnectionsWindowedView(private val targetHost: Host):WindowedView {

    override fun applyEvents(vararg events: Event) {
        val updateState: (InboundConnectionsState, Event) -> InboundConnectionsState= {
            state, event ->

            when(event) {
                is NetConnectionEvent -> {
                    when (event.toHost) {
                        targetHost -> state.connections.add(event.fromHost)
                    }
                }
            }
            state

        }
        state = events.fold(state, updateState)

    }

    override fun reportStatus():String {
        val message = "Inbound connections: ${state.connections} for Host: $targetHost"
        state = InboundConnectionsState()
        return message
    }


    val connections: MutableSet<Host>
        get() = state.connections

    private var state = InboundConnectionsState()


    private data class InboundConnectionsState(
            val connections:MutableSet<Host> = mutableSetOf())



}
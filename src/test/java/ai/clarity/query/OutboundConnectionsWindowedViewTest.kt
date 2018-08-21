package ai.clarity.query

import ai.clarity.domain.Host
import ai.clarity.domain.NotDefinedEvent
import ai.clarity.domain.to
import ai.clarity.query.OutboundConnectionsWindowedViewTest.OutboundConnectionsProcessorTestListener.view
import io.kotlintest.Description
import io.kotlintest.be
import io.kotlintest.extensions.TestListener
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec
import mu.KLogging


class OutboundConnectionsWindowedViewTest : BehaviorSpec(){


    override fun listeners(): List<TestListener> = listOf(OutboundConnectionsProcessorTestListener)
    companion object: KLogging() {
        val target = Host("targetOrigin")
        val someDestination = Host("someDestination")
        val otherDestination = Host("otherDestination")
    }

    init {
        Given("An Clean InboundConnectionsWindowedView"){

            When("feeded with a NotDefinedEvent"){
                val event = NotDefinedEvent()

                Then("The number of connections should stay the same"){
                    view.applyEvents(event)
                    view.connections should be(emptySet<Host>())
                }
            }

            When("""feeded with a NetConnectionEvent
                where the origin is not our someDestination"""){
                val event = Host("not_target") to  someDestination

                Then("The number of connections should stay the same"){
                    view.applyEvents(event)
                    view.connections should be(emptySet<Host>())
                }
            }

            When("""feeded with a NetConnectionEvent
                where the origin is our target"""){
                val event = target to someDestination

                Then("The number of connections should increase by one"){
                    view.applyEvents(event)
                    view.connections should be(setOf(someDestination))
                }
            }
            When("""feeded with 2 NetConnectionEvent's
                where the origin is our target"""){
                val event1 = target to someDestination
                val event2 = target to otherDestination

                Then("The number of connections should increase by 2"){
                    view.applyEvents(event1, event2)
                    view.connections should be(setOf(someDestination, otherDestination))
                }
            }

            When("""feeded with 1 NetConnectionEvent's
                where the origin is our target,
                and some other where the destination is not our target"""){
                val event1 = target to someDestination
                val event2 = Host("not_target") to someDestination

                Then("The number of connections should increase by 2"){
                    view.applyEvents(event1, event2)
                    view.connections should be(setOf(someDestination))
                }
            }
        }

    }
    object OutboundConnectionsProcessorTestListener : TestListener {

        lateinit var view :OutboundConnectionsWindowedView

        override fun beforeTest(description: Description){
            view = OutboundConnectionsWindowedView(target)
        }


    }


}


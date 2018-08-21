package ai.clarity.query

import ai.clarity.domain.Host
import ai.clarity.domain.NotDefinedEvent
import ai.clarity.domain.to
import ai.clarity.query.InboundConnectionsWindowedViewTest.InboundConnectionsProcessorTestListener.view
import io.kotlintest.Description
import io.kotlintest.be
import io.kotlintest.extensions.TestListener
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec
import mu.KLogging


class InboundConnectionsWindowedViewTest : BehaviorSpec(){


    override fun listeners(): List<TestListener> = listOf(InboundConnectionsProcessorTestListener)
    companion object: KLogging() {
        val someOrigin = Host("someOrigin")
        val otherOrigin = Host("otherOrigin")
        private val target = Host("targetDestination")
    }

    init {
        Given("An Clean InboundConnectionsWindowedView"){

            When("feeded with a NotDefinedEvent"){
                val event = NotDefinedEvent()

                Then("The set of hosts should be empty"){
                    view.applyEvents(event)
                    view.connections should be(emptySet<Host>())
                }
            }

            When("""feeded with a NetConnectionEvent
                where the destination is not our someDestination"""){
                val event = someOrigin to Host("not_target")

                Then("The set of hosts should be empty"){
                    view.applyEvents(event)
                    view.connections should be(emptySet<Host>())
                }
            }

            When("""feeded with a NetConnectionEvent
                where the destination is our someDestination"""){
                val event = someOrigin to target

                Then("The set of hosts should contain one entry"){
                    view.applyEvents(event)
                    view.connections should be(setOf(someOrigin))
                }
            }

            When("""feeded with 2 NetConnectionEvent's
                where the destination is our someDestination"""){
                val event1 = someOrigin to target
                val event2 = otherOrigin to target

                Then("The set of hosts should contain 2 entries"){
                    view.applyEvents(event1, event2)
                    view.connections should be(setOf(someOrigin, otherOrigin))

                }
            }
        }

    }
    object InboundConnectionsProcessorTestListener : TestListener {

        lateinit var view :InboundConnectionsWindowedView

        override fun beforeTest(description: Description){
            view = InboundConnectionsWindowedView(target)
        }


    }


}


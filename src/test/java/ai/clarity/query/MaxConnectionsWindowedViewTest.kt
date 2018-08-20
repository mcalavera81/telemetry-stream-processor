package ai.clarity.query

import ai.clarity.domain.Host
import ai.clarity.domain.to
import ai.clarity.query.MaxConnectionsWindowedViewTest.MaxConnectionsWindowedViewTestListener.view
import io.kotlintest.Description
import io.kotlintest.be
import io.kotlintest.extensions.TestListener
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec


class MaxConnectionsWindowedViewTest: BehaviorSpec() {
    override fun listeners(): List<TestListener> = listOf(MaxConnectionsWindowedViewTestListener)

    val host1 = Host("host1")
    val host2 = Host("host2")
    val host3 = Host("host3")
    val host4 = Host("host4")


    init {
        Given("A MaxConnectionsWindowedView") {
            When("feeded with a NotDefinedEvent"){
                Then(""){
                    assert(view.maxHostCount  == null)
                }
            }
            When("A bunch of events are applied"){
                val events = arrayOf(
                        host1 to host2,
                        host2 to host3,
                        host3 to host1,
                        host2 to host4,
                        host4 to host2
                )
                Then("The host with max. connections should be host2"){
                    view.applyEvents(*events)
                    view.maxHostCount!!.first should be(host2)
                    view.maxHostCount!!.second should be(4)

                }
            }
        }
    }

    object MaxConnectionsWindowedViewTestListener : TestListener {

        lateinit var view :MaxConnectionsWindowedView

        override fun beforeTest(description: Description): Unit {
            view = MaxConnectionsWindowedView()
        }


    }
}
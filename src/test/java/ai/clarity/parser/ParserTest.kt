package ai.clarity.parser

import ai.clarity.domain.NetConnectionEvent
import io.kotlintest.be
import io.kotlintest.matchers.collections.containExactly
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec
import mu.KLogging

class ParserTest: BehaviorSpec(){

    val testFilename = "/parser/network-connections.txt"
    companion object: KLogging()

    init{
        Given("A String with the format <timestamp host host>"){
            When("toEvent is invoked with it"){
                Then("A NetConnectionEvent is produced"){
                    val event = "1366815793 quark garak".toEvent()

                    event.fromHost.hostname should be("quark")
                    event.toHost.hostname should be("garak")
                    event.instant.epochSecond should be(1366815793L)

                }
            }
        }

        Given("A file with lines to parse"){
            When("A lambda is passed to parseFile") {
                Then("An event is produced for each line") {
                    val events = mutableListOf<NetConnectionEvent>()
                    parseFile(testFilename.getPath()){
                        events += it
                    }
                    events.map { it.instant.epochSecond } should containExactly(
                            listOf(1366815793L, 1366815795L, 1366815811L))
                }
            }
        }
    }

    fun String.getPath():String {
        logger.info("**********************************")
        logger.info(this)
        return this.javaClass::class.java.getResource(this)!!.path
    }
}

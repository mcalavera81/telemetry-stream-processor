package ai.clarity

import ai.clarity.driver.CliParser
import ai.clarity.driver.ParserMode
import ai.clarity.driver.appSetup
import ai.clarity.parser.parseFile
import ai.clarity.parser.tailFile
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody

fun main(args: Array<String>) = mainBody{

    val parsedArgs = ArgParser(args).parseInto(::CliParser)
    parsedArgs.run {

        val handler = appSetup(
                inbound = inbound,
                outbound = outbound,
                output = output)

        val parser = when (mode) {
             ParserMode.BATCH-> ::parseFile
             ParserMode.TAIL-> ::tailFile
        }

        parser(inputFile,handler::handle)
    }



    val rt = Runtime.getRuntime()
    println("Used Memory: ${(rt.totalMemory() - rt.freeMemory())/ 1024 / 1024} Mb")
}
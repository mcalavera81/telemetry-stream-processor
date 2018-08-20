package ai.clarity.driver

import ai.clarity.domain.Host
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

enum class ParserMode { BATCH, TAIL }

class CliParser(parser: ArgParser) {

    val inbound by parser.storing(
            "-A", "--inbound",
            help = "Hostname for inbound connections counter")
            { Host(this) }


    val outbound by parser.storing(
            "-B", "--outbound",
            help = "Hostname for outbound connections counter")
            { Host(this) }

    val inputFile by parser.storing(
            "-I", "--input",
            help = "Filename to write the report to")

    val output by parser.storing(
            "-O", "--output",
            help = "Filename to write the report to")
            .default("")

    val mode by parser.mapping(
            "--batch" to ParserMode.BATCH,
            "--tail" to ParserMode.TAIL,
            help = "mode of operation")

}
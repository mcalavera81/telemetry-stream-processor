package ai.clarity.report

import java.io.PrintStream
import java.io.PrintWriter
import java.io.Writer
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter




class Reporter(str: String="") {
    private val output:Writer

    init {
        output = PrintWriter(
                if(str.isBlank()) (System.out)
                else PrintStream(str))
    }

    companion object {
        val formatter:DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:00")

    }
    fun printBucketReport(viewReports: List<String>,
                          dt: ZonedDateTime){
        val bucket = formatter.format(dt)
        output.write("Report for  $bucket \n\n")
        output.write(viewReports.joinToString("\n"))
        output.write("\n")
        output.write("-".repeat(10))
        output.write("\n\n")
        output.flush()

    }
}
package ai.clarity.parser

import ai.clarity.domain.Host
import ai.clarity.domain.NetConnectionEvent
import ai.clarity.domain.Timestamp
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun parseFile(filename:String, consumeEvent: (NetConnectionEvent)-> Unit){
    val bufferedReader = File(filename).bufferedReader()
    bufferedReader.useLines {
        it.filter{s->s.isNotBlank()}.map(String::toEvent).forEach(consumeEvent)
    }
}

fun tailFile(filename:String, consumeEvent: (NetConnectionEvent)-> Unit){
    val input = BufferedReader(FileReader(filename))
    var currentLine:String?

    while (true) {
        currentLine = input.readLine()
        if (currentLine != null && currentLine.isNotBlank()) {
            println(currentLine)
            consumeEvent(currentLine.toEvent())
            continue;
        }

        try {
            Thread.sleep(1000);
        } catch (e:InterruptedException) {
            Thread.currentThread().interrupt();
            break;
        }

    }
}

fun String.toEvent():NetConnectionEvent{

    val (unixEpoch, fromHost, toHost) = split("\\s+".toRegex())
    return NetConnectionEvent(
            Host(fromHost),
            Host(toHost),
            Timestamp(unixEpoch.toLong()) )
}


/*
;
*/
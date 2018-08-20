package ai.clarity.domain

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class Timestamp(val unixEpoch: Long = Instant.now().epochSecond){
    operator fun minus(seconds: Int) = Timestamp(unixEpoch - seconds)
    operator fun plus(seconds: Int) = Timestamp(unixEpoch + seconds)
    constructor(dt: String):this(ZonedDateTime.parse(dt).toEpochSecond())

    operator fun minus(duration: Duration) = Timestamp(unixEpoch - duration.seconds)
    operator fun plus(duration: Duration) = Timestamp(unixEpoch + duration.seconds)

}

data class Host(val hostname: String){
    override fun toString(): String {
        return hostname
    }
}

infix fun Host.to(destination:Host)=
        NetConnectionEvent(this, destination)


sealed class Event(val timestamp: Timestamp= Timestamp()){
    val instant: Instant
        get() = Instant.ofEpochSecond(timestamp.unixEpoch)
    val dateTime: ZonedDateTime
        get() = ZonedDateTime.ofInstant(instant, ZoneId.of("GMT"))

}

class NetConnectionEvent(
        val fromHost: Host,
        val toHost: Host,
        timestamp: Timestamp = Timestamp()):Event(timestamp)


class NotDefinedEvent(timestamp: Timestamp = Timestamp()):Event(timestamp)

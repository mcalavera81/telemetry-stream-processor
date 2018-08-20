package ai.clarity.driver

import ai.clarity.domain.BucketedEventHandler
import ai.clarity.domain.EventHandler
import ai.clarity.domain.Host
import ai.clarity.query.InboundConnectionsWindowedView
import ai.clarity.query.MaxConnectionsWindowedView
import ai.clarity.query.OutboundConnectionsWindowedView
import ai.clarity.report.Reporter


fun appSetup(inbound: Host, outbound:Host, output: String):EventHandler{
    val handler = BucketedEventHandler(reporter = Reporter(output))
    val inView = InboundConnectionsWindowedView(inbound)
    val outView = OutboundConnectionsWindowedView(outbound)
    val maxView = MaxConnectionsWindowedView()
    handler.subscribe(inView, outView, maxView)
    return handler
}
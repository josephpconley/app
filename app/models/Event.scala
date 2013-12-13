package models

import play.api.libs.json.{Json, JsValue}
import play.api.libs.Comet.CometMessage
import play.api.libs.EventSource.{EventNameExtractor, EventIdExtractor}

/**
 * An event object.
 *
 * @param _id The id of the event.
 * @param name The name of the event. For Server Sent Events in the browser, this will end up being the event type, so
 *             it will be possible to subscribe to this event by calling
 *             addEventListener("somename", function(e) { ... }, false).
 * @param data The data associated with the event.
 */
case class Event(_id: String, name: String, data: JsValue)

object Event {
  // For EventSource serialization
  implicit val cometMessage = CometMessage[Event](e => Json.stringify(e.data))
  implicit val idExtractor = EventIdExtractor[Event](e => Some(e._id))
  implicit val nameExtractor = EventNameExtractor[Event](e => Some(e.name))
}
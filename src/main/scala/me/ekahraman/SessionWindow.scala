package me.ekahraman


import akka.stream._
import akka.stream.stage._

import scala.collection.immutable.Queue
import scala.concurrent.duration.{Duration, FiniteDuration}

sealed trait SessionOverflowStrategy
case object DropOldest extends SessionOverflowStrategy
case object DropNewest extends SessionOverflowStrategy
case object FailStage  extends SessionOverflowStrategy


final case class SessionOverflowException(msg: String) extends RuntimeException(msg)

final class SessionWindow[T](val inactivity: FiniteDuration,
                             val maxSize: Int,
                             val overflowStrategy: SessionOverflowStrategy)
  extends GraphStage[FlowShape[T, T]] {

  require(maxSize > 1, "maxSize must be greater than 1")
  require(inactivity > Duration.Zero)

  val in  = Inlet[T]("SessionWindow.in")
  val out = Outlet[T]("SessionWindow.out")

  override val shape: FlowShape[T, T] = FlowShape(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new TimerGraphStageLogic(shape) with InHandler with OutHandler {

      private var queue: Queue[T] = Queue.empty[T]
      private var nextDeadline: Long = System.nanoTime + inactivity.toNanos

      setHandlers(in, out, this)

      override def preStart(): Unit = schedulePeriodically(shape, inactivity)

      override def postStop(): Unit = queue = Queue.empty[T]

      override def onPush(): Unit = {
        val element: T = grab(in)
        queue =
          if (queue.size < maxSize) queue.enqueue(element)
          else overflowStrategy match {
            case DropOldest =>
              if (queue.isEmpty) queue.enqueue(element)
              else queue.tail.enqueue(element)
            case DropNewest => queue
            case FailStage =>
              failStage(SessionOverflowException(s"Received messages are more than $maxSize"))
              Queue.empty[T]
          }

        nextDeadline = System.nanoTime + inactivity.toNanos

        if (!hasBeenPulled(in)) pull(in)
      }

      override def onPull(): Unit = if (!hasBeenPulled(in)) pull(in)

      override def onUpstreamFinish(): Unit = {
        if (queue.nonEmpty) {
          emitMultiple(out, queue)
        }
        super.onUpstreamFinish()
      }

      final override protected def onTimer(key: Any): Unit =
        if (nextDeadline - System.nanoTime < 0 && queue.nonEmpty) {
          emitMultiple(out, queue)
          queue = Queue.empty[T]
        }
    }
}

object SessionWindow {
  def apply[T](gap: FiniteDuration, maxSize: Int, overflowStrategy: SessionOverflowStrategy): SessionWindow[T] =
    new SessionWindow[T](gap, maxSize, overflowStrategy)
}

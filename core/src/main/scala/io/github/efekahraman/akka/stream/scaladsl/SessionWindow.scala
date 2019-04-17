package io.github.efekahraman.akka.stream.scaladsl

import akka.stream.FlowShape
import akka.stream.stage.GraphStage
import io.github.efekahraman.akka.stream._

import scala.concurrent.duration.FiniteDuration


object SessionWindow {
  def apply[T](gap: FiniteDuration,
               maxSize: Int,
               overflowStrategy: SessionOverflowStrategy): GraphStage[FlowShape[T, T]] =
    new SessionWindow[T](gap, maxSize, overflowStrategy)
}

package io.github.efekahraman.akka.stream.javadsl

import java.util.concurrent.TimeUnit

import akka.stream.FlowShape
import akka.stream.stage.GraphStage

import io.github.efekahraman.akka.stream._

import scala.concurrent.duration.FiniteDuration

object SessionOverflowStrategy {
  def dropOldest: SessionOverflowStrategy = DropOldest
  def dropNewest: SessionOverflowStrategy = DropNewest
  def failStage: SessionOverflowStrategy  = FailStage
}

object SessionWindow {
  def apply[T](gap: java.time.Duration,
               maxSize: Int,
               overflowStrategy: SessionOverflowStrategy): GraphStage[FlowShape[T, T]] = {
    def fromJavaDuration(duration: java.time.Duration): FiniteDuration =
      FiniteDuration.apply(duration.toNanos, TimeUnit.NANOSECONDS)
    new SessionWindow[T](fromJavaDuration(gap), maxSize, overflowStrategy)
  }
}

package me.ekahraman.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.scalatest.WordSpec

import scala.concurrent.duration.{FiniteDuration, _}
import scala.concurrent.{Await, Future}
import scala.util.Try

class SessionWindowSpec extends WordSpec {

  implicit val system: ActorSystem = ActorSystem("SessionWindowSpec")

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def run(range: Range, sessionWindow: SessionWindow[Int], atMost: FiniteDuration): Try[List[Int]] = {
    val future: Future[List[Int]] =
      Source(range)
        .via(sessionWindow)
        .runFold(Nil: List[Int])(_ :+ _)
    Try(Await.result(future, atMost))
  }

  "SessionWindow" must {
    "accumulate window until a gap occurs" in {
      val window: SessionWindow[Int] = SessionWindow(1 second, 10, FailStage)
      val result: Try[List[Int]] = run(1 to 10, window, 2 seconds)
      assert(result.isSuccess)
      assert(result.get == (1 to 10).toList)
    }

    "emit the current buffer upon completion" in {
      val window: SessionWindow[Int] = SessionWindow(5 second, 100, FailStage)
      val result: Try[List[Int]] = run(1 to 10, window, 2 seconds)
      assert(result.isSuccess)
      assert(result.get == (1 to 10).toList)
    }

    "drop oldest entries when maxSize is reached" in {
      val window: SessionWindow[Int] = SessionWindow(1 second, 5, DropOldest)
      val result: Try[List[Int]] = run(1 to 10, window, 2 seconds)
      assert(result.isSuccess)
      assert(result.get == (6 to 10).toList)
    }

    "drop newest entries when maxSize is reached" in {
      val window: SessionWindow[Int] = SessionWindow(1 second, 5, DropNewest)
      val result: Try[List[Int]] = run(1 to 10, window, 2 seconds)
      assert(result.isSuccess)
      assert(result.get == (1 to 5).toList)
    }

    "fail the graph stage when maxSize is reached" in {
      val window: SessionWindow[Int] = SessionWindow(1 second, 5, FailStage)
      val result: Try[List[Int]] = run(1 to 10, window, 2 seconds)
      assert(result.isFailure)
      assert(result.failed.get.isInstanceOf[SessionOverflowException])
    }
  }

}

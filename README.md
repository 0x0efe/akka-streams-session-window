[![Build Status](https://travis-ci.org/efekahraman/akka-streams-session-window.svg?branch=master)](https://travis-ci.org/efekahraman/akka-streams-session-window)

# Session Window extension for Akka Streams

This library provides session windowing for the Akka Streams.

Session windowing helps to identify periods of activity. This library uses a static idle period and favors the _processing time_ for events to distinguish the periods. More information can be found in the related [blog post](https://efekahraman.github.io/2019/01/session-windows-in-akka-streams).

Defines a `FlowShape` with same input and output types as `GraphStage[FlowShape[T, T]]`. Requires 3 parameters:

* `gap`: Idle time to determine particular activity as a session.
* `maxSize`: Max elements to hold in session buffer.
* `overflowStrategy`: Strategy to define what happens when new elements arrive and buffer is full. Can be one of:
    * `DropOldest`: Drops the oldest message in the buffer and adds the new one.
    * `DropNewest`: Drops the new message.
    * `FailStage`: Fails the stream with `SessionOverflowException`.

**Versions**

Current version is built with Akka Streams `2.5.16`.

Cross compiled with Scala `2.12` and `2.11`.

## Dependency

```
libraryDependencies += "io.github.efekahraman" %% "akka-stream-session-window" % "0.1.0"
```

## Examples

Below snippets show how to create a session window.

### Scala

```
import akka.stream._
import akka.stream.scaladsl._

import io.github.efekahraman.akka.stream.DropOldest
import io.github.efekahraman.akka.stream.scaladsl.SessionWindow

import scala.concurrent.duration._

val source: Source[String, NotUsed] = ???
val sessionWindow: GraphStage[FlowShape[String, String]] = SessionWindow(10 second, 5, DropOldest)

val windowedSource = source.via(sessionWindow)
// ...
```

### Java

```
import akka.stream.*;
import akka.stream.javadsl.*;

import io.github.efekahraman.akka.stream.javadsl.*;

import java.time.Duration;

final Source<String, NotUsed> source = // ...
final GraphStage<FlowShape<String, String>> sessionWindow = SessionWindow.apply(Duration.ofSeconds(10), 5, SessionOverflowStrategy.dropOldest());

final Source<ReadResult, NotUsed> windowedSource = amqpSource.via(window);
//...
```

# License

Code is available under the Apache 2 license, available at https://opensource.org/licenses/Apache-2.0.

// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package concurrency

import java.time.LocalDate

import scalaz.zio._
import scalaz.zio.clock.Clock
import scalaz.zio.console.{ putStrLn, Console }
import scalaz.zio.duration._
import scalaz.zio.stream.Sink

import scala.concurrent.duration.Duration

object zio_fibers {

  /**
   * A Fiber is a lightweight Thread of execution
   * They are spawned by forking an IO value, they take the types of the IO
   * every value in ZIO is described in IO
   * ZIO#fork describe a computation that never fails and produces a computation in a separated Fiber
   * That helps us to build concurrent and non blocking programs
   */
  /**
   * Using `ZIO#fork` Start a task that fails with "oh no!" in a separated Fiber
   * Identify the correct types
   */
  val failedF: UIO[Fiber[???, ???]] = IO.fail("oh no!") ?

  /**
   *  Using `ZIO#fork` Start a task that produces an int in a separated Fiber
   * Identify the correct types
   */
  val succeededF: UIO[Fiber[???, ???]] = IO.succeed(1) ?

  /**
   *  Using `ZIO#fork` Start a `console.putStrLn` in a new fiber
   *  Identify the correct types (!)
   */
  val putStrLnF: UIO[Fiber[???, ???]] = console.putStrLn("Hello ZIO") ?

  /**
   * Print in the console forever without blocking the main Fiber
   *  Identify the correct types (!)
   */
  val putStrLForeverF: ZIO[Console, Nothing, Fiber[???, ???]] = 
    console.putStrLn("Hello ZIO") ?

  /**
   * Get the value of the following Fibers using `Fiber#join`
   * Identify the correct types.
   */
  val fiber1: IO[???, ???] = Fiber.succeed[Nothing, Int](1) ?
  val fiber2: IO[???, ???] = Fiber.fail[Int](1) ?
  val fiber3: IO[???, ???] = Fiber.fail[Exception](new Exception("error!")) ?

  /**
   * Using `await` suspend the awaiting fibers until the result will be ready and
   * returns if the fiber is Succeed or Failed
   * Identify the correct types.
   */
  val await1: UIO[???] = Fiber.succeed[Nothing, Int](1) ?
  val await2: UIO[???] = Fiber.lift(IO.succeed("run forever").forever) ?
  val await3: UIO[???] = Fiber.fail[Int](1) ?
  val await4: UIO[???] = Fiber.fail[Exception](new Exception("error!")) ?

  /**
   *   Using `poll` observe if the Fiber is terminated
   *   Identify the correct types.
   */
  val observe1: UIO[???] = Fiber.succeed[Nothing, Int](1) ?
  val observe2: UIO[???] = Fiber.lift(IO.succeed("run forever").forever) ?
  val observe3: UIO[???] = Fiber.fail[Int](1) ?
  val observe4: UIO[???] = Fiber.fail[Exception](new Exception("error!")) ?

  /**
   * Using `flatMap` and `interrupt` to interrupt the fiber `putStrLForeverF`
   * Identify the correct types
   */
  val interruptedF: ZIO[Console, Nothing, Exit[???, ???]] = 
    putStrLForeverF ?

  /**
   * Write a program that asks 2 users for their name and greets them concurrently,
   * At the end join the Fibers and return the result.
   * Use for-comprehension or `flatMap` and `map`.
   *  Identify the correct types
   */
  val sayHello: ZIO[???, ???, Unit]     = 
    ???
  val sayHelloBoth: ZIO[???, ???, Unit] = sayHello ?

  /**
   * Write a program that computes the sum of a given List of Int and
   * checks if all elements are positive concurrently
   * At the end join the Fibers and return a tuple of their results using `zip`
   */
  def sumPositive(as: UIO[List[Int]]): UIO[(Int, Boolean)] = ???

  /**
   * Using `zipWith`. Write a program that start 2 Fibers with
   * pure values of type int and compute their sum
   * At the end join the Fibers and return a the result
   * Identify the correct return type.
   */
  def sum(a: UIO[Int], b: UIO[Int]): UIO[Int] = ???

  /**
   * Create a FiberLocal
   */
  val local: UIO[FiberLocal[Int]] = ???

  /**
   * set a value 42 to `local` using `flatMap` and `LocalFiber#set`
   * and then call `FiberLocal#get`
   */
  val updateLocal: UIO[Option[Int]] = ???

  /**
   * Using `locally`. Create FiberLocal that automatically sets
   * a value "Hello" and frees data and return that value
   */
  val locally: UIO[Option[String]] = ???

  /**
   * Write a program that executes 2 tasks in parallel
   * combining their results with the specified in a tuple. If
   * either side fails, then the other side will be interrupted.
   */
  def par[A, B](io1: Task[A], io2: Task[B]): Task[(A, B)] = ???
}

object zio_parallelism {

  /**
   * Using `zipPar` Write a program that finds the first and the last user in parallel
   * that satisfies a given condition for a given list of users
   * Identify the correct type.
   */
  case class User(id: Int, name: String, subscription: LocalDate)

  def findFirstAndLast(as: List[User])(p: LocalDate => Boolean): ??? = {
    val findFirst: UIO[Option[User]] = Task.effectTotal(as.find(user => p(user.subscription)))
    val findLast: UIO[Option[User]]  = Task.effectTotal(as.reverse.find(user => p(user.subscription)))
    
    ???
  }

  /**
   * Using `ZIO.collectAllPar`. Write a program that get users with specified ids in parallel.
   * Identify the correct type.
   */
  val users: Map[Int, User] = Map(
    1 -> User(1, "u1", LocalDate.of(2018, 9, 22)),
    2 -> User(2, "u2", LocalDate.of(2018, 8, 6))
  )

  def getUser(id: Int): IO[String, User] = ???

  def allUser(ids: List[Int]): IO[String, ???] = ???

  /**
   * Using `ZIO.foreachPar`. Write a program that displays the information of users (using `console.putStrLn`)
   * in parallel.
   * Identify the correct ZIO type.
   */
  def printAll(users: List[User]): ??? = ???

  def fib(n: Int): UIO[BigInt] = 
    if (n <= 1) UIO.succeed(BigInt(n))
    else fib(n -  1).zipWith(fib(n - 2))(_ + _)

  /**
   * Compute the first 20 fibonacci numbers in parallel.
   */
  val firstTwentyFibs: UIO[List[BigInt]] = 
    ???


  /**
   * Using `ZIO.foreachPar`. Write a program that compute the sum of action1, action2 and action3
   */
  val action1: ZIO[Clock, Nothing, Int] = IO.succeed(1).delay(10.seconds)
  val action2: ZIO[Clock, Nothing, Int] = IO.succeed(2).delay(1.second)
  val action3: ZIO[Clock, Nothing, Int] = IO.succeed(2).delay(1.second)
  val sum: ZIO[Clock, Nothing, Int]     = ???

  /**
   * Rewrite `printAll` specifying the number of fibers
   * Identify the correct ZIO type.
   */
  def printAll_(users: List[IO[String, List[User]]]): ??? = ???

  /**
   * Using `ZIO#race`. Race queries against primary and secondary databases
   * to return whichever one succeeds first.
   */
  sealed trait Database 
  object Database {
    case object Primary extends Database 
    case object Secondary extends Database
  }
  def getUserById(userId: Int, db: Database): Task[User] = ???
  def getUserById(userId: Int): Task[User] = ???

  /**
   * Using `raceAttempt` Race `leftContestent1` and `rightContestent1` to see
   * which one finishes first and returns Left if the left action will be the winner
   * otherwise it returns the successful value in Right
   */
  val raced2: ??? = ???

  /**
   * Using `raceWith`. Race `a` and `b` and print out the winner's result
   */
  val a: UIO[Int]                                  = UIO.succeedLazy((1 to 1000).sum)
  val b: UIO[Int]                                  = UIO.succeedLazy((1 to 10).sum)
  val firstCompleted1: ZIO[Console, Nothing, Unit] = ???

  /**
   * Using `raceAll` return the first completed action.
   */
  val a1: ZIO[Clock, Nothing, Int]             = IO.succeed(1).delay(10.seconds)
  val a2: ZIO[Clock, Nothing, Int]             = IO.succeed(2).delay(1.second)
  val a3: ZIO[Clock, Nothing, Int]             = IO.succeed(2).delay(1.second)
  val firstCompleted: ZIO[Clock, Nothing, Int] = (a1 :: a2 :: a3 :: Nil) ?

}

object zio_ref {

  /**
   * Using `Ref.make` constructor, create a `Ref` that is initially `0`.
   */
  val makeZero: UIO[Ref[Int]] = 0 ?

  /**
   * Using `Ref#get` and `Ref#set`, change the
   * value to be 10 greater than its initial value. Return the new value.
   */
  val incrementedBy10: UIO[Int] =
    for {
      ref   <- makeZero
      _     <- (ref ? : UIO[Unit])
      value <- (ref ? : UIO[Int])
    } yield value

  /**
   * Using `Ref#update` to atomically increment the value by 10.
   * Return the new value.
   */
  val atomicallyIncrementedBy10: UIO[Int] =
    for {
      ref   <- makeZero
      value <- (ref ? : UIO[Int])
    } yield value

  /**
   * Refactor this contentious code to be atomic using `Ref#update`.
   */
  def makeContentious1(n: Int): UIO[Fiber[Nothing, List[Nothing]]] = 
    Ref.make(0).flatMap(ref =>
      IO.forkAll(List.fill(n)(ref.get.flatMap(value =>
        ref.set(value + 10)
      ).forever))
    )
  def makeContentious2(n: Int): UIO[Fiber[Nothing, List[Nothing]]] = 
    ???

  /**
   * Using the `Ref#modify` to atomically increment the value by 10,
   * but return the old value, converted to a string.
   */
  val atomicallyIncrementedBy10PlusGet: UIO[String] =
    for {
      ref   <- makeZero
      value <- ref.modify(v => (???, v + 10))
    } yield value

  /**
   * Using `Ref#updateSome` change the state of a given ref to Active
   * only if the state is Closed
   */
  trait State
  case object Active extends State
  case object Closed extends State

  def setActive(ref: Ref[State], boolean: Boolean): UIO[State] = 
    ???

  /**
   * Using `Ref#modifySome` change the state to Closed only if the state was Active and return true
   * if the state is already closed return false
   */
  def setClosed(ref: Ref[State], boolean: Boolean): UIO[Boolean] = ???

  /**
   * RefM allows effects in atomic operations
   */
  /**
   * Create a RefM using `RefM.apply`
   */
  val refM: UIO[RefM[Int]] = 4 ?

  /**
   * update the refM with the square of the old state and print it out in the Console
   */
  val square =
    for {
      ref <- (??? : UIO[Ref[Int]])
      v   <- (??? : UIO[Ref[Int]])
    } yield v

}

object zio_promise {

  /**
   * Using `Promise.make` construct a promise that cannot
   * fail but can be completed with an integer.
   */
  val makeIntPromise: UIO[Promise[Nothing, Int]] =
    ???

  /**
   * Using `Promise.succeed`, complete a `makeIntPromise` with an integer 42
   */
  val completed1: UIO[Boolean] =
    for {
      promise   <- makeIntPromise
      completed <- (promise ? : UIO[Boolean])
    } yield completed

  /**
   * Using `Promise.fail`, try to complete `makeIntPromise`.
   * Explain your findings
   */
  val errored1: UIO[Boolean] =
    for {
      promise   <- makeIntPromise
      completed <- (promise ? : UIO[Boolean])
    } yield completed

  /**
   * Create a new promise that can fail with a `Error` or produce a value of type `String`
   */
  val errored2: UIO[Boolean] =
    for {
      promise   <- Promise.make[???, ???]
      completed <- (promise ? : UIO[Boolean])
    } yield completed

  /**
   * Make a promise that might fail with `Error`or produce a value of type 
   * `Int` and interrupt it using `interrupt`.
   */
  val interrupted: UIO[Boolean] =
    for {
      promise   <- Promise.make[???, ???]
      completed <- (promise ? : UIO[Boolean])
    } yield completed

  /**
   * Using `await` retrieve a value computed from inside another fiber
   */
  val handoff1: ZIO[Console with Clock, Nothing, Int] =
    for {
      promise <- Promise.make[Nothing, Int]
      _       <- (clock.sleep(10.seconds) *> promise.succeed(42)).fork
      _       <- putStrLn("Waiting for promise to be completed...")
      value   <- (promise ? : UIO[Int])
      _       <- putStrLn("Got: " + value)
    } yield value

  /**
   * Using `await`. retrieve a value from a promise that was failed in another fiber.
   */
  val handoff2: ZIO[Console with Clock, Error, Int] =
    for {
      promise <- Promise.make[Error, Int]
      _       <- (clock.sleep(10.seconds) *> promise.fail(new Error("Uh oh!"))).fork
      _       <- putStrLn("Waiting for promise to be completed...")
      value   <- (promise ? : IO[Error, Int])
      _       <- putStrLn("This line will NEVER be executed")
    } yield value

  /**
   * Using `await`. Try to retrieve a value from a promise
   * that was interrupted in another fiber.
   */
  val handoff3: ZIO[Clock with Console, Nothing, Int] =
    for {
      promise <- Promise.make[Nothing, Int]
      _       <- promise.interrupt.delay(10.milliseconds).fork
      value   <- (promise ? : IO[Nothing, Int])
      _       <- putStrLn("This line will NEVER be executed")
    } yield value

  /**
   * Build auto-refreshing cache using `Ref`and `Promise`
   */
}

object zio_queue {

  /**
   * Using `Queue.bounded`, create a queue for `Int` values with a capacity of 10
   */
  val makeQueue: UIO[Queue[Int]] = Queue.bounded[Int](10)

  /**
   * Place `42` into the queue using `Queue#offer`.
   */
  val offered1: UIO[Unit] =
    for {
      queue <- makeQueue
      _     <- (queue ? : UIO[Unit])
    } yield ()

  /**
   * Using `Queue#take` take an integer value from the queue
   */
  val taken1: UIO[Int] =
    for {
      queue <- makeQueue
      _     <- queue.offer(42)
      value <- (queue ? : UIO[Int])
    } yield value

  /**
   * In a child fiber, place 2 values into a queue, and in the main fiber, read
   *  2 values from the queue.
   */
  val offeredTaken1: UIO[(Int, Int)] =
    for {
      queue <- makeQueue
      _     <- (??? : UIO[Unit]).fork
      v1    <- (queue ? : UIO[Int])
      v2    <- (queue ? : UIO[Int])
    } yield (v1, v2)

  /**
   * In a child fiber, read infintely many values out of the queue and write
   * them to the console. In the main fiber, write 100 values into the queue,
   * using `ZIO.foreach` on a `List`.
   */
  val infiniteReader1: ZIO[Console, Nothing, List[Boolean]] =
    for {
      queue <- Queue.bounded[String](10)
      _     <- (??? : ZIO[Console, Nothing, Nothing]).fork
      vs    <- (??? : UIO[List[Boolean]])
    } yield vs

  /**
   * Using  `Queue`, `Ref`, and `Promise`, implement an "actor" like construct
   * that can atomically update the values of a counter.
   */
  sealed trait Message
  case class Increment(amount: Int) extends Message
  val makeCounter: UIO[Message => UIO[Int]] =
    for {
      counter <- Ref.make(0)
      mailbox <- Queue.bounded[(Message, Promise[Nothing, Int])](100)
      _       <- (mailbox.take ? : UIO[Fiber[Nothing, Nothing]])
    } yield { (message: Message) =>
      ???
    }

  val counterExample: UIO[Int] =
    for {
      counter <- makeCounter
      _       <- IO.collectAllPar(List.fill(100)(IO.foreach((0 to 100).map(Increment(_)))(counter)))
      value   <- counter(Increment(0))
    } yield value

  /**
   * using `Queue.sliding` create a queue with capacity 3 using sliding strategy
   */
  val slidingQ: UIO[Queue[Int]] = ???

  /**
   * Using `Queue#offerAll`, offer 4 integer values to a sliding queue with capacity of 3
   * and take them all using `Queue#takeAll`. What will you get as result?
   */
  val offer4TakeAllS: UIO[List[Int]] = for {
    queue  <- Queue.sliding[Int](3)
    _      <- queue.offerAll(List(1, 2, 3))
    values <- (??? : UIO[List[Int]])
  } yield values

  /**
   * using `Queue.dropping` create a queue with capacity 3 using sliding strategy
   */
  val dropingQ: UIO[Queue[Int]] = ???

  /**
   * Using `Queue#offerAll`, offer 4 integer values to a dropping queue with capacity of 3
   * and take them all using `Queue#takeAll`. What will you get as result?
   */
  val offer4TakeAllD: UIO[List[Int]] = for {
    queue  <- Queue.sliding[Int](3)
    _      <- queue.offerAll(List(1, 2, 3))
    values <- (??? : UIO[List[Int]])
  } yield values

}

object zio_semaphore {

  /**
   *Using `Semaphore.make`, create a semaphore with 1 permits.
   */
  val semaphore: UIO[Semaphore] = ???

  /**
   * Using `Semaphore#acquire` acquire permits sequentially (using IO.???) and
   * return the number of available permits using `Semaphore#available`.
   */
  val nbAvailable1: UIO[Long] =
    for {
      semaphore <- Semaphore.make(5)
      _         <- (??? : UIO[Unit])
      available <- (??? : UIO[Long])
    } yield available

  /**
   * Using `Semaphore#acquireN` acquire permits in parallel (using IO.???) and
   * return the number of available permits.
   */
  val nbAvailable2: UIO[Long] =
    for {
      semaphore <- Semaphore.make(5)
      _         <- (??? : UIO[Unit])
      available <- (??? : UIO[Long])
    } yield available

  /**
   * Acquire one permit and release it using `Semaphore#release`.
   * How much permit are available?
   */
  val nbAvailable3: UIO[Long] =
    for {
      semaphore <- Semaphore.make(5)
      _         <- (??? : UIO[Unit])
      _         <- (??? : UIO[Unit])
      available <- (??? : UIO[Long])
    } yield available

  /**
   * Using `Semaphore#withPermit` prepare a semaphore that once it acquires a permit
   * putStrL("is completed")
   */
  val s: ZIO[Clock, Nothing, Unit] =
    for {
      semaphore <- Semaphore.make(1)
      p         <- Promise.make[Nothing, Unit]
      _         <- (??? : UIO[Unit])
      _         <- semaphore.acquire.delay(1.second).fork
      msg       <- p.await
    } yield msg


  /**
   * Implement `createAcceptor` to create a connection acceptor that will 
   * accept at most the specified number of connections.
   */
  trait Request 
  trait Response
  type Handler = Request => UIO[Response]
  lazy val defaultHandler: Handler = ???
  def startWebServer(handler: Handler): UIO[Nothing] = 
    // Pretend this is implemented.
    ???
  def limitedHandler(limit: Int, handler: Handler): UIO[Handler] = 
    ???
  val webServer1k: UIO[Nothing] = 
    for {
      acceptor <- limitedHandler(1000, defaultHandler)
      value    <- startWebServer(acceptor)
    } yield value
}

object zio_stream {
  import scalaz.zio.stream.Stream

  /**
   * Create a stream using `Stream.apply`
   */
  val streamStr: Stream[Any, Nothing, Int] = ???

  /**
   * Create a stream using `Stream.fromIterable`
   */
  val stream1: Stream[Any, Nothing, Int] = (1 to 42) ?

  /**
   * Create a stream using `Stream.fromChunk`
   */
  val chunk: Chunk[Int]                  = Chunk(43 to 100: _*)
  val stream2: Stream[Any, Nothing, Int] = ???

  /**
   * Make a queue and use it to create a stream using `Stream.fromQueue`
   */
  val stream3: UIO[Stream[Any, Nothing, Int]] = ???

  /**
   * Create a stream from an effect producing a String
   * using `Stream.lift`
   */
  val stream4: Stream[Any, Nothing, String] = ???

  /**
   * Create a stream of ints that starts from 0 until 42,
   * using `Stream#unfold`
   */
  val stream5: Stream[Any, Nothing, Int] = ???
  
  /**
   * Using `Stream.unfoldM`, create a stream of lines of input from the user, 
   * terminating when the user enters the command "exit" or "quit".
   */
  import java.io.IOException
  import scalaz.zio.console.getStrLn
  val stream6: Stream[Console, IOException, String] = ???

  /**
   * Using `withEffect` log every element.
   */
  val loggedInts: Stream[Console, Nothing, Int] = stream1 ?

  /**
   * Using `Stream#filter` filter the even numbers
   */
  val evenNumbrers: Stream[Any, Nothing, Int] = stream1 ?

  /**
   * Using `Stream#takeWhile` take the numbers that are less than 10
   */
  val lessThan10: Stream[Any, Nothing, Int] = stream1 ?

  /**
   * Print out each value in the stream using `Stream#foreach`
   */
  val printAll: ZIO[Console, Nothing, Unit] = stream1 ?

  /**
   * Convert every Int into String using `Stream#map`
   */
  val toStr: Stream[Any, Nothing, String] = stream1 ?

  /**
   * Merge two streams together using `Stream#merge`
   */
  val mergeBoth: Stream[Any, Nothing, Int] = (stream1, stream2) ?

  /**
   * Create a Sink using `Sink#readWhile` that takes an input of type String and check if it's not empty
   */
  val sink: Sink[Any, Nothing, String, String, List[String]] = ???

  /**
   * Run `sink` on the stream to get a list of non empty string
   */
  val stream                                         = Stream("Hello", "Hi", "Bonjour", "cześć", "", "Hallo", "Hola")
  val firstNonEmpty: ZIO[Any, Nothing, List[String]] = ???

}

object zio_schedule {

  /**
   * Using `Schedule.recurs`, create a schedule that recurs 5 times.
   */
  val fiveTimes: Schedule[Any, Any, Int] = ???

  /**
   * Using the `ZIO.repeat`, repeat printing "Hello World"
   * five times to the console.
   */
  val repeated1 = putStrLn("Hello World") ?

  /**
   * Using `Schedule.spaced`, create a schedule that recurs forever every 1 second
   */
  val everySecond: Schedule[Any, Any, Int] = ???

  /**
   * Using the `&&` method of the `Schedule` object, the `fiveTimes` schedule,
   * and the `everySecond` schedule, create a schedule that repeats fives times,
   * evey second.
   */
  val fiveTimesEverySecond = ???

  /**
   *  Using the `ZIO#repeat`, repeat the action
   *  putStrLn("Hi hi") using `fiveTimesEverySecond`.
   */
  val repeated2 = putStrLn("Hi hi") ?

  /**
   * Using `Schedule#andThen` the `fiveTimes`
   * schedule, and the `everySecond` schedule, create a schedule that repeats
   * fives times rapidly, and then repeats every second forever.
   */
  val fiveTimesThenEverySecond = ???

  /**
   * Using `ZIO#retry`, retry the following error
   * a total of five times.
   */
  val error1   = IO.fail("Uh oh!")
  val retried5 = error1 ?

  /**
   * Using the `Schedule#||`, the `fiveTimes` schedule,
   * and the `everySecond` schedule, create a schedule that repeats the minimum
   * of five times and every second.
   */
  val fiveTimesOrEverySecond = ???

  /**
   * Using `Schedule.exponential`, create an exponential schedule that starts from 10 milliseconds.
   */
  val exponentialSchedule: Schedule[Any, Any, Duration] = 
    ???

  /**
   * Using `Schedule.jittered` produced a jittered version of
   * `exponentialSchedule`.
   */
  val jitteredExponential = exponentialSchedule ?

  /**
   * Using `Schedule.whileOutput`, produce a filtered schedule from
   * `Schedule.forever` that will halt when the number of recurrences exceeds 100.
   */
  val oneHundred = Schedule.forever.whileOutput(???)

  /**
   * Using `Schedule.identity`, produce a schedule that recurs forever,
   * without delay, returning its inputs.
   */
  def inputs[A]: Schedule[Any, A, A] = ???

  /**
   * Using `Schedule#collect`, produce a schedule that recurs
   * forever, collecting its inputs into a list.
   */
  def collectedInputs[A]: Schedule[Any, A, List[A]] =
    Schedule.identity[A] ?

  /**
   * Using  `*>`, combine `fiveTimes` and `everySecond` but return the output of `everySecond`.
   */
  val fiveTimesEverySecondR: Schedule[Any, Any, Int] = ???

  /**
   * Produce a jittered schedule that first does exponential spacing (starting
   * from 10 milliseconds), but then after the spacing reaches 60 seconds,
   * switches over to fixed spacing of 60 seconds between recurrences, but will
   * only do that for up to 100 times, and produce a list of the inputs to 
   * the schedule.
   */
  import scalaz.zio.random.Random
  def mySchedule[A]: Schedule[Clock with Random, A, List[A]] = 
    ???
}

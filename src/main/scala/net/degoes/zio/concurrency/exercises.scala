// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package concurrency

import java.time.LocalDate

import scalaz.zio._
import scalaz.zio.clock.Clock
import scalaz.zio.console.{ putStrLn, Console }
import scalaz.zio.duration._
import scalaz.zio.stream._

import scala.concurrent.duration.Duration

object zio_fibers {

  /**
   * A Fiber is a lightweight Thread of execution. They are spawned by forking
   * an effect, they take the types of the effect.
   *
   * ZIO#fork describes a computation that never fails, and succeeds in producing
   * the fiber, which is a "handle" to the running computation.
   */
  /**
   * EXERCISE 1
   *
   * Using `ZIO#fork`, start an effect that fails with "oh no!" in a separate
   * fiber.
   */
  val failedF: UIO[Fiber[String, Nothing]] = IO.fail("oh no!") ?

  /**
   * EXERCISE 2
   *
   * Using `ZIO#fork`, start an effect that succeeds with an integer in a
   * separate fiber.
   */
  val succeededF: UIO[Fiber[Nothing, Int]] = IO.succeed(1) ?

  /**
   * EXERCISE 3
   *
   * Using `ZIO#fork`, start the `console.putStrLn` effect in a separate
   * fiber.
   */
  val putStrLnF: UIO[Fiber[Nothing, Unit]] = console.putStrLn("Hello ZIO") ?

  /**
   * EXERCISE 4
   *
   * Using a combination of `ZIO#forever` and `ZIO#fork`, print text to the
   * console in an infinite loop.
   */
  val putStrLForeverF: ZIO[Console, Nothing, Fiber[Nothing, Nothing]] =
    console.putStrLn("Hello ZIO") ?

  /**
   * EXERCISE 8
   *
   * Construct a "trivial" fiber that has already succeeded with an integer
   * value using `Fiber.succeed`.
   */
  val trivialSuccess: Fiber[Nothing, Int] = ???

  /**
   * EXERCISE 9
   *
   * Construct a "trivial" fiber that has already failed with a string
   * value using `Fiber.fail`.
   */
  val trivialFailure: Fiber[String, Nothing] = ???

  /**
   * EXERCISE 10
   *
   * Using `Fiber#join`, join the main fiber to the fibers constructed below,
   * which yields the value of the (joinee) fiber to the main (joiner) fiber.
   */
  val fiber1: IO[Nothing, Int]       = Fiber.succeed[Nothing, Int](1) ?
  val fiber2: IO[Int, Nothing]       = Fiber.fail[Int](1) ?
  val fiber3: IO[Exception, Nothing] = Fiber.fail[Exception](new Exception("error!")) ?

  /**
   * EXERCISE 11
   *
   * Using `Fiber#await`, suspend the main (joiner) fibers to the below fibers,
   * until they have run their course and produce `Exit` values.
   */
  val await1: UIO[Exit[Nothing, Int]]       = Fiber.succeed[Nothing, Int](1) ?
  val await2: UIO[Exit[Nothing, Nothing]]   = Fiber.fromEffect(IO.succeed("run forever").forever) ?
  val await3: UIO[Exit[Int, Nothing]]       = Fiber.fail[Int](1) ?
  val await4: UIO[Exit[Exception, Nothing]] = Fiber.fail[Exception](new Exception("error!")) ?

  /**
   * EXERCISE 12
   *
   * Using `Fiber#poll`, observe to see if the Fiber is done executing.
   */
  val observe1: UIO[Exit[Nothing, Int]]       = Fiber.succeed[Nothing, Int](1) ?
  val observe2: UIO[Exit[Nothing, Nothing]]   = Fiber.fromEffect(IO.succeed("run forever").forever) ?
  val observe3: UIO[Exit[Int, Nothing]]       = Fiber.fail[Int](1) ?
  val observe4: UIO[Exit[Exception, Nothing]] = Fiber.fail[Exception](new Exception("error!")) ?

  /**
   * EXERCISE 13
   *
   * Using `ZIO#flatMap` and `Fiber#interrupt`, interrupt the fiber `putStrLForeverF`.
   */
  val interruptedF: ZIO[Console, Nothing, Exit[Nothing, Nothing]] =
    putStrLForeverF ?

  /**
   * EXERCISE 14
   *
   * By using `ZIO#fork` and `Fiber#join`, make an effect that computes two
   * fibonacci numbers in parallel.
   */
  def fibInt(n: Int): UIO[Int]         = if (n <= 1) IO.succeed(n) else fibInt(n - 2).zipWith(fibInt(n - 1))(_ + _)
  val computeFib10: UIO[Int]           = fibInt(10)
  val computeFib20: UIO[Int]           = fibInt(20)
  val computeBothFibs: UIO[(Int, Int)] = ???

  /**
   * EXERCISE 15
   *
   * Using `FiberRef.make`, create a `FiberRef` that is initially set to 0.
   */
  val fiberRef: UIO[FiberLocal[Int]] = ???

  /**
   * EXERCISE 16
   *
   * Using `FiberRef#set`, set the passed in `FiberRef` to 42.
   */
  def updateRef(ref: Ref[Int]): UIO[Unit] = ???
}

object zio_parallelism {

  /**
   * EXERCISE 1
   *
   * Using `ZIO#zipPar`, make an effect that finds the first and last user
   * whose subscription date satisfies some predicate.
   */
  case class User(id: Int, name: String, subscription: LocalDate)

  def findFirstAndLast(users: List[User])(p: LocalDate => Boolean): ??? = {
    val findFirst: UIO[Option[User]] = Task.effectTotal(users.find(user => p(user.subscription)))
    val findLast: UIO[Option[User]]  = Task.effectTotal(users.reverse.find(user => p(user.subscription)))

    ???
  }

  /**
   * EXERCISE 2
   *
   * Using `ZIO.collectAllPar`, make an effect that retrieves all users with the
   * specified ids.
   */
  val users: Map[Int, User] = Map(
    1 -> User(1, "u1", LocalDate.of(2018, 9, 22)),
    2 -> User(2, "u2", LocalDate.of(2018, 8, 6))
  )

  def getUser(id: Int): IO[String, User] = IO.effect(users(id)).mapError(_.getMessage)

  def getAllUsers(ids: List[Int]): IO[String, ???] = ???

  /**
   * EXERCISE 3
   *
   * Using `ZIO.foreachPar`, make an effect that prints out many users in
   * parallel, using the `printUser` function.
   */
  def printUser(user: User): UIO[Unit]             = UIO.effectTotal(println(s"${user.toString}"))
  def printAll(users: List[User]): UIO[List[Unit]] = ???

  def fib(n: Int): UIO[BigInt] =
    if (n <= 1) UIO.succeed(BigInt(n))
    else fib(n - 1).zipWith(fib(n - 2))(_ + _)

  /**
   * EXERCISE 4
   *
   * Using `ZIO.foreachPar`, make an effect that computes the first 20
   * fibonacci numbers in parallel.
   */
  val firstTwentyFibs: UIO[List[BigInt]] =
    ???

  /**
   * EXERCISE 5
   *
   * Using `ZIO#zipPar`, make an effect that sums the integers produced by
   * the following effects.
   */
  val action1: ZIO[Clock, Nothing, Int] = IO.succeed(3).delay(1.seconds)
  val action2: ZIO[Clock, Nothing, Int] = IO.succeed(2).delay(2.second)
  val action3: ZIO[Clock, Nothing, Int] = IO.succeed(1).delay(3.second)
  val sum: ZIO[Clock, Nothing, Int]     = ???

  /**
   * EXERCISE 6
   *
   * Using `ZIO#race`, make an effect that races queries against primary
   * and secondary databases, returning whichever succeeds first.
   */
  sealed trait Database
  object Database {
    case object Primary   extends Database
    case object Secondary extends Database
  }
  /* Assume this onoe is implemented: */
  def getUserById(userId: Int, db: Database): Task[User] = ???
  /* Implement this one: */
  def getUserById1(userId: Int): Task[User] =
    ???

  /**
   * EXERCISE 7
   *
   * Using `ZIO#raceAttempt`, make an effect that races the two database
   * queries, but this time succeeding or failing with the first one to finish.
   */
  def getUserById2(userId: Int): Task[User] = ???

  /**
   * EXERCISE 8
   *
   * Using `ZIO.raceAll`, make an effect that races the below effects to return
   * the winner.
   */
  val a1: ZIO[Clock, Nothing, Int]             = IO.succeed(1).delay(10.seconds)
  val a2: ZIO[Clock, Nothing, Int]             = IO.succeed(2).delay(1.second)
  val a3: ZIO[Clock, Nothing, Int]             = IO.succeed(2).delay(1.second)
  val firstCompleted: ZIO[Clock, Nothing, Int] = (a1 :: a2 :: a3 :: Nil) ?

}

object zio_ref {

  /**
   * EXERCISE 1
   *
   * Using `Ref.make`, create a `Ref` that is initially `0`.
   */
  val makeZero: UIO[Ref[Int]] = 0 ?

  /**
   * EXERCISE 2
   *
   * Using `Ref#get` and `Ref#set`, change the value to be 10 greater than its
   * initial value. Return the new value.
   */
  val incrementedBy10: UIO[Int] =
    for {
      ref   <- makeZero
      _     <- (ref ? : UIO[Unit])
      value <- (ref ? : UIO[Int])
    } yield value

  /**
   * EXERCISE 3
   *
   * Using `Ref#update` to atomically increment the value by 10.
   */
  val atomicallyIncrementedBy10: UIO[Int] =
    for {
      ref   <- makeZero
      value <- (ref ? : UIO[Int])
    } yield value

  /**
   * EXERCISE 4
   *
   * Using `Ref#update`, refactor this contentious code to be atomic.
   */
  def makeContentious1(n: Int): UIO[Fiber[Nothing, List[Nothing]]] =
    Ref.make(0).flatMap(ref => IO.forkAll(List.fill(n)(ref.get.flatMap(value => ref.set(value + 10)).forever)))
  def makeContentious2(n: Int): UIO[Fiber[Nothing, List[Nothing]]] =
    ???

  /**
   * EXERCISE 5
   *
   * Using `Ref#modify`, atomically increment the value by 10, but return the
   * old value, converted to a string.
   */
  val atomicallyIncrementedBy10PlusGet: UIO[String] =
    for {
      ref   <- makeZero
      value <- ref.modify(v => (???, v + 10))
    } yield value

  /**
   * EXERCISE 6
   *
   * Using `Ref#updateSome`, change the state of a given ref to `Active`
   * only if the state is `Closed`.
   */
  trait State
  case object Active extends State
  case object Closed extends State

  def setActive(ref: Ref[State], boolean: Boolean): UIO[State] =
    ???

  /**
   * EXERCISE 7
   *
   * Using `Ref#modifySome`, change the state to `Closed` only if the state was
   * `Active` and return true; if the state is already closed, return false.
   */
  def setClosed(ref: Ref[State], boolean: Boolean): UIO[Boolean] = ???
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
  val makeQueue: UIO[Queue[Int]] = ???

  /**
   * Place `42` into the queue using `Queue#offer`.
   */
  val offered1: UIO[Unit] =
    for {
      queue <- makeQueue
      _     <- (queue ? : UIO[Boolean])
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
  lazy val defaultHandler: Handler                   = ???
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
  val streamStr: Stream[Nothing, Int] = ???

  /**
   * Create a stream using `Stream.fromIterable`
   */
  val stream1: Stream[Nothing, Int] = (1 to 42) ?

  /**
   * Create a stream using `Stream.fromChunk`
   */
  val chunk: Chunk[Int]             = Chunk(43 to 100: _*)
  val stream2: Stream[Nothing, Int] = ???

  /**
   * Make a queue and use it to create a stream using `Stream.fromQueue`
   */
  val stream3: UIO[Stream[Nothing, Int]] = ???

  /**
   * Create a stream from an effect producing a String
   * using `Stream.fromEffect`
   */
  val stream4: Stream[Nothing, String] = ???

  /**
   * Create a stream of ints that starts from 0 until 42,
   * using `Stream#unfold`
   */
  val stream5: Stream[Nothing, Int] = ???

  /**
   * Using `Stream.unfoldM`, create a stream of lines of input from the user,
   * terminating when the user enters the command "exit" or "quit".
   */
  import java.io.IOException
  import scalaz.zio.console.getStrLn
  val stream6: ZStream[Console, IOException, String] = ???

  /**
   * Using `withEffect` log every element.
   */
  val loggedInts: ZStream[Console, Nothing, Int] = stream1 ?

  /**
   * Using `Stream#filter` filter the even numbers
   */
  val evenNumbrers: Stream[Nothing, Int] = stream1 ?

  /**
   * Using `Stream#takeWhile` take the numbers that are less than 10
   */
  val lessThan10: Stream[Nothing, Int] = stream1 ?

  /**
   * Print out each value in the stream using `Stream#foreach`
   */
  val printAll: ZIO[Console, Nothing, Unit] = stream1 ?

  /**
   * Convert every Int into String using `Stream#map`
   */
  val toStr: Stream[Nothing, String] = stream1 ?

  /**
   * Merge two streams together using `Stream#merge`
   */
  val mergeBoth: Stream[Nothing, Int] = (stream1, stream2) ?

  /**
   * Create a Sink using `Sink#readWhile` that takes an input of type String and check if it's not empty
   */
  val sink: Sink[Nothing, String, String, List[String]] = ???

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
  val fiveTimes: Schedule[Any, Int] = ???

  /**
   * Using the `ZIO.repeat`, repeat printing "Hello World"
   * five times to the console.
   */
  val repeated1 = putStrLn("Hello World") ?

  /**
   * Using `Schedule.spaced`, create a schedule that recurs forever every 1 second
   */
  val everySecond: Schedule[Any, Int] = ???

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
  val exponentialSchedule: Schedule[Any, Duration] =
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
  def inputs[A]: Schedule[A, A] = ???

  /**
   * Using `Schedule#collect`, produce a schedule that recurs
   * forever, collecting its inputs into a list.
   */
  def collectedInputs[A]: Schedule[A, List[A]] =
    Schedule.identity[A] ?

  /**
   * Using  `*>`, combine `fiveTimes` and `everySecond` but return the output of `everySecond`.
   */
  val fiveTimesEverySecondR: Schedule[Any, Int] = ???

  /**
   * Produce a jittered schedule that first does exponential spacing (starting
   * from 10 milliseconds), but then after the spacing reaches 60 seconds,
   * switches over to fixed spacing of 60 seconds between recurrences, but will
   * only do that for up to 100 times, and produce a list of the inputs to
   * the schedule.
   */
  import scalaz.zio.random.Random
  def mySchedule[A]: ZSchedule[Clock with Random, A, List[A]] =
    ???
}

object zio_stm {
  // TRef[A]
  // STM[E, A]
}

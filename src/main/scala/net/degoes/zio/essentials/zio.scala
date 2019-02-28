// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package essentials

import java.io.File
import java.util.concurrent.{ Executors, TimeUnit }

import scalaz.zio._
import scalaz.zio.internal.{ Platform, PlatformLive }

import scala.io.Source

/**
 * `ZIO[R, E, A]` is an immutable data structure that models an effectful program.
 *
 *  - The program requires an environment `R`
 *  - The program may fail with an error `E`
 *  - The program may succeed with a value `A`
 */
object zio_types {

  /**
   * Write the following types in terms of the `ZIO` type.
   */
  /**
   * A program that might fail with an error of type `E` or succeed with a
   * value of type `A`.
   */
  type FailOrSuccess[E, A] = ???

  /**
   * A program that never fails and might succeed with a value of type `A`
   */
  type Success[A] = ???

  /**
   * A program that runs forever but might fail with `E`.
   */
  type Forever[E] = ???

  /**
   * A program that cannot fail or succeed with a value.
   */
  type NeverStops = ???

  /**
   * Types aliases built into ZIO.
   */
  /**
   * An effect that may fail with a value of type `E` or succeed with a value
   * of type `A`.
   */
  type IO[E, A] = ???

  /**
   * An effect that may fail with `Throwable` or succeed with a value of
   * type `A`.
   */
  type Task[A] = ???

  /**
   * An effect that cannot fail but may succeed with a value of type `A`.
   */
  type UIO[A] = ???

}

object zio_values {

  /**
   * Using `ZIO.succeed` method. Construct an effect that succeeds with the
   * integer `42`, and ascribe the correct type.
   */
  val ioInt: ??? = ???

  /**
   * Using the `ZIO.succeedLazy` method, construct an effect that succeeds with
   * the (lazily evaluated) specified value and ascribe the correct type.
   */
  lazy val bigList       = (1L to 100000000L).toList
  lazy val bigListString = bigList.mkString("\n")
  val ioString: ???      = ???

  /**
   * Using the `ZIO.fail` method, construct an effect that fails with the string
   * "Incorrect value", and ascribe the correct type.
   */
  val incorrectVal: ??? = ???

  /**
   * Using the `ZIO.effectTotal` method, construct an effect that wraps Scala
   * `println` method, so you have a pure functional version of print line, and
   * ascribe the correct type.
   */
  def putStrLn(line: String): ??? = println(line) ?

  /**
   * Using the `ZIO.effect` method, wrap Scala's `readLine` method to make it
   * purely functional with the correct ZIO error type.
   *
   * Note: You will have to use the `.refineOrDie` method to refine the
   * `Throwable` type into something more specific.
   */
  import java.io.IOException
  val getStrLn: ??? = ???

  /**
   * Using the `ZIO.effect` method, wrap Scala's `getLines` to make it
   * purely functional with the correct ZIO error type.
   *
   * Note: You will have to use the `.refineOrDie` method to refine the
   * `Throwable` type into something more specific.
   */
  def readFile(file: File): IO[???, List[String]] =
    Source.fromFile(file).getLines.toList ?

  /**
   * Using the `ZIO.effect` method, wrap Scala's `Array#update` method to make
   * it purely functional with the correct ZIO error type.
   *
   * Note: You will have to use the `.refineOrDie` method to refine the
   * `Throwable` type into something more specific.
   */
  def arrayUpdate[A](a: Array[A], i: Int, f: A => A): ??? =
    a.update(i, f(a(i))) ?

  /**
   * In order to execute the effectful programs that are described in `ZIO`
   * values, you need to interpret them using the `Runtime` in `ZIO`
   * and call `unsafeRun`
   *       or
   * call the main function in `zio.App`
   */
  object Example extends DefaultRuntime {
    val sayHelloIO: UIO[Unit] = putStrLn("Hello ZIO!")
    //run sayHelloIO using `unsafeRun`
    val sayHello: Unit = ???
  }
}

/**
 * Basic operations in zio
 */
object zio_composition {

  /**
   * Map a ZIO value that produces an Int 42 into ZIO value that produces a String "42"
   * by converting the integer into its string
   * and define the return ZIO type
   */
  val toStr: ??? = IO.succeed(42) ?

  /**
   * Add one to the value of the computation, and define the return ZIO type
   */
  def addOne(i: Int): ??? = IO.succeed(i) ?

  /**
   * Map a ZIO value that fails with an Int 42 into ZIO value that fails with a String "42"
   * by converting the integer into its string
   * and define the return ZIO type
   */
  val toFailedStr: ??? = IO.fail(42) ?

  /**
   * Using `flatMap` check the precondition `p` in the result of the computation of `io`
   * and improve the ZIO types in the following input parameters
   */
  def verify(io: IO[Nothing, Int])(p: Int => Boolean): ??? =
    ???

  /**
   * Using `flatMap` and `map` compute the sum of the values of `a` and `b`
   * and improve the ZIO types
   */
  val a: IO[Nothing, Int]   = IO.succeed(14)
  val b: IO[Nothing, Int]   = IO.succeed(16)
  val sum: IO[Nothing, Int] = ???

  /**
   * Using `flatMap`, implement `ifThenElse`, which checks the ZIO condition and
   * returns the result of either `ifTrue` or `ifFalse`.
   *
   * @example
   * val exampleIf: IO[String, String] =
   *      ifThenElse(IO.succeed(true))(ifTrue = IO.succeed("It's true!"), ifFalse = IO.fail("It's false!"))
   */
  def ifThenElse[E, A](condition: IO[E, Boolean])(ifTrue: IO[E, A], ifFalse: IO[E, A]): IO[E, A] =
    ???

  /**
   * Implement `divide` using `ifThenElse`.
   * if (b != 0), returns `a / b` otherwise, fail with `ArithmeticException`.
   */
  def divide(a: Int, b: Int): IO[ArithmeticException, Int] =
    ???

  /**
   * Using `ifThenElse` implement parseInt that
   * checks if the input is positive, and returns it if it is positive
   * otherwise the program fails with `String` ("This is a negative int") error
   * and define the return ZIO type
   */
  def positive(value: Int): IO[???, ???] = (value > 0) ?

  /**
   * Translate this a recursive function that repeats an action n times
   */
  def repeatN1(n: Int, action: () => Unit): Unit =
    if (n <= 0) ()
    else {
      action()
      repeatN1(n - 1, action)
    }

  def repeatN2[E](n: Int, action: IO[E, Unit]): IO[E, Unit] =
    ???

  /**
   * translate the following loop into its ZIO equivalent
   * and improve the ZIO input/output types.
   */
  def sumList1(ints: List[Int], acc: Int): Int = ints match {
    case Nil     => acc
    case x :: xs => sumList1(xs, acc + x)
  }
  def sumList2(ints: IO[Nothing, List[Int]], acc: IO[Nothing, Int]): IO[Nothing, Int] = ???

  /**
   * translate the following loop into its ZIO equivalent
   * and improve the ZIO input/output types.
   */
  def decrementUntilFour1(int: Int): Unit =
    if (int <= 4) ()
    else decrementUntilFour1(int - 1)
  def decrementUntilFour2(int: Int): IO[Nothing, Unit] = ???

  /**
   * Implement the following loop into its ZIO equivalent.
   */
  def factorial(n: Int): Int =
    if (n <= 1) 1
    else n * factorial(n - 1)
  def factorialIO(n: Int): UIO[Int] =
    ???

  /**
   * Make `factorialIO` tail recursion
   */
  def factorialTailIO(n: Int, acc: Int = 1): UIO[Int] = ???

  /**
   * Translate the following program that uses `flatMap` and `map` into
   * its equivalent using for-comprehension syntax sugar
   */
  def multiply(a: UIO[Int], b: UIO[Int]): UIO[Int] = a.flatMap(v1 => b.map(v2 => v1 * v2))

  /**
   * Translate the following program, which uses for-comprehension, to its
   * equivalent chain of `flatMap` and `map`
   * improve the ZIO return type
   */
  val totalStr1: IO[Nothing, String] = for {
    v1 <- IO.succeed(42)
    v2 <- IO.succeed(58)
    v3 <- IO.succeed("The Total Is: ")
  } yield v3 + (v1 + v2).toString

  val totalStr2: IO[???, ???] = ???

  /**
   * Using `zip`
   * combine the result of two effects into a tuple
   */
  def toTuple(io1: UIO[Int], io2: UIO[Int]): UIO[(Int, Int)] = ???

  /**
   * Using `zipWith`, add the two values computed by these effects.
   */
  val combine: UIO[Int] = UIO.succeed(2).zipWith(UIO.succeed(40))(???)

  /**
   * Using `ZIO.foreach`
   * convert a list of integers into a List of String
   */
  def convert(l: List[Int]): UIO[List[String]] = l.map(_.toString) ?

  /**
   * Using `ZIO.collectAll`
   * evaluate a list of effects and collect the result into an IO of a list with their result
   */
  def collect(effects: List[UIO[Int]]): UIO[List[Int]] = effects ?

  /**
   * rewrite this procedural program into a ZIO equivalent and improve the ZIO input/output types
   */
  def analyzeName1(first: String, last: String): String =
    if ((first + " " + last).length > 20) "Your full name is really long"
    else if ((first + last).contains(" ")) "Your name is really weird"
    else "Your name is pretty normal"

  def analyzeName2(first: UIO[String], last: UIO[String]): UIO[String] = ???

  /**
   * Translate the following procedural program into ZIO.
   */
  def playGame1(): Unit = {
    val number = scala.util.Random.nextInt(5)
    println("Enter a number between 0 - 5: ")
    scala.util.Try(scala.io.StdIn.readLine().toInt).toOption match {
      case None =>
        println("You didn't enter an integer!")
        playGame1()
      case Some(guess) if guess == number =>
        println("You guessed right! The number was " + number)
      case _ =>
        println("You guessed wrong! The number was " + number)
    }
  }
  val playGame2: Task[Unit] = ???
}

object zio_failure {

  /**
   * Using `ZIO.fail` method, create an `IO[String, Int]` value that
   * represents a failure with a string error message, containing
   * a user-readable description of the failure.
   */
  val stringFailure1: IO[String, Int] = ???

  /**
   * Using the `IO.fail` method, create an `IO[Int, String]` value that
   * represents a failure with an integer error code.
   */
  val intFailure: IO[Int, String] = ???

  /**
   * Translate the following exception-throwing program into its ZIO equivalent.
   * And identify the ZIO types
   */
  def accessArr1[A](i: Int, a: Array[A]): A =
    if (i < 0 || i >= a.length)
      throw new IndexOutOfBoundsException(s"The index $i is out of bounds [0, ${a.length} )")
    else a(i)

  def accessArr2[A](i: Int, a: Array[A]): IO[IndexOutOfBoundsException, A] = ???

  /**
   * Translate the following ZIO program into its exception-throwing equivalent.
   */
  def divide1(n: Int, d: Int): IO[ArithmeticException, Int] =
    if (d == 0) IO.fail(new ArithmeticException)
    else IO.succeedLazy(n / d)

  def divide2(n: Int, d: Int): Int = ???

  /**
   * Recover from a division by zero error by using `fold`
   */
  val recovered1: UIO[Option[Int]] = divide1(100, 0) ?

  /**
   * Using `foldM`, Print out either an error message or the division.
   */
  val recovered2: UIO[Unit] = divide1(100, 0) ?

  /**
   * Recover from division by zero error by returning -1 using `either`
   */
  val recovered3: UIO[Int] = divide1(100, 0) ?

  /**
   * Recover from division by zero error by returning -1 using `option`
   */
  val recovered4: UIO[Int] = divide1(100, 0) ?

  /**
   * Use the `orElse` method of `IO` to try `firstChoice`, and fallback to
   * `secondChoice` only if `firstChoice` fails.
   */
  val firstChoice: IO[ArithmeticException, Int] = divide1(100, 0)
  val secondChoice: UIO[Int]                    = IO.succeed(400)
  val combined: UIO[Int]                        = ???

  // io.catchSome
  // io.catchAll

  /**
   * Using `IO.effectTotal`. Import a synchronous effect with a strange code
   */
  val defect1: UIO[Int] = "this is a short text".charAt(30) ?

  /**
   * Throw an Exception in pure code using `IO.succeedLazy`.
   */
  val defect2: UIO[Int] = throw new Exception("oh no!") ?

  /**
   * Using `die`, terminate this program with a fatal error.
   */
  val terminate1: UIO[Int] = IO.unit ?

  /**
   * Using `orDieWith`, terminate the following UIO program with a Throwable
   * when there it fails
   */
  val terminate2: UIO[Int] = IO.fail("unexpected error") ?

  /**
   * Using `orDie`, terminate the following UIO program that fails with an exception
   */
  val terminate3: UIO[Int] = IO.effectTotal("Hello".toInt) ?

  case class DomainError()

  val veryBadIO: IO[DomainError, Int] =
    IO.effectTotal(5 / 0).flatMap(_ => IO.fail(DomainError()))

  /**
   * using `sandbox` recover the `veryBadIO` and catch all errors using `catchAll`
   */
  val caught1: IO[DomainError, Int] = veryBadIO ?

  /**
   * using `sandboxWith` improve the code above and use `catchSome` with a partial match for the possible errors
   */
  def caught2: IO[DomainError, Int] = veryBadIO ?
}

object zio_effects {

  /**
   * Using `ZIO.effectTotal` method. increment the given int and identify the correct ZIO type
   */
  def increment(i: Int): ??? = (i + 1) ?

  /**
   * Using `ZIO.effect` method. wrap Scala's `toInt` method to convert a given string value to int
   * and identify the correct ZIO type, choose which type alias better
   */
  def parseInt(str: String): IO[???, ???] = str.toInt ?

  /**
   * Using `flatMap` and `map`
   * implement a program that reads from the console and
   * calls `parseInt` to convert the given input into to an integer value
   * and define the return ZIO type
   */
  def readInt: IO[???, ???] = scala.io.StdIn.readInt ?

  /**
   * Translate `readInt` using for-comprehension
   */
  def _readInt: IO[???, ???] = ???

  /**
   * Using `catchSome` on `parseInt`, return -1 when
   * the parsing fails with the specified Exception `NumberFormatException` and return a -1
   * and identify the correct ZIO type
   */
  def successfulParseInt(str: String): ??? = ???

  /**
   * Using `catchAll` method, wrap Scala's `getLines` method to
   * import it into the world of pure functional programming and recover from all Exceptions
   * to return a successful result with an empty list
   */
  def readFile(file: File): IO[???, List[String]] =
    Source.fromFile(file).getLines.toList ?

  /**
   * Using `refineOrDie` method, catch the NoSuchElementException and return -1
   */
  def first(as: List[Int]): Task[Int] = as.head ?

  /**
   * Use `ZIO.effectAsync` method to implement the following `sleep`method
   * and choose the correct error type
   */
  val scheduledExecutor = Executors.newScheduledThreadPool(1)
  def sleep(l: Long, u: TimeUnit): IO[Nothing, Unit] =
    scheduledExecutor
      .schedule(new Runnable {
        def run(): Unit = ???
      }, l, u) ?

  /**
   * Wrap the following Java callback API, into an `IO` using `IO.effectAsync`
   * and use other ZIO type alias
   */
  def readChunkCB(success: Array[Byte] => Unit, failure: Throwable => Unit): Unit = ???
  val readChunkIO: IO[Throwable, Array[Byte]]                                     = ???

  /**
   * Rewrite this program using `readChunkIO`
   */
  readChunkCB(
    a1 =>
      readChunkCB(
        a2 => readChunkCB(a3 => println(s"${a1 ++ a2 ++ a3}"), e3 => println(s"${e3.toString}")),
        e2 => println(s"${e2.toString}")
      ),
    e1 => println(s"${e1.toString}")
  )

  /**
   * Using `ZIO.effectAsyncMaybe` wrap the following Java callback API into an `IO`
   * and use better ZIO type alias.
   */
  def readFromCacheCB(key: String)(success: Array[Byte] => Unit, error: Throwable => Unit): Option[Array[Byte]] = ???
  def readFromCacheIO(key: String): IO[Throwable, Array[Byte]]                                                  = ???

  /**
   * using `ZIO.effectAsyncInterrupt` wrap the following Java callback API into an `IO`
   * and use better ZIO type alias.
   */
  case class HttpGetToken(canceller: () => Unit)

  def httpGetCB(url: String)(success: Array[Byte] => Unit, error: Throwable => Unit): HttpGetToken = ???
  def httpGetIO(url: String): IO[Throwable, Array[Byte]]                                           = ???

  /**
   * using `ZIO.effectAsync` import a pure value of type int
   * and identify the ZIO type
   */
  val async42: ??? = 42 ?
}

object impure_to_pure {

  /**
   * Translate the following procedural programs into ZIO.
   */
  def getName1(print: String => Unit, read: () => String): Option[String] = {
    print("Do you want to enter your name?")
    read().toLowerCase.take(1) match {
      case "y" => Some(read())
      case _   => None
    }
  }
  def getName2[E](print: String => IO[E, Unit], read: IO[E, String]): IO[E, Option[String]] =
    ???

  def ageExplainer1(): Unit = {
    println("What is your age?")
    scala.util.Try(scala.io.StdIn.readLine().toInt).toOption match {
      case Some(age) =>
        if (age < 12) println("You are a kid")
        else if (age < 20) println("You are a teenager")
        else if (age < 30) println("You are a grownup")
        else if (age < 50) println("You are an adult")
        else if (age < 80) println("You are a mature adult")
        else if (age < 100) println("You are elderly")
        else println("You are probably lying.")
      case None =>
        println("That's not an age, try again")

        ageExplainer1()
    }
  }

  def ageExplainer2: UIO[Unit] = ???

  def decode1(read: () => Byte): Either[Byte, Int] = {
    val b = read()
    if (b < 0) Left(b)
    else {
      Right(
        b.toInt +
          (read().toInt << 8) +
          (read().toInt << 16) +
          (read().toInt << 24)
      )
    }
  }
  def decode2[E](read: IO[E, Byte]): IO[E, Either[Byte, Int]] = ???

  def replaceV1(as: List[Int], value: Int, newValue: Int): List[Int] =
    as.map(v => if (v == value) newValue else v)

  def replaceV2(as: List[UIO[Int]], value: UIO[Int], newValue: UIO[Int]): UIO[List[Int]] = ???
}

object zio_interop {

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.global

  /**
   * Using `Fiber#toFuture`. Convert the following `Fiber` into a `Future`
   */
  val fiber: Fiber[Throwable, Int] = Fiber.succeed(1)
  val fToFuture: Future[Int]       = ???

  /**
   * Using `Fiber.fromFruture`. Wrap the following Future in a `Fiber`
   */
  val future1                          = () => Future(Thread.sleep(1000))(global)
  val fToFiber: Fiber[Throwable, Unit] = ???

  /**
   * Using `Task#toFuture`. Convert unsafely the following ZIO Task into `Future`.
   */
  val task1: Task[Int]       = IO.effect("wrong".toInt)
  val tToFuture: Future[Int] = ???

  /**
   * Use `Task.fromFuture` to convert the following Scala `Future` into ZIO Task
   */
  val future2             = () => Future.successful("Hello World")
  val task2: Task[String] = ???

}

object zio_resources {
  import java.io.{ File, FileInputStream }
  class InputStream private (is: FileInputStream) {
    def read: IO[Exception, Option[Byte]] =
      IO.effectTotal(is.read).map(i => if (i < 0) None else Some(i.toByte))
    def close: IO[Exception, Unit] =
      IO.effectTotal(is.close())
  }
  object InputStream {
    def openFile(file: File): IO[Exception, InputStream] =
      IO.effectTotal(new InputStream(new FileInputStream(file)))
  }

  /**
   * This following program is the classic paradigm for resource handling using try / finally
   */
  object classic {
    trait Handle
    def openFile(file: String): Handle        = ???
    def closeFile(handle: Handle): Unit       = ???
    def readFile(handle: Handle): Array[Byte] = ???

    // Classic paradigm for safe resource handling using
    // try / finally:
    def safeResource(file: String): Unit = {
      var handle: Handle = null.asInstanceOf[Handle]

      try {
        handle = openFile(file)

        readFile(handle)
      } finally if (handle != null) closeFile(handle)
    }

    def finallyPuzzler(): Unit =
      try {
        try throw new Error("e1")
        finally throw new Error("e2")
      } catch {
        case e: Error => println(e)
      }
  }

  /**
   * Rewrite the following procedural program to ZIO, using `IO.fail` and the `ensuring` method.
   */
  var i = 0
  def increment1(): Unit =
    try {
      i += 1
      throw new Exception("Boom!")
    } finally i -= 1

  def increment2(): Task[Unit] = ???

  /**
   * Rewrite the following procedural program to ZIO, using `IO.fail` and the
   * `ensuring` method of the `IO` object.
   */
  def tryCatch1(): Unit =
    try throw new Exception("Uh oh")
    finally println("On the way out...")
  val tryCatch2: Task[Unit] = ???

  /**
   * Rewrite the `readFile1` function to use `bracket` so resources can be
   * safely cleaned up in the event of errors, defects, or interruption.
   */
  def readFile1(file: File): Task[List[Byte]] = {
    def readAll(is: InputStream, acc: List[Byte]): Task[List[Byte]] =
      is.read.flatMap {
        case None       => IO.succeed(acc.reverse)
        case Some(byte) => readAll(is, byte :: acc)
      }

    for {
      stream <- InputStream.openFile(file)
      bytes  <- readAll(stream, Nil)
      _      <- stream.close
    } yield bytes
  }

  def readFile2(file: File): Task[List[Byte]] = ???

  /**
   * Implement the `tryCatchFinally` method using `bracket` or `ensuring`.
   */
  def tryCatchFinally[E, A](try0: IO[E, A])(catch0: PartialFunction[E, IO[E, A]])(finally0: UIO[Unit]): IO[E, A] = ???

  /**
   * Use the `bracket` method to rewrite the following snippet to ZIO.
   */
  def readFileTCF1(file: File): List[Byte] = {
    var fis: FileInputStream = null

    try {
      fis = new FileInputStream(file)
      val array = Array.ofDim[Byte](file.length.toInt)
      fis.read(array)
      array.toList
    } catch {
      case e: java.io.IOException => Nil
    } finally if (fis != null) fis.close()
  }
  def readFileTCF2(file: File): Task[List[Byte]] = ???

  /**
   *`Managed[R, E, A]` is a managed resource of type `A`, which may be used by
   * invoking the `use` method of the resource. The resource will be automatically
   * acquired before the resource is used, and automatically released after the
   * resource is used.
   */
  /**
   * separate: the acquire, release and the use actions in this procedural program
   * and describe them as ZIO data structures
   */
  trait Status
  object Status {
    case object Opened extends Status
    case object Closed extends Status
  }
  case class Charge(price: Double)
  def buyTicket(ch: Charge) = ???

  var status: Status = Status.Opened
  def receiveEvent(ev: Charge) =
    try {
      if (status == Status.Closed)
        Status.Opened
      else
        buyTicket(ev)
    } finally status = Status.Closed

  /**
   * Define a value of type `Managed` for the acquire and release actions
   * and identify the correct types
   */
  val managed1: Managed[???, ???, ???] = ???

  /**
   * implement the computation of `use` for this Managed
   */
  val use1: IO[???, ???] = ???

  /**
   * write the same example using `manage` in ZIO for the `acquire` action
   */
  val computation: IO[???, ???] = ???

  /**
   * using the data structure `Managed` implement a program that divide a/b then check if it is odd,
   * and finally print out the result on the console
   */
  def acquire(a: Int, b: Int): Task[Int]    = (a / b) ?
  val managed: Managed[Any, Throwable, Int] = ???
  val check: Task[Int]                      = ???

}

object zio_environment {

  /**
   * The Environments in ZIO
   * console (putStr, getStr)
   * clock (currentTime, sleep, nanoTime)
   * random (nextInt, nextBoolean, ...)
   * system (env)
   */
  //write the type of a program that requires scalaz.zio.clock.Clock and which could fail with E or succeed with A
  type ClockIO = ???

  //write the type of a program that requires scalaz.zio.console.Console and which could fail with E or succeed with A
  type ConsoleIO = ???

  //write the type of a program that requires scalaz.zio.system.System and which could fail with E or succeed with A
  type SystemIO = ???

  //write the type of a program that requires scalaz.zio.random.Random and which could fail with E or succeed with A
  type RandomIO = ???

  //write the type of a program that requires Clock and System and which could fail with E or succeed with A
  type ClockWithSystemIO = ???

  //write the type of a program that requires Console and System and which could fail with E or succeed with A
  type ConsoleWithSystemIO = ???

  //write the type of a program that requires Clock, System and Random and which could fail with E or succeed with A
  type ClockWithSystemWithRandom = ???

  //write the type of a program that requires Clock, Console, System and Random and which could fail with E or succeed with A
  type ClockWithConsoleWithSystemWithRandom = ???
}

object zio_dependency_management {
  import scalaz.zio.console
  import scalaz.zio.console.Console 
  import scalaz.zio.clock
  import scalaz.zio.clock.Clock
  import scalaz.zio.system
  import scalaz.zio.system.System
  import java.io.IOException

  /**
   * Using `zio.console.getStrLn`, implement `getStrLn` and identify the 
   * correct type for the ZIO effect.
   */
  val getStrLn: ZIO[???, ???, ???] = ???

  /**
   * Using `zio.console.putStrLn`, implement `putStrLn` and identify the 
   * correct type for the ZIO effect.
   */
  def putStrLn(line: String): ZIO[???, ???, ???] = ???

  /**
   * Using `scalaz.zio.clock.nanoTime`, implement `nanoTime` and identity the 
   * correct type for the ZIO effect.
   */
  val nanoTime: ZIO[???, ???, ???] = ???

  /**
   * Using `scalaz.zio.system`, implement `env` and identify the correct
   * type for the ZIO effect.
   */
  def env(property: String): ZIO[???, ???, ???] = ???

  /**
   * Call three of the preceding methods inside the following `for` 
   * comprehension and identify the correct type for the ZIO effect.
   */
  val program: ZIO[???, ???, ???] = 
    ???

  /**
   * Build a new Service called `Configuration`
   * - define the module
   * - define the interface
   * - define the helper functions (host, port)
   * - implement a trait `Live` that extends the module.
   * - implement all helper functions.
   */
  //Module
  import system.System 

  trait Config {
    val config: ???
  }

  object Config {
    // Service: definition of the methods provided by module:
    trait Service[R] {
      val port: ZIO[R, Nothing, Int]
      val host: ZIO[R, Nothing, String]
    }
    // Production module implementation:
    trait Live extends Config {
      val config: ??? = ???
    }
    object Live extends Live
  }

  //Helpers
  object config_ extends Config.Service[Config] {

    /**
     * Access to the environment `Config` using `accessM`
     */
    override val port = ???
    override val host = ???
  }
  import config_._ 

  /**
   * Write a program that depends on `Config` and `Console` and use the Scala 
   * compiler to infer the correct type.
   */
  val configProgram: ZIO[Config with Console, ???, ???] = ???

  /**
   * Give the `configProgram` its dependencies by supplying it with both `Config`
   * and `Console` modules, and determine the type of the resulting effect.
   */
  configProgram.provide(???)

  /**
   * Create a `Runtime[Config with Console]` that can be used to run any 
   * effect that has a dependency on `Config`:
   */
  val ConfigRuntime: Runtime[Config with Console] = 
    Runtime(??? : Config with Console, PlatformLive.Default)

  /**
   * Define a ZIO value that describes an effect which uses Config with 
   * Console that displays the port and host in the Console and fails 
   * with a String if the host name contains `:`
   */
  val simpleConfigProgram: ZIO[Config, String, Unit] = ???

  /**
   * run the `program` using `unsafeRun`
   * @note When you call unsafeRun the Runtime will provide all the environment that you defined above
   *       when you give a wrong Environment, you will get compile errors.
   */
  val run: ??? = simpleConfigProgram ?

  /**
   * Build a file system service
   */
  //Module
  trait FileSystem {
    val filesystem: ???
  }

  object FileSystem {
    // Service: definition of the methods of the module:
    trait Service[R] {}

    // Production implementation of the module:
    trait Live extends FileSystem {
      val filesystem: ??? = ???
    }
    object Live extends Live
  }
  //Helpers
  object filesystem_ extends FileSystem.Service[FileSystem] {}

  /**
   * Write an effect that uses `FileSystem with Console`.
   */
  val fileProgram: ZIO[FileSystem with Console, ???, ???] = ???

  /**
   * Create a `Runtime` that can execute effects that require 
   * `FileSystem with Console`.
   */
  val FSRuntime: Runtime[FileSystem with Console] = ???

  /**
   * Execute `fileProgram` using `FSRuntime`.
   */
  lazy val fileProgramLive: ??? = FSRuntime.unsafeRun(fileProgram)

  /**
   * Implement a mock file system module.
   */
  trait MockFileSystem extends FileSystem {
    val filesystem = ???
  }

  /**
   * Using `ZIO#provide` with the mock file system module, and a default 
   * runtime, execute `fileProgram`.
   */
  lazy val fileProgramTest: ??? = new DefaultRuntime {}.unsafeRun {
    fileProgram.provide(???)
  }
}

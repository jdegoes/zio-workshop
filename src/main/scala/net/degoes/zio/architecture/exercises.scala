// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio.architecture

import scala.util._

import scalaz.zio._
import net.degoes.zio._
import scala.compat.Platform
import scalaz.zio.internal.PlatformLive
import scalaz.zio.internal.Executor
import scala.concurrent.ExecutionContext
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.Executors
import scalaz.zio.blocking.Blocking

object zio_errors {

  /**
   * EXERCISE 1
   *
   * Correct what is wrong with the return type of the following method.
   */
  def parseInt(value: String): Int = ???

  /**
   * EXERCISE 2
   *
   * Correct what is wrong with the return type of the following method.
   * Hint: You may have to make your own error type or modified the
   * `Result` type.
   */
  sealed trait Result
  object Result {
    final case class Date(value: java.time.Instant) extends Result
    final case class Integer(value: Int)            extends Result
  }
  def parseResult(value: String): Option[Result] = ???

  /**
   * EXERCISE 3
   *
   * Correct what is wrong with the return type of the following method.
   */
  def repeatN(n: Int, action: () => Unit, accum: Boolean = true): Boolean =
    if (n <= 0) accum
    else {
      val result = try {
        action(); true
      } catch { case _: Throwable => false }

      repeatN(n - 1, action, accum && result)
    }

  /**
   * EXERCISE 4
   *
   * Design an error type that can model failure to validate user-input.
   */
  sealed trait InvalidDataError
  object InvalidDataError {}

  /**
   * EXERCISE 5
   *
   * Correct what is wrong with the following data model for HTTP client errors.
   */
  sealed trait HttpClientError
  object HttpClientError {
    final case class Informational(subtype: Int, message: String) extends HttpClientError
    final case class Successful(subtype: Int, message: String)    extends HttpClientError
    final case class Redirection(subtype: Int, message: String)   extends HttpClientError
    final case class ClientError(subtype: Int, message: String)   extends HttpClientError
    final case class ServerError(subtype: Int, message: String)   extends HttpClientError
    final case class OtherError(throwable: Throwable)             extends HttpClientError
  }

  /**
   * EXERCISE 6
   *
   * Using `ZIO#refineOrDie`, refine the error type of the following method to
   * `HttpClientError`.
   */
  trait Request
  trait Response
  class HttpError(code: Int, message: String) extends Throwable
  def httpRequest(request: Request): IO[Throwable, Response] = {
    lazy val response: IO[Throwable, Response] = ??? // Assume this is implemented

    response
  }

  /**
   * EXERCISE 7
   *
   * Modify all these methods to return ZIO effects, using the most narrow
   * error possible.
   */
  def parseInt2(value: String): Option[Int]                       = ???
  def httpReqest2(request: Request): Try[Response]                = ???
  def ensureEmailValid(email: String): Either[InvalidEmail, Unit] = ???
  final case class InvalidEmail(message: String)

  /**
   * EXERCISE 8
   *
   * Revise your solutions to Exercise 7, this time unifying the error types,
   * so all effects can be used in the same `for` comprehension.
   */
  /**
   * EXERCISE 9
   *
   * Create a partial function that (plausibly) maps the "recoverable" errors
   * of `HttpClientError` into the semantic error type `UserServiceError`.
   */
  sealed trait UserServiceError
  object UserServiceError {
    final case class InvalidUserId(id: String) extends UserServiceError
    final object ServiceUnavailable            extends UserServiceError
  }
  val mapErrors1: PartialFunction[HttpClientError, UserServiceError] = {
    case _ => ???
  }

  /**
   * EXERCISE 10
   *
   * Using `ZIO#refineOrDie` and `mapErrors1`, map the output of this
   * function.
   */
  def translateErrors(response: IO[HttpClientError, Response]): IO[UserServiceError, Response] = ???

  /**
   * EXERCISE 11
   *
   * Using `ZIO#sandbox`, bulletproof this web server to defects in the
   * request handler.
   */
  lazy val acceptConnection: Task[(Request, Response => Task[Unit])] = ???
  def launchServer(handler: Request => Task[Response]): UIO[Nothing] = {
    def loop: UIO[Nothing] =
      acceptConnection.foldM(_ => loop, {
        case (request, responder) =>
          handler(request).foldM(
            _ => loop,
            response =>
              responder(response).foldM(
                _ => loop,
                _ => loop
              )
          )
      })

    loop
  }

  /**
   * EXERCISE 12
   *
   * An app is built from services returning the following error types.
   * These error types do not compose. Fix the problem.
   */
  sealed trait CacheError
  sealed trait PricingServiceError
  sealed trait ProductCatalogServiceError
}

object zio_threads {

  /**
   * EXERCISE 1
   *
   * Count how many fibers are will be used in the following effect.
   */
  val effect1 =
    for {
      fiber1 <- fib(10).fork
      fiber2 <- fib(20).fork
      tuple  <- (fiber1 zip fiber2).join
    } yield tuple
  lazy val Effect1FiberCount = ???

  /**
   * EXERCISE 2
   *
   * Create a `Platform` with a single threaded executor.
   */
  val singleThread           = ExecutionContext.fromExecutor(java.util.concurrent.Executors.newSingleThreadExecutor())
  lazy val oneThreadPlatform = PlatformLive.fromExecutionContext(???)

  /**
   * EXERCISE 3
   *
   * Create a `Runtime` from the platform in Exercise 2.
   */
  lazy val runtime = Runtime(new DefaultRuntime {}.Environment, ???)

  /**
   * EXERCISEE 4
   *
   * Execute `effect1` using the `Runtime` you created in Exercise 3.
   */
  lazy val fibs: (BigInt, BigInt) = ???

  /**
   * EXERCISE 5
   *
   * Create a ZIO `Executor` from a Java `ThreadPoolExecutor`.
   */
  lazy val pool         = new ThreadPoolExecutor(???, ???, ???, ???, ???)
  lazy val poolExecutor = PlatformLive.ExecutorUtil.fromThreadPoolExecutor(_ => 1024)(pool)

  /**
   * EXERCISE 6
   *
   * Using `ZIO#lock`, make an effect that locks `effect1` to the pool you
   * created in Exercise 5.
   */
  lazy val lockedEffect1 = effect1 ?

  /**
   * EXERCISE 7
   *
   * Using the `blocking` combinator from `zio.blocking`, make an effect that
   * shifts `effect1` to a blocking thread pool.
   */
  lazy val blockedEffect1: ZIO[Blocking, Nothing, (BigInt, BigInt)] = blocking.blocking(???)

  /**
   * EXERCISE 8
   *
   * Write an `onDatabase` combinator that shifts execution of the provided
   * effect to the `databaseExecutor`, which is a special thread pool used
   * just for database connections.
   */
  lazy val databaseExecutor: Executor                      = Executor.fromExecutionContext(1024)(ExecutionContext.global)
  def onDatabase[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] = ???

  /**
   * EXERCISE 9
   *
   * Implement a combinator to print out thread information before and after
   * executing the specified effect.
   */
  def threadLogged[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] = {
    val log = UIO {
      val thread = Thread.currentThread()

      val id        = thread.getId()
      val name      = thread.getName()
      val groupName = thread.getThreadGroup().getName()

      println(s"Thread($id, $name, $groupName)")
    }

    zio
  }

  /**
   * EXERCISE 10
   *
   * Use the `threadLogged` combinator around different effects below to
   * determine which threads are executing which effects.
   */
  lazy val investigation =
    onDatabase {
      UIO(println("Database")) *>
        blocking.blocking {
          UIO(println("Blocking"))
        } *>
        UIO(println("Database"))
    }

  /**
   * EXERCISE 11
   *
   * Implement a method on `UserService` that can return a decorated
   * `UserService` that locks all methods onto the specified `Executor`.
   */
  trait UserService {
    def lookupName(id: Long): Task[String]

    def lookupAddress(id: Long): Task[(Int, String)]

    def lock(exec: Executor): UserService = ???
  }

  def fib(n: BigInt): UIO[BigInt] =
    if (n <= 1) UIO(n)
    else fib(n - 1).zipWith(fib(n - 2))(_ + _)
}

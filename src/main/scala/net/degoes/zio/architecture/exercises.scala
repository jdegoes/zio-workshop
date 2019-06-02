// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio.architecture

import scala.util._

import scalaz.zio._

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

object zio_threads {}

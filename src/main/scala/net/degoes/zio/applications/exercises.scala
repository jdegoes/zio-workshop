// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package applications

import zio._
import zio.blocking.Blocking
import zio.console._
import zio.duration.Duration
import zio.random.Random

import java.io.IOException

object sharding extends App {
  /**
   * Create N workers reading from a Queue, if one of them fails, 
   * then wait for the other ones to process the current item, but 
   * terminate all the workers.
   */
  def shard[R, E, A](queue: Queue[A], n: Int, worker: A => ZIO[R, E, Unit]): ZIO[R, E, Nothing] = {
    val worker1 = queue.take.flatMap(a => worker(a).uninterruptible).forever

    ZIO.forkAll(List.fill(n)(worker1)).flatMap(fiber =>
      fiber.join
    ) *> ZIO.never
  }

  def run(args: List[String]) = ???
}

object alerting {
  import zio.stm._

  final case class Metrics(
    hourlyErrors: TRef[Int]
  )

  final case class Email(value: String)

  final case class Engineer(email: Email)

  def sendSystemEmail(to: Email, subject: String, body: String): UIO[Unit] = ???

  /**
   * Use STM to alert an engineer when the number of hourly errors exceeds 
   * 100.
   */
  def alertEngineer(metrics: Metrics, onDuty: TRef[Engineer]): UIO[Unit] = 
    ???
}

object parallel_web_crawler {
  trait Web {
    def web: Web.Service
  }
  object Web {
    trait Service {
      def getURL(url: URL): IO[Exception, String]
    }
    trait Live extends Web with Blocking {
      val web = new Service {

        /**
         * EXERCISE 1
         *
         * Use the `effectBlocking` combinator to safely import the Scala `Source.fromURL`
         * side-effect into a purely functional ZIO effect, using `refineOrDie` to narrow
         * the `Throwable` error to `Exceptiono`.
         */
        def getURL(url: URL): IO[Exception, String] = {
          // def effectBlocking[A](sideEffect: => A): ZIO[Blocking, Throwable, A]

          def getURLImpl(url: URL): String =
            scala.io.Source.fromURL(url.url)(scala.io.Codec.UTF8).mkString

          ???
        }
      }
    }
  }

  /**
   * EXERCISE 2
   *
   * Using `ZIO.accessM`, delegate to the `Web` module's `getURL` function.
   */
  def getURL(url: URL): ZIO[Web, Exception, String] = ???

  final case class CrawlState[+E](visited: Set[URL], errors: List[E]) {
    final def visitAll(urls: Set[URL]): CrawlState[E] = copy(visited = visited ++ urls)

    final def logError[E1 >: E](e: E1): CrawlState[E1] = copy(errors = e :: errors)
  }

  /**
   * EXERCISE 3
   *
   * Implement the `crawl` function using the helpers provided in this object.
   *
   * {{{
   * def getURL(url: URL): ZIO[Blocking, Exception, String]
   * def extractURLs(root: URL, html: String): List[URL]
   * }}}
   */
  def crawl[E](
    seeds: Set[URL],
    router: URL => Set[URL],
    processor: (URL, String) => IO[E, Unit]
  ): ZIO[Web, Nothing, List[E]] = ???

  /**
   * A data structure representing a structured URL, with a smart constructor.
   */
  final case class URL private (parsed: io.lemonlabs.uri.Url) {
    import io.lemonlabs.uri._

    final def relative(page: String): Option[URL] =
      scala.util
        .Try(parsed.path match {
          case Path(parts) =>
            val whole = parts.dropRight(1) :+ page.dropWhile(_ == '/')

            parsed.withPath(UrlPath(whole))
        })
        .toOption
        .map(new URL(_))

    def url: String = parsed.toString

    override def equals(a: Any): Boolean = a match {
      case that: URL => this.url == that.url
      case _         => false
    }

    override def hashCode: Int = url.hashCode
  }

  object URL {
    import io.lemonlabs.uri._

    def make(url: String): Option[URL] =
      scala.util.Try(AbsoluteUrl.parse(url)).toOption match {
        case None         => None
        case Some(parsed) => Some(new URL(parsed))
      }
  }

  /**
   * A function that extracts URLs from a given web page.
   */
  def extractURLs(root: URL, html: String): List[URL] = {
    val pattern = "href=[\"\']([^\"\']+)[\"\']".r

    scala.util
      .Try({
        val matches = (for (m <- pattern.findAllMatchIn(html)) yield m.group(1)).toList

        for {
          m   <- matches
          url <- URL.make(m).toList ++ root.relative(m).toList
        } yield url
      })
      .getOrElse(Nil)
  }

  object test {
    val Home          = URL.make("http://scalaz.org").get
    val Index         = URL.make("http://scalaz.org/index.html").get
    val ScaladocIndex = URL.make("http://scalaz.org/scaladoc/index.html").get
    val About         = URL.make("http://scalaz.org/about").get

    val SiteIndex =
      Map(
        Home          -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        Index         -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        ScaladocIndex -> """<html><body><a href="index.html">Home</a><a href="/about">About</a></body></html>""",
        About         -> """<html><body><a href="home.html">Home</a><a href="http://google.com">Google</a></body></html>"""
      )

    val TestWeb = new Web {
      val web = new Web.Service {

        /**
         * EXERCISE 4
         *
         * Use the `SiteIndex` test data to provide an implementation of
         * `getURL` for the `TestWeb` module.
         */
        def getURL(url: URL): IO[Exception, String] =
          ???
      }
    }

    val TestRouter: URL => Set[URL] =
      url => if (url.parsed.apexDomain == Some("scalaz.org")) Set(url) else Set()

    val Processor: (URL, String) => IO[Unit, List[(URL, String)]] =
      (url, html) => IO.succeed(List(url -> html))
  }

  def run(args: List[String]): ZIO[Console, Nothing, Int] =
    (for {
      _ <- putStrLn("Hello World!")
    } yield ()).fold(_ => 1, _ => 0)
}

object circuit_breaker extends App {
  import java.util.concurrent.TimeUnit

  import zio.clock._

  /**
   * EXERCISE 1
   *
   * Design an API for the following circuit breaker trait.
   */
  trait CircuitBreaker[E] {
    def apply[R <: Clock, A](zio: ZIO[R, E, A]): ZIO[R, E, A]
  }

  object CircuitBreaker {

    /**
     * EXERCISE 2
     *
     * Design an immutable data structure to hold a time-windowed histogram of
     * failures and successes.
     */
    private final case class HistogramState(timeUnit: TimeUnit, size: Int /* add more state */ ) {
      def add(millis: Long, b: Boolean): HistogramState = ???

      def failures: Int = ???

      def successes: Int = ???
    }

    /**
     * EXERCISE 3
     *
     * Implement the constructor for `CircuitBreaker`.
     */
    def make[E](rejectedError: E, threshold: Double): UIO[CircuitBreaker[E]] =
      ???

    private class Histogram(ref: Ref[HistogramState]) {
      def add(millis: Long, b: Boolean): UIO[Unit] = ref.update(_.add(millis, b)).unit
      def failures: UIO[Int]                       = ref.get.map(_.failures)
      def successes: UIO[Int]                      = ref.get.map(_.successes)
    }
  }

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = ???
}

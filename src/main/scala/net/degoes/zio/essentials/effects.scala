// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package essentials

object effects {
  sealed trait Program[A] { self =>

    /**
     * Implement flatMap for every type of Program[A] to turn it into a Program[B] using the function `f`.
     */
    final def flatMap[B](f: A => Program[B]): Program[B] = ???

    final def map[B](f: A => B): Program[B] = flatMap(f andThen (Program.succeed(_)))

    final def *>[B](that: Program[B]): Program[B] = self.zip(that).map(_._2)

    final def <*[B](that: Program[B]): Program[A] = self.zip(that).map(_._1)

    /**
     * Implement zip using `flatMap` and `map`.
     */
    final def zip[B](that: Program[B]): Program[(A, B)] = ???

  }
  object Program {
    final case class ReadLine[A](next: String => Program[A])      extends Program[A]
    final case class WriteLine[A](line: String, next: Program[A]) extends Program[A]
    final case class Return[A](value: () => A)                    extends Program[A]

    /**
     * Describe the following Programs
     */
    val readLine: Program[String] = ???

    def writeLine(line: String): Program[Unit] = ???
    def succeed[A](a: => A): Program[A]        = ???
  }

  /**
   * Write a program that does nothing.
   */
  val unit: Program[???] = ???

  /**
   * Write a program that returns a pure value 42.
   */
  val value: Program[???] = ???

  /**
   * Write a program that asks the user for their name.
   */
  val askYourName: Program[Unit] = ???

  /**
   * Write a program that read the name.
   */
  val name: Program[Unit] = ???

  /***
   * Using flatMap and map and the existing functions above,
   * write a program that asks the user for their name and greets them.
   */
  val sayHello: Program[Unit] = ???

  /**
   * Convert a given int to string
   */
  def parseInt(s: String): Program[Option[Int]] = ???

  /**
   * Write a program that reads from the console then parse the given input into int if it possible
   * otherwise it returns None
   */
  val readInt: Program[???] = ???

  /**
   * implement the following effectful procedure, which interprets
   * the description of a given `Program[A]` into A and run it.
   */
  def unsafeRun[A](program: Program[A]): A = ???

  /**
   * implement the following combinator `collectAll` that operates on programs
   */
  def collectAll[A](programs: Seq[Program[A]]): Program[List[A]] = ???

  /**
   * implement the `foreach` function that compute a result for each iteration
   */
  def foreach[A, B](values: Iterable[A])(body: A => Program[B]): Program[List[B]] = ???

  /**
   * Implement the methods of Thunk
   */
  class Thunk[A](val run: () => A) {
    def map[B](ab: A => B): Thunk[B]             = ???
    def flatMap[B](afb: A => Thunk[B]): Thunk[B] = ???
    def attempt: Thunk[Either[Throwable, A]]     = ???
  }
  object Thunk {
    def succeed[A](a: => A): Thunk[A]   = ???
    def fail[A](t: Throwable): Thunk[A] = ???
  }

  /**
 * Build the version of printLn and readLn
 * then make a program base on that
 */

}

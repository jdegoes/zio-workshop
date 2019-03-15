// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package essentials

object effects {

  /**
   * `Console` is an immutable data structure that describes a console program
   * that may involve reading from the console, writing to the console, or
   * returning a value.
   */
  sealed trait Console[A] { self =>

    /**
     * Implement `flatMap` for every type of `Console[A]` to turn it into a
     * `Console[B]` using the function `f`.
     */
    final def flatMap[B](f: A => Console[B]): Console[B] = ???

    final def map[B](f: A => B): Console[B] = flatMap(f andThen (Console.succeed(_)))

    final def *>[B](that: Console[B]): Console[B] = (self zip that).map(_._2)

    final def <*[B](that: Console[B]): Console[A] = (self zip that).map(_._1)

    /**
     * Implement the `zip` function using `flatMap` and `map`.
     */
    final def zip[B](that: Console[B]): Console[(A, B)] = ???
  }
  object Console {
    final case class ReadLine[A](next: String => Console[A])      extends Console[A]
    final case class WriteLine[A](line: String, next: Console[A]) extends Console[A]
    final case class Return[A](value: () => A)                    extends Console[A]

    /**
     * Implement the following helper functions:
     */
    final val readLine: Console[String]              = ???
    final def writeLine(line: String): Console[Unit] = ???
    final def succeed[A](a: => A): Console[A]        = ???
  }

  /**
   * Using the helper functions, write a program that just returns a unit value.
   */
  val unit: Console[???] = ???

  /**
   * Using the helper functions, write a program that just returns the value 42.
   */
  val fortyTwo: Console[???] = ???

  /**
   * Using the helper functions, write a program that asks the user for their name.
   */
  val askName: Console[Unit] = ???

  /**
   * Using the helper functions, write a program that read the name of the user.
   */
  val readName: Console[String] = ???

  /**
   * Using the helper functions, write a program that greets the user by their name.
   */
  def greetUser(name: String): Console[Unit] =
    ???

  /***
   * Using `flatMap` and the preceding three functions, write a program that
   * asks the user for their name, reads their name, and greets them.
   */
  val sayHello: Console[Unit] =
    ???

  /**
   * Write a program that reads from the console then parse the given input into int if it possible
   * otherwise it returns None
   */
  val readInt: Console[???] = ???

  /**
   * implement the following effectful procedure, which interprets
   * the description of a given `Console[A]` into A and run it.
   */
  def unsafeRun[A](program: Console[A]): A =
    ???

  /**
   * implement the following combinator `collectAll` that operates on programs
   */
  def collectAll[A](programs: List[Console[A]]): Console[List[A]] =
    ???

  /**
   * implement the `foreach` function that compute a result for each iteration
   */
  def foreach[A, B](values: List[A])(body: A => Console[B]): Console[List[B]] =
    ???

  /**
   * Using `Console.writeLine` and `Console.readLine`, map the following
   * list of strings into a list of programs, each of which writes a
   * question and reads an answer.
   */
  val questions =
    List(
      "What is your name?",
      "Where where you born?",
      "Where do you live?",
      "What is your age?",
      "What is your favorite programming language?"
    )
  val answers: List[Console[String]] = ???

  /**
   * Using `collectAll`, transform `answers` into a program that returns
   * a list of strings.
   */
  val answers2: Console[List[String]] = ???

  /**
   * Now using only `questions` and `foreach`, write a program that is
   * equivalent to `answers2`.
   */
  val answers3: Console[List[String]] = foreach(???) { question =>
    ???
  }

  /**
   * Implement the methods of Thunk
   */
  class Thunk[A](val unsafeRun: () => A) {
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
   * then make a simple program base on that.
   */
  def printLn(line: String): Thunk[Unit] = ???
  def readLn: Thunk[String]              = ???

  val thunkProgram: Thunk[Unit] = ???
}

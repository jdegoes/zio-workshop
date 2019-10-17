// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package essentials
import net.degoes.zio.essentials.effects.Console.ReadLine
import net.degoes.zio.essentials.effects.Console.WriteLine
import net.degoes.zio.essentials.effects.Console.Return

object effects {

  /**
   * `Console` is an immutable data structure that describes a console program
   * that may involve reading from the console, writing to the console, or
   * returning a value.
   */
  sealed trait Console[A] { self =>

    /**
     * EXERCISE 1
     * 
     * Implement `flatMap` for every type of `Console[A]` to turn it into a
     * `Console[B]` using the function `f`.
     */
    final def flatMap[B](f: A => Console[B]): Console[B] =
      self match {
        case Console.ReadLine(next)        => ???
        case Console.WriteLine(line, next) => ???
        case Console.Return(value)         => ???
      }

    final def map[B](f: A => B): Console[B] = flatMap(f andThen (Console.succeed(_)))

    final def *>[B](that: Console[B]): Console[B] = (self zip that).map(_._2)

    final def <*[B](that: Console[B]): Console[A] = (self zip that).map(_._1)

    /**
     * EXERCISE 2
     * 
     * Implement the `zip` function using `flatMap` and `map`.
     */
    final def zip[B](that: Console[B]): Console[(A, B)] = ???
  }
  object Console {
    final case class ReadLine[A](next: String => Console[A])      extends Console[A]
    final case class WriteLine[A](line: String, next: Console[A]) extends Console[A]
    final case class Return[A](value: () => A)                    extends Console[A]

    /**
     * EXERCISE 3
     * 
     * Implement the following helper functions.
     */
    final val readLine: Console[String]              = ???
    final def writeLine(line: String): Console[Unit] = ???
    final def succeed[A](a: => A): Console[A]        = ???
  }

  /**
   * EXERCISE 4
   * 
   * Using the helper functions, write a program that just returns a unit value.
   */
  val unit: Console[Unit] = ???

  /**
   * EXERCISE 5
   * 
   * Using the helper functions, write a program that just returns the value 42.
   */
  val fortyTwo: Console[Int] = ???

  /**
   * EXERCISE 6
   * 
   * Using the helper functions, write a program that asks the user for their name.
   */
  val askName: Console[Unit] = Console.writeLine("What is your name?")

  /**
   * EXERCISE 7
   * 
   * Using the helper functions, write a program that reads a line of input from
   * the user.
   */
  val readName: Console[String] = Console.readLine 

  /**
   * EXERCSE 8
   * 
   * Write a function that greets the user by the specified name.
   */
  def greetUser(name: String): Console[Unit] = Console.writeLine(s"Hello, ${name}, good to meet you!")

  /**
   * EXERCISE 9
   * 
   * Using `flatMap` and the preceding three functions, write a program that
   * asks the user for their name, reads their name, and greets them.
   */
  val sayHello: Console[Unit] = ???

  /**
   * EXERCISE 10
   * 
   * Implement the following effectful procedure, which effectfully interprets
   * the description of a given `Console[A]` into an `A`.
   */
  def unsafeRun[A](program: Console[A]): A = ???

  /**
   * EXERCISE 11
   * 
   * Implement the following combinator `collectAll` that transforms a list of
   * console programs into a console program that returns a list of collected
   * results of the individual programs.
   */
  def collectAll[A](programs: List[Console[A]]): Console[List[A]] =
    ???

  /**
   * EXERCISE 12
   * 
   * Implement the `foreach` function, which iterates over the values in a list,
   * passing every value to a body, which effectfully computes a `B`, and
   * collecting all such `B` values in a list.
   */
  def foreach[A, B](values: List[A])(body: A => Console[B]): Console[List[B]] = ???
  /**
   * EXERCISE 13
   *
   * Using `Console.writeLine` and `Console.readLine`, map the following
   * list of strings into a list of programs, each of which prints out its
   * question and reads the answer.
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
   * EXERCISE 14
   * 
   * Using `collectAll`, transform `answers` into a program that returns
   * a list of strings.
   */
  val answers2: Console[List[String]] = ???

  /**
   * EXERCISE 15
   * 
   * Now using only `questions` and `foreach`, write a program that is
   * equivalent to `answers2`.
   */
  val answers3: Console[List[String]] = ???
  
  /**
   * EXERCISE 16
   * 
   * Implement the missing methods of Thunk.
   */
  class Thunk[A](val unsafeRun: () => A) {
    def map[B](ab: A => B): Thunk[B]             = ???
    def flatMap[B](afb: A => Thunk[B]): Thunk[B] = ???
    def either: Thunk[Either[Throwable, A]]      = ???
  }
  object Thunk {
    def succeed[A](a: => A): Thunk[A]   = ???
    def fail[A](t: Throwable): Thunk[A] = ???
  }

  /**
   * EXERCISE 17
   * 
   * Build the version of printLn and readLn
   * then make a simple program base on that.
   */
  def printLn(line: String): Thunk[Unit] = ???
  def readLn: Thunk[String]              = ???

  val thunkProgram: Thunk[Unit] = ???
}

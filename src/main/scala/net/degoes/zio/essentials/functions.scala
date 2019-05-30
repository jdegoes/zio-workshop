// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package essentials

import java.time.LocalDate

/**
 * Functions are total, deterministic, and free of side effects.
 */
object functions {

  /**
   * EXERCISE 1
   *
   * Convert the following partial procedure into a function.
   */
  def parseInt1(s: String): Int   = s.toInt
  def parseInt2( /* ??? */ ): ??? = ???

  /**
   * EXERCISE 2
   *
   * Convert the following partial procedure into a function.
   */
  def head1[A](l: List[A]): A    = l.head
  def head2[A]( /* ??? */ ): ??? = ???

  /**
   * EXERCISE 3
   *
   * Convert the following non-deterministic procedure into a deterministic function.
   */
  def increment1: Int              = scala.util.Random.nextInt(0) + 1
  def increment2( /* ??? */ ): ??? = ???

  /**
   * EXERCISE 4
   *
   * Convert the following non-deterministic procedure into a deterministic function.
   */
  def nextDay1: LocalDate        = LocalDate.now.plusDays(1)
  def nextDay2( /* ??? */ ): ??? = ???

  /**
   * EXERCISE 5
   *
   * Convert the following side-effecting procedure into a pure function.
   */
  def get1(a: Int): Int = {
    println(s"the value is: $a")
    a
  }
  def get2( /* ??? */ ): ??? = ???

  /**
   * EXERCISE 6
   *
   * Convert the following side-effecting procedure into a pure function.
   */
  def updateArray1[A](arr: Array[A], i: Int, f: A => A): Unit =
    arr.update(i, f(arr(i)))
  def updateArray2[A]( /* ??? */ ): ??? = ???

  /**
   * EXERCISE 7
   *
   * Design a purely functional API for drawing a bitmap image on a canvas.
   */
  trait Draw {
    def goLeft(): Unit
    def goRight(): Unit
    def goUp(): Unit
    def goDown(): Unit
    def draw(): Unit
    def finish(): List[List[Boolean]]
  }
  def draw1(size: Int): Draw = new Draw {
    val canvas = Array.fill(size, size)(false)
    var x      = 0
    var y      = 0

    def goLeft(): Unit  = x -= 1
    def goRight(): Unit = x += 1
    def goUp(): Unit    = y += 1
    def goDown(): Unit  = y -= 1
    def draw(): Unit = {
      def wrap(x: Int): Int =
        if (x < 0) (size - 1) + ((x + 1) % size) else x % size

      val x2 = wrap(x)
      val y2 = wrap(y)

      canvas.updated(x2, canvas(x2).updated(y2, true))
    }
    def finish(): List[List[Boolean]] =
      canvas.map(_.toList).toList
  }
  def draw2( /* ... */ ): ??? = ???

}

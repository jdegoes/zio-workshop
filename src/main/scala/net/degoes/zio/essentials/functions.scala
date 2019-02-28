// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package essentials

import java.time.LocalDate

/**
 * Functions are total, deterministic, and free of side effects.
 */
object functions {

  /**
   * Turn the following pseudo-functions into functions.
   */
  /**
   * Partial => Total
   */
  def parseInt1(s: String): Int   = s.toInt
  def parseInt2( /* ??? */ ): ??? = ???

  def divide1(a: Int, b: Int): Int = a / b
  def divide2( /* ??? */ ): ???    = ???

  def head1[A](l: Seq[A]): A     = l.head
  def head2[A]( /* ??? */ ): ??? = ???

  def secondChar1(str: String): Char = str.charAt(2)
  def secondChar2( /* ??? */ ): Char = ???

  /**
   * Non-deterministic => Deterministic
   */
  def increment1: Int              = scala.util.Random.nextInt(0) + 1
  def increment2( /* ??? */ ): ??? = ???

  def nextDay1: LocalDate        = LocalDate.now.plusDays(1)
  def nextDay2( /* ??? */ ): ??? = ???

  case object IncorrectAge extends Exception
  def computeAge1(year: Int): Int = {
    val age = LocalDate.now.getYear - year
    if (age < 0) throw IncorrectAge
    else age
  }
  def computeAge2( /*???*/ ): ??? = ???

  /**
   * Side effects => Free of side effects
   */
  def get1(a: Int): Int = {
    println(s"the given of a is: $a")
    a
  }
  def get2( /* ??? */ ): ??? = ???

  def sumN1(n: Int): Int = {
    var result = 0
    (1 to n).foreach(i => result = result + i)
    result
  }
  def sumN2( /* ??? */ ): ??? = ???

  def updateArray1[A](arr: Array[A], i: Int, f: A => A): Unit =
    arr.update(i, f(arr(i)))
  def updateArray2[A]( /* ??? */ ): ??? = ???

  trait CreditCard
  trait PaymentProcessor {
    def charge(cc: CreditCard, price: Double): Unit
  }
  case class Coffee(sugar: Int) {
    val price = 2.5
  }

  def buyCoffee1(withSugar: Option[Int], p: PaymentProcessor, cc: CreditCard): Coffee = {
    val cup =
      withSugar.fold(Coffee(0))(n => Coffee(n))
    p.charge(cc, cup.price)
    cup
  }
  final case class Charge(cc: CreditCard, price: Double)
  def buyCoffee2( /*???*/ ): ??? = ???

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

// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio.essentials

/**
 * Pure functions should be:
 *     Total
 *     Deterministic
 *     Free of side effect
 */
object functions {

  type ??? = Nothing

  /**
   * EXERCISE 1
   * Turn those following "non total" functions into total
   */
  def parseInt1(s: String): Int   = s.toInt
  def parseInt2( /* ??? */ ): ??? = ???

  def divide1(a: Int, b: Int): Int = a / b
  def divide2( /* ??? */ ): ???    = ???

  def firstElement1[A](l: Seq[A]): A     = l.head
  def firstElement2[A]( /* ??? */ ): ??? = ???

  /**
   * EXERCISE 2
   * Turn those following "non deterministic" functions into deterministic
   */
  def increment1: Int              = scala.util.Random.nextInt(0) + 1
  def increment2( /* ??? */ ): ??? = ???

  def nextDay1: java.time.LocalDate = java.time.LocalDate.now.plusDays(1)
  def nextDay2( /* ??? */ ): ???    = ???

  /**
   * EXERCISE 3
   * Turn those following "non pure" functions into pure and free of side effect
   */
  def updateArray1[A](arr: Array[A], i: Int, f: A => A): Unit =
    arr.update(i, f(arr(i)))
  def updateArray2[A]( /* ??? */ ): ??? = ???

  def get1(a: Int): Int = {
    println(s"the given Int is $a")
    a
  }
  def get2( /* ??? */ ): ??? = ???

  trait CreditCard
  trait Payment {
    def charge(cc: CreditCard, price: Double)
  }
  case class Coffee(sugar: Int) {
    val price = 2.5
  }

  def makeCoffee1(withSugar: Option[Int], p: Payment, cc: CreditCard): Coffee = {
    val cup =
      withSugar.fold(Coffee(0))(n => Coffee(n))
    p.charge(cc, cup.price)
    cup
  }
  def makeCoffee2( /*???*/ ): ??? = ???
}

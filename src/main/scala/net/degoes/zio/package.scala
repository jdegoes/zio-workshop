package net.degoes

import scalaz.zio.IO

package object zio {

  type ???      = Nothing
  type Task[+A] = IO[Throwable, A]
  type UIO[+A]  = IO[Nothing, A]

  implicit class FixMe[A](a: A) {
    def ? : Nothing = ???
  }
}

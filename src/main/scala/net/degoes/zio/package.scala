package net.degoes

import scalaz.zio.IO

package object zio {
  type ??? = Nothing

  implicit class FixMe[A](a: A) {
    def ? : Nothing = ???
  }
}

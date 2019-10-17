package net.degoes

import _root_.zio.IO

package object zio {
  type ??? = Nothing

  implicit class FixMe[A](a: A) {
    def ? : Nothing = ???
  }
}

package org.jpioug.template.python


class State[S, A](x: S => (A, S)) {
  val runState = x

  def flatMap[B](f: A => State[S, B]): State[S, B] = {
    new State(s => {
      val (v, s_) = x(s)
      f(v).runState(s_)
    })
  }

  def map[B](f: A => B): State[S, B] = {
    new State(s => {
      val (v, s_) = x(s)
      State.returns[S, B](f(v)).runState(s_)
    })
  }
}

object State {
  def returns[S, A](a: A): State[S, A] = new State(s => (a, s))
}

object ModelState {
  def get[S]: State[S, S] = new State(s => (s, s))

  def put[S](s: S): State[S, Unit] = new State(_ => ((), s))
}
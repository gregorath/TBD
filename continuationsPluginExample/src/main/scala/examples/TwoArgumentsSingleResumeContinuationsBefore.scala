package examples

import continuations.Suspend

@main def TwoArgumentsSingleResumeContinuationsBefore =
  def twoArgumentsSingleResumeContinuationsBefore(x: Int, y: Int)(using s: Suspend): Int =
    println("Hello")
    val z = x + y
    s.suspendContinuation(_.resume(Right(1)))
  println(twoArgumentsSingleResumeContinuationsBefore(1, 2))

package examples

import continuations.*

@main def ProgramSuspendingContinuationNoParamResumeIgnoreResult =
  def fooTest()(using s: Suspend): Int =
    println("Start")
    s.suspendContinuation[Unit] { _.resume(Right { println("Hello") }) }
    println("World")
    val x = 1
    s.suspendContinuation[Boolean] { continuation =>
      val q = "World"
      println("Hi")
      continuation.resume(Right { println(q); false })
    }
    s.suspendContinuation[Int] { continuation =>
      continuation.resume(Left(new Exception("error")))
    }
    10
  fooTest()

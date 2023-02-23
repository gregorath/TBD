package examples

import continuations.*

@main def ZeroArgumentsZeroContinuations =
  println(ExampleObject.oneArgumentsSingleResumeContinuations(1))
  println(program.foo(2))

object ExampleObject:
  private def test(x: Int) = x + 1
  private val z = 1
  def oneArgumentsSingleResumeContinuations(x: Int)(using s: Suspend): Int =
    def test2(x: Int) = x + 1
    val z2 = 1
    s.suspendContinuation[Int] { continuation =>
      def test3(x: Int) = x // TODO: x+1
      val z3 = 1
      continuation.resume {
        val z4 = 1
        def test4(x: Int) = x // TODO: x+1
        Right(test(x) + 1 + z + z2 + test2(x) + z3 + test3(x) + z4 + test4(x))
      }
    }
    val qq = s.suspendContinuation[Int] { continuation =>
      continuation.resume(Right(test(x) + 1))
    }
    2 + qq

object program {
  def foo(x: Int)(using Suspend): Int = {
    val q = summon[Suspend].suspendContinuation[Int] { continuation =>
      continuation.resume(Right(1 + x))
    }
    val y = 2
    val qq = summon[Suspend].suspendContinuation[Int] { continuation =>
      continuation.resume(Right(1 + x))
    }
    q + 1 + qq + x + y
  }
}

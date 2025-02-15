package continuations {
  final lazy module val ExampleObject: continuations.ExampleObject = new continuations.ExampleObject()
  @SourceFile("compileFromString.scala") final module class ExampleObject() extends Object() {
    this: continuations.ExampleObject.type =>
    private def writeReplace(): AnyRef = new scala.runtime.ModuleSerializationProxy(classOf[continuations.ExampleObject.type])
    private[this] def method1(x: Int): Int = x.+(1)
    protected val z1: Int = 1
    class ExampleObject$continuations$1($completion: continuations.Continuation[Any | Null]) extends continuations.jvm.internal.ContinuationImpl(
      $completion
    , $completion.context) {
      var I$0: Any = _
      var I$1: Any = _
      var I$2: Any = _
      var I$3: Any = _
      def I$0_=(x$0: Any): Unit = ()
      def I$1_=(x$0: Any): Unit = ()
      def I$2_=(x$0: Any): Unit = ()
      def I$3_=(x$0: Any): Unit = ()
      var $result: Either[Throwable, Any | Null | continuations.Continuation.State.Suspended.type] = _
      var $label: Int = _
      def $result_=(x$0: Either[Throwable, Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State)]): Unit = ()
      def $label_=(x$0: Int): Unit = ()
      protected override def invokeSuspend(
        result: Either[Throwable, Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State)]
      ): Any | Null =
        {
          this.$result = result
          this.$label = this.$label.|(scala.Int.MinValue)
          continuations.ExampleObject.continuations(null, null, this)
        }
    }
    def continuations(x: Int, y: Int, completion: continuations.Continuation[Int]):
      Int | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State)
     =
      {
        var x##1: Int = x
        var y##1: Int = y
        var suspension1: Int = null
        var suspension2: Int = null
        def method2(x: Int): Int = x.+(1)
        def method5(x: Int): Int = x.+(1)
        {
          val $continuation: continuations.ExampleObject.ExampleObject$continuations$1 =
            completion match
              {
                case x$0 @ <empty> if
                  x$0.isInstanceOf[continuations.ExampleObject.ExampleObject$continuations$1].&&(
                    x$0.asInstanceOf[continuations.ExampleObject.ExampleObject$continuations$1].$label.&(scala.Int.MinValue).!=(0)
                  )
                 =>
                  x$0.asInstanceOf[continuations.ExampleObject.ExampleObject$continuations$1].$label =
                    x$0.asInstanceOf[continuations.ExampleObject.ExampleObject$continuations$1].$label.-(scala.Int.MinValue)
                  x$0
                case _ => new continuations.ExampleObject.ExampleObject$continuations$1(completion)
              }
          val $result: Either[Throwable, Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State)] =
            $continuation.$result
          $continuation.$label match
            {
              case 0 =>
                continuations.Continuation.checkResult($result)
                val z2: Int = 1
                $continuation.I$0 = y##1
                $continuation.I$1 = x##1
                $continuation.$label = 1
                val safeContinuation: continuations.SafeContinuation[Int] = continuations.SafeContinuation.init[Int]($continuation)
                {
                  {
                    def method3(x: Int): Int = x.+(1)
                    val z3: Int = 1
                    safeContinuation.resume(
                      {
                        val z4: Int = 1
                        def method4(x: Int): Int = x.+(1)
                        Right.apply[Nothing, Int](
                          continuations.ExampleObject.method1(x##1).+(1).+(continuations.ExampleObject.z1).+(z2).+(method2(y##1)).+(z3).+(
                            method3(x##1)
                          ).+(z4).+(method4(x##1))
                        )
                      }
                    )
                  }
                }
                val orThrow: Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State) =
                  safeContinuation.getOrThrow()
                if orThrow.==(continuations.Continuation.State.Suspended) then return continuations.Continuation.State.Suspended
                suspension1 = orThrow.asInstanceOf[Int]
                return[label1] ()
              case 1 =>
                y##1 = $continuation.I$0
                x##1 = $continuation.I$1
                continuations.Continuation.checkResult($result)
                suspension1 = $result.asInstanceOf[Int]
                label1[Unit]: <empty>
                $continuation.I$0 = y##1
                $continuation.I$1 = x##1
                $continuation.I$2 = suspension1
                $continuation.$label = 2
                val safeContinuation: continuations.SafeContinuation[Int] = continuations.SafeContinuation.init[Int]($continuation)
                {
                  {
                    safeContinuation.resume(Right.apply[Nothing, Int](method1(x##1).+(1)))
                  }
                }
                val orThrow: Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State) =
                  safeContinuation.getOrThrow()
                if orThrow.==(continuations.Continuation.State.Suspended) then return continuations.Continuation.State.Suspended
                return[label2] ()
              case 2 =>
                y##1 = $continuation.I$0
                x##1 = $continuation.I$1
                suspension1 = $continuation.I$2
                continuations.Continuation.checkResult($result)
                label2[Unit]: <empty>
                val z5: Int = suspension1
                $continuation.I$0 = y##1
                $continuation.I$1 = x##1
                $continuation.I$2 = suspension1
                $continuation.$label = 3
                val safeContinuation: continuations.SafeContinuation[Int] = continuations.SafeContinuation.init[Int]($continuation)
                {
                  {
                    safeContinuation.resume(Right.apply[Nothing, Int](z5.+(suspension1).+(method5(y##1))))
                  }
                }
                val orThrow: Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State) =
                  safeContinuation.getOrThrow()
                if orThrow.==(continuations.Continuation.State.Suspended) then return continuations.Continuation.State.Suspended
                suspension2 = orThrow.asInstanceOf[Int]
              case 3 =>
                y##1 = $continuation.I$0
                x##1 = $continuation.I$1
                suspension1 = $continuation.I$2
                continuations.Continuation.checkResult($result)
                suspension2 = $result.asInstanceOf[Int]
              case _ => throw new IllegalArgumentException("call to \'resume\' before \'invoke\' with coroutine")
            }
        }
        val z6: Int = 1
        def method6(x: Int): Int = x.+(1)
        1.+(suspension1).+(suspension2).+(z6).+(continuations.ExampleObject.method1(x##1)).+(method2(x##1)).+(method5(x##1)).+(method6(x##1))
      }
  }
  final lazy module val compileFromString$package:
    continuations.compileFromString$package
   = new continuations.compileFromString$package()
  @SourceFile("compileFromString.scala") final module class
    compileFromString$package
  () extends Object() { this: continuations.compileFromString$package.type =>
    private def writeReplace(): AnyRef =
      new scala.runtime.ModuleSerializationProxy(classOf[continuations.compileFromString$package.type])
    def foo: Int = continuations.ExampleObject.continuations(1, 2, continuations.jvm.internal.ContinuationStub.contImpl)
  }
}
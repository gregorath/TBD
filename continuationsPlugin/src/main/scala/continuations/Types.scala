package continuations

import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols.defn
import dotty.tools.dotc.core.Types.Type

import scala.annotation.tailrec

trait Types {
  private[continuations] def flattenTypes(typ: Type)(using Context): List[Type] =
    @tailrec
    def recur(t: Type, acc: List[Type]): List[Type] =
      t match
        case defn.FunctionOf(_, rt, _, _) =>
          recur(rt, acc :+ t)
        case _ => acc :+ t

    recur(typ, List.empty[Type])
}

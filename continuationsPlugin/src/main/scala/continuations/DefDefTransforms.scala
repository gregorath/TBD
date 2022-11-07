package continuations

import continuations.DefDefTransforms.*
import dotty.tools.dotc.ast.Trees.*
import dotty.tools.dotc.ast.tpd.TreeOps
import dotty.tools.dotc.ast.{tpd, Trees}
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.{ctx, Context}
import dotty.tools.dotc.core.Flags
import dotty.tools.dotc.core.Names.termName
import dotty.tools.dotc.core.StdNames.nme
import dotty.tools.dotc.core.Symbols.{newSymbol, ClassSymbol, Symbol, TermSymbol}
import dotty.tools.dotc.core.Types.{OrType, Type}

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer

class DefDefTransforms(
    continuationTraitSym: ClassSymbol,
    safeContinuationClassSym: ClassSymbol,
    interceptedMethodSym: TermSymbol
)(using Context) {
  import tpd.*

  private val continuationObjectSym: Symbol = continuationTraitSym.companionModule

  /*
   * It works with only one top level `suspendContinuation` that just calls `resume`
   * and it replaces the parent method body keeping any rows before the `suspendContinuation` (but not ones after).
   */
  private def transformSuspendNoParametersOneContinuationResume(
      tree: DefDef,
      resumeArg: Tree): DefDef =
    val parent: Symbol = tree.symbol
    val returnType: Type = tree.tpt.tpe

    val rowsBeforeSuspend =
      tree.rhs match
        case Block(trees, expr) =>
          (trees :+ expr).takeWhile(_.symbol.showFullName != suspendContinuationFullName)
        case _ =>
          List.empty[Tree]

    val continuationTyped: Type =
      continuationTraitSym.typeRef.appliedTo(returnType)

    val completion =
      newSymbol(parent, termName("completion"), Flags.LocalParam, continuationTyped)

    val continuation1: ValDef =
      ValDef(
        newSymbol(parent, termName("continuation1"), Flags.Local, continuationTyped),
        ref(completion))

    val undecidedState =
      ref(continuationObjectSym).select(termName("State")).select(termName("Undecided"))

    val interceptedCall =
      ref(interceptedMethodSym)
        .appliedToType(returnType)
        .appliedTo(ref(continuation1.symbol))
        .appliedToNone

    val safeContinuationConstructor =
      New(ref(safeContinuationClassSym))
        .select(nme.CONSTRUCTOR)
        .appliedToType(returnType)
        .appliedTo(interceptedCall, undecidedState)

    val safeContinuation: ValDef =
      ValDef(
        newSymbol(
          parent,
          termName("safeContinuation"),
          Flags.Local,
          safeContinuationConstructor.tpe),
        safeContinuationConstructor)

    val safeContinuationRef =
      ref(safeContinuation.symbol)

    val suspendContinuation: ValDef =
      ValDef(
        newSymbol(
          parent,
          termName("suspendContinuation"),
          Flags.Local,
          ctx.definitions.IntType),
        Literal(Constant(0))
      )

    val suspendContinuationResume =
      safeContinuationRef.select(termName("resume")).appliedTo(resumeArg)

    val suspendContinuationGetThrow =
      safeContinuationRef.select(termName("getOrThrow")).appliedToNone

    val body = Block(
      rowsBeforeSuspend ++
        (continuation1 :: safeContinuation :: suspendContinuation :: suspendContinuationResume :: Nil),
      suspendContinuationGetThrow
    )

    val suspendedState =
      ref(continuationObjectSym).select(termName("State")).select(termName("Suspended"))

    val finalMethodReturnType =
      OrType(
        OrType(ctx.definitions.AnyType, ctx.definitions.NullType, soft = false),
        suspendedState.symbol.namedType,
        soft = false)

    DefDef(parent.asTerm, List(List(completion)), finalMethodReturnType, body)

  def transformSuspendContinuation(tree: DefDef): DefDef =
    tree match
      case HasSuspendParameter(_) =>
        tree match
          case CallsContinuationResumeWith(resumeArg) =>
            transformSuspendNoParametersOneContinuationResume(tree, resumeArg)
          case _ => tree
      case _ => tree
}

object DefDefTransforms {
  private val suspendFullName = "continuations.Suspend"
  private val suspendContinuationFullName = "continuations.Continuation.suspendContinuation"
  private val resumeFullName = "continuations.Continuation.resume"

  object HasSuspendParameter {
    def unapply(tree: tpd.DefDef)(using c: Context): Option[tpd.DefDef] =
      Option(tree).filter {
        _.paramss.exists {
          _.exists { v =>
            v.symbol.is(Flags.Given) && v.tpe.classSymbol.showFullName == suspendFullName
          }
        }
      }
  }

  object CallsContinuationResumeWith {
    def unapply(tree: tpd.DefDef)(using c: Context): Option[tpd.Tree] =
      val args =
        tree
          .rhs
          .toList
          .flatMap {
            case Block(trees, tree) => trees :+ tree
            case tree => List(tree)
          }
          .filter { _.symbol.showFullName == suspendContinuationFullName }
          .flatMap(_.filterSubTrees(_.symbol.showFullName == resumeFullName))
          .flatMap {
            case Apply(_, List(arg)) => Option(arg)
            case _ => None
          }

      Option.when(args.size == 1)(args.head)
  }
}

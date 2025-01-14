package com.sksamuel.scapegoat.inspections.collections

import com.sksamuel.scapegoat.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class AvoidSizeEqualsZero extends Inspection("Avoid Traversable.size == 0, use Traversable.isEmpty instead", Levels.Warning) {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private val Size = TermName("size")
      private val Length = TermName("length")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Select(q, Size | Length), TermName("$eq$eq")), List(Literal(Constant(0)))) if isTraversable(q) =>
            context.warn(tree.pos, self,
              "Traversable.size is slow for some implementations. Prefer .isEmpty which is O(1): " + tree.toString().take(100))
          case _ => continue(tree)
        }
      }
    }
  }
}

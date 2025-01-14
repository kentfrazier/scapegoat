package com.sksamuel.scapegoat.inspections.collections

import com.sksamuel.scapegoat.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class ListAppend extends Inspection("List append is slow", Levels.Info,
  "List append is O(n). For large lists, consider using cons (::) or another data structure such as ListBuffer or Vector and converting to a List once built.") {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private val Append = TermName("$colon$plus")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(lhs, Append), _), _) if isList(lhs) =>
            context.warn(tree.pos, self)
          case _ => continue(tree)
        }
      }
    }
  }
}
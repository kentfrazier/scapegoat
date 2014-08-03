package com.sksamuel.scapegoat.inspections.collections

import com.sksamuel.scapegoat._

import scala.tools.nsc.Global

/** @author Stephen Samuel */
class FilterIsEmpty extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def traverse(tree: Tree): Unit = {
        tree match {
          case Select(Apply(Select(_, TermName("filter")), _), TermName("isEmpty")) =>
            context.warn("filter().isEmpty instead of !exists()", tree.pos, Levels.Info,
              ".filter(x => Bool).isEmpty can be replaced with !exists(x => Bool): " + tree.toString().take(500))
          case _ => super.traverse(tree)
        }
      }
    }
  }
}
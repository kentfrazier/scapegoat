package com.sksamuel.scapegoat.inspections.unneccesary

import com.sksamuel.scapegoat._

/** @author Stephen Samuel */
class UnnecessaryIf extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def traverse(tree: Tree): Unit = {
        tree match {
          case If(cond, Literal(Constant(true)), Literal(Constant(false))) =>
            context.warn("Unncessary if condition.",
              tree.pos,
              Levels.Info,
              "If comparision is not needed. Use the condition. Eg, instead of if (a ==b) true else false, simply use a == b. : " + tree
                .toString()
                .take(500))
          case If(cond, Literal(Constant(false)), Literal(Constant(true))) =>
            context.warn("Unncessary if condition.",
              tree.pos,
              Levels.Info,
              "If comparision is not needed. Use the negated condition. Eg, instead of if (a ==b) false else true, simply use !(a == b). : " + tree
                .toString()
                .take(500))
          case _ => super.traverse(tree)
        }
      }
    }
  }
}
package com.sksamuel.scapegoat.inspections.style

import com.sksamuel.scapegoat._

/** @author Stephen Samuel */
class ParameterlessMethodReturnsUnit extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def traverse(tree: Tree): Unit = {
        tree match {
          case d@DefDef(_, name, _, vparamss, tpt, _) if tpt.tpe.toString == "Unit" && vparamss.isEmpty =>
            context.warn("Parameterless methods returns unit", tree.pos, Levels.Warning, name.toString.take(300))
          case _ => super.traverse(tree)
        }
      }
    }
  }
}

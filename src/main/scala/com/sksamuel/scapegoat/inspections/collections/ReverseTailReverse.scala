package com.sksamuel.scapegoat.inspections.collections

import com.sksamuel.scapegoat._

class ReverseTailReverse extends Inspection("reverse.tail.reverse instead of init", Levels.Info) {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Select(Select(c, TermName("reverse")), TermName("tail")), TermName("reverse")) if isTraversable(c) =>
            warn(tree)
          case Select(Apply(arrayOps0, List(Select(Apply(arrayOps1, List(Select(Apply(arrayOps2, List(col)), TermName("reverse")))), TermName("tail")))), TermName("reverse")) if (arrayOps0.toString.contains("ArrayOps"))
            && arrayOps1.toString.contains("ArrayOps")
            && arrayOps2.toString.contains("ArrayOps") =>
            warn(tree)
          case _ => continue(tree)
        }
      }

      private def warn(tree: Tree) =
        context.warn(tree.pos, self,
          ".reverse.tail.reverse can be replaced with init: " + tree.toString().take(500))
    }
  }
}
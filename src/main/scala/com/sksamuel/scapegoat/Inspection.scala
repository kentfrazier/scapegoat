package com.sksamuel.scapegoat

import scala.reflect.api.Position
import scala.tools.nsc.Global

/** @author Stephen Samuel */
trait Inspection {
  def inspector(context: InspectionContext): Inspector
}

abstract class Inspector(val context: InspectionContext) {
  def traverser: context.Traverser
}

case class InspectionContext(global: Global, feedback: Feedback) {

  def warn(text: String, pos: Position, level: Level) = feedback.warn(text, pos, level)
  def warn(text: String, pos: Position, level: Level, snippet: String) = feedback.warn(text, pos, level)

  trait Traverser extends global.Traverser with SuppressAwareTraverser

  trait SuppressAwareTraverser extends global.Traverser {

    import global._

    private def isSuppressed(symbol: Symbol) = {
      symbol != null &&
        symbol.annotations.nonEmpty &&
        symbol.annotations.head.tree.tpe.typeSymbol.fullName == classOf[SuppressWarnings].getCanonicalName &&
        (symbol.annotations.head.javaArgs.head._2.toString.toLowerCase.contains(getClass.getSimpleName.toLowerCase) ||
          symbol.annotations.head.javaArgs.head._2.toString.toLowerCase.contains("\"all\""))
    }

    abstract override def traverse(tree: Tree): Unit = {
      tree match {
        case dd@DefDef(_, _, _, _, _, _) if isSuppressed(dd.symbol) =>
        case block@Block(_, _) if isSuppressed(block.symbol) =>
        case iff@If(_, _, _) if isSuppressed(iff.symbol) =>
        case tri@Try(_, _, _) if isSuppressed(tri.symbol) =>
        case _ => super.traverse(tree)
      }
    }
  }
}





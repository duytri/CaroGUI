package uit.ai.controller

import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import scalafx.scene.layout.GridPane
import javafx.scene.{ layout => jfxsl }
import javafx.scene.canvas.Canvas

object CaroUtils {

  val SQUARE = 0
  val CIRCLE = 1

  def drawWithAnimation(gc: GraphicsContext, shape: Int, color: Color, x: Double, y: Double, width: Double, height: Double): Unit = {
    gc.setFill(color)
    if (shape == SQUARE) {
      gc.fillRect(x, y, width, height)
    } else {
      gc.fillOval(x, y, width, height)
    }
  }

  def getNodeByRowColumnIndex(row: Int, column: Int, gridPane: GridPane): Canvas = {
    var result: Canvas = null
    var childrens = gridPane.getChildren()

    for (node <- childrens.toArray if node.isInstanceOf[Canvas]) {
      val canvas = node.asInstanceOf[Canvas]
      if (jfxsl.GridPane.getRowIndex(canvas) == row && jfxsl.GridPane.getColumnIndex(canvas) == column) {
        result = canvas
      }
    }

    return result
  }
}
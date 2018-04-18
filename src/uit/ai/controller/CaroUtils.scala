package uit.ai.controller

import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext

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
}
package uit.ai.controller

import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import scalafx.scene.layout.GridPane
import javafx.scene.{ layout => jfxsl }
import javafx.scene.canvas.Canvas
import uit.ai.model.GameResult.GameResult
import com.jfoenix.controls.JFXDialog
import com.jfoenix.controls.JFXDialogLayout
import com.jfoenix.controls.JFXButton
import scalafx.scene.text.Text
import uit.ai.model.GameResult
import uit.ai.CaroGUI
import javafx.scene.layout.StackPane

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

  def showResult(result: GameResult, boardPane: GridPane, squarePlayerName: String, circlePlayerName: String): Unit = {
    result match {
      case GameResult.Square => {
        showCongrateDiaglog("Người chơi " + squarePlayerName + " đã chiến thắng!", "#c8ffcf", boardPane) //màu xanh
      }
      case GameResult.Circle => {
        showCongrateDiaglog("Người chơi " + circlePlayerName + " đã chiến thắng!", "#ffa6a6", boardPane) //màu đỏ
      }

      case GameResult.Tie => {
        showCongrateDiaglog("Hai đối thủ ngang tài ngang sức!", "#fdb2ff", boardPane) //màu tím
      }

      case GameResult.NoResult => // do nothing
    }
  }

  def showCongrateDiaglog(congratText: String, colorCode: String, boardPane: GridPane): Unit = {
    val endgameForm = new JFXDialog // dialog
    val dialogLayout = new JFXDialogLayout // thiết kế layout
    dialogLayout.setHeading(new Text("Chúc mừng")) // tiêu đề
    dialogLayout.setBody(new Text(congratText)) //nội dung

    // Hai nút lựa chọn
    val btnConfirm = new JFXButton("Đóng")
    btnConfirm.setMinWidth(100)
    btnConfirm.setMinHeight(40)
    btnConfirm.setStyle("-fx-background-color:" + colorCode) //màu nền xanh

    btnConfirm.setOnAction(e => {
      endgameForm.close // đóng dialog, không thoát nữa
      boardPane.setDisable(true)
    })

    // thêm danh sách nút vào layout
    dialogLayout.setActions(btnConfirm)

    // set nội dung của dialog là layout
    endgameForm.setContent(dialogLayout)
    endgameForm.setTransitionType(JFXDialog.DialogTransition.CENTER) // hiệu ứng di chuyển từ giữa
    endgameForm.show(CaroGUI.stage.getScene.getRoot.asInstanceOf[StackPane]) // lấy control cha là tấm nền chính của cả chương trình
  }
}
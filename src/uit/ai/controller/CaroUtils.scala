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

  def showResult(result: GameResult, boardPane: GridPane): Unit = {
    result match {
      case GameResult.Square => {
        val endgameForm = new JFXDialog // dialog
        val dialogLayout = new JFXDialogLayout // thiết kế layout
        dialogLayout.setHeading(new Text("Chúc mừng")) // tiêu đề
        dialogLayout.setBody(new Text("Người chơi với ký hiệu ô vuông đã thắng!")) //nội dung

        // Hai nút lựa chọn
        val btnConfirm = new JFXButton("Đóng")
        btnConfirm.setMinWidth(100)
        btnConfirm.setMinHeight(40)
        btnConfirm.setStyle("-fx-background-color:#c8ffcf") //màu nền xanh

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
      case GameResult.Circle => {
        val endgameForm = new JFXDialog // dialog
        val dialogLayout = new JFXDialogLayout // thiết kế layout
        dialogLayout.setHeading(new Text("Chúc mừng")) // tiêu đề
        dialogLayout.setBody(new Text("Người chơi với ký hiệu hình tròn đã thắng!")) //nội dung

        // Hai nút lựa chọn
        val btnConfirm = new JFXButton("Đóng")
        btnConfirm.setMinWidth(100)
        btnConfirm.setMinHeight(40)
        btnConfirm.setStyle("-fx-background-color:#ffa6a6") //màu nền đỏ

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

      case GameResult.Tie => {
        val endgameForm = new JFXDialog // dialog
        val dialogLayout = new JFXDialogLayout // thiết kế layout
        dialogLayout.setHeading(new Text("Chúc mừng")) // tiêu đề
        dialogLayout.setBody(new Text("Hai bên thiệt là ngang tài ngang sức!")) //nội dung

        // Hai nút lựa chọn
        val btnConfirm = new JFXButton("Đóng")
        btnConfirm.setMinWidth(100)
        btnConfirm.setMinHeight(40)
        btnConfirm.setStyle("-fx-background-color:#fdb2ff") //màu nền tím

        btnConfirm.setOnAction(e => {
          endgameForm.close // đóng dialog, không thoát nữa
          boardPane.setDisable(true)
        })

        // thêm danh sách hai nút vào layout
        dialogLayout.setActions(btnConfirm)

        // set nội dung của dialog là layout
        endgameForm.setContent(dialogLayout)
        endgameForm.setTransitionType(JFXDialog.DialogTransition.CENTER) // hiệu ứng di chuyển từ giữa
        endgameForm.show(CaroGUI.stage.getScene.getRoot.asInstanceOf[StackPane]) // lấy control cha là tấm nền chính của cả chương trình
      }

      case GameResult.NoResult => // do nothing
    }
  }
}
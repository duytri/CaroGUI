package uit.ai.controller

import scalafxml.core.macros.sfxml
import com.jfoenix.controls.JFXTextField
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXButton
import scalafx.application.Platform
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.GridPane
import javafx.fxml.FXML
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonType
import uit.ai.CaroGUI
import com.jfoenix.controls.JFXDialog
import scalafx.scene.control.Label
import com.jfoenix.controls.JFXDialogLayout
import scalafx.scene.text.Text
import scalafx.scene.input.MouseEvent
import scalafx.scene.Node
import java.util.ArrayList
import javafx.scene.layout.StackPane
import javafx.scene.{ layout => jfxsl }
import javafx.event.EventHandler
import javafx.scene.Group
import scalafx.scene.control.Control
import scalafx.scene.layout.Region
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.RowConstraints
import javafx.scene.layout.Priority

@sfxml
class CaroController(
  @FXML private var boardPane: GridPane,
  @FXML private var playerSquare: JFXTextField,
  @FXML private var playerCircle: JFXTextField,
  @FXML private var boardSize: JFXTextField,
  @FXML private val cbTwoHead: JFXCheckBox,
  @FXML private val btnStart: JFXButton,
  @FXML private val btnStop: JFXButton) {

  var isSquareTurn = true //biến cờ lượt đi

  def startGame(event: ActionEvent) {
    // set Thông báo
    if (playerSquare.getText == "")
      playerSquare.setText("Con người")
    if (playerCircle.getText == "")
      playerCircle.setText("Con người")

    // get biến
    var hasBlock = cbTwoHead.isSelected() //chặn hai đầu
    var size = boardSize.getText.toInt //kích thước bàn cờ

    // xóa bàn cờ cũ
    boardPane.getChildren.clear()
    boardPane.getRowConstraints.clear()
    boardPane.getColumnConstraints.clear()
    boardPane.setGridLinesVisible(false)

    //tạo bàn cờ mới
    for (i <- 0 until size) { //từng hàng
      val r = new RowConstraints();
      r.setVgrow(Priority.ALWAYS);
      boardPane.getRowConstraints().add(r);
    }

    for (j <- 0 until size) { //từng cột
      val c = new ColumnConstraints();
      c.setHgrow(Priority.ALWAYS);
      boardPane.getColumnConstraints().add(c);
    }

    for (i <- 0 until size) { //từng ô
      for (j <- 0 until size) {
        var label = new Label("");
        label.setPrefHeight(boardPane.getHeight)
        label.setPrefWidth(boardPane.getWidth)
        label.setMouseTransparent(true);
        GridPane.setRowIndex(label, i);
        GridPane.setColumnIndex(label, j);

        boardPane.getChildren().add(label);
      }
    }
    
    boardPane.setGridLinesVisible(true)
  }

  def playerClicked(event: MouseEvent) {
    for (node <- boardPane.getChildren.toArray if node.isInstanceOf[javafx.scene.control.Label]) {
      var label = node.asInstanceOf[javafx.scene.control.Label]
      if (label.getBoundsInParent().contains(event.getSceneX() - 5, event.getSceneY() - 5)) { // 5 is a bias
        //playerSquare.setText("Clicked: " + jfxsl.GridPane.getRowIndex(label) + ":" + jfxsl.GridPane.getColumnIndex(label))
      }
    }
  }

  def endGame(event: ActionEvent) {
    //======== Cách dùng JavaFX truyền thống =======//
    /*val confirmForm = new Alert(Alert.AlertType.Confirmation, "Bạn thật sự muốn thoát khỏi chương trình?", ButtonType.Yes, ButtonType.No)
    confirmForm.setTitle("Thoát")
    confirmForm.setHeaderText("Xác nhận")
    confirmForm.initOwner(CaroGUI.stage)
    confirmForm.showAndWait
    if (confirmForm.getResult == javafx.scene.control.ButtonType.YES)
      Platform.exit()*/

    //======== Cách dùng JFoenix truyền thống =======//
    val confirmForm = new JFXDialog // dialog
    val dialogLayout = new JFXDialogLayout // thiết kế layout
    dialogLayout.setHeading(new Text("Xác nhận")) // tiêu đề
    dialogLayout.setBody(new Text("Bạn thật sự muốn thoát khỏi chương trình?")) //nội dung

    // Hai nút lựa chọn
    val btnOK = new JFXButton("Thoát")
    btnOK.setMinWidth(100)
    btnOK.setMinHeight(40)
    btnOK.setStyle("-fx-background-color:#ffa6a6") //màu nền đỏ

    val btnCancel = new JFXButton("Không")
    btnCancel.setMinWidth(100)
    btnCancel.setMinHeight(40)
    btnCancel.setStyle("-fx-background-color:#c8ffcf") //màu nền xanh

    btnCancel.setOnAction(e => {
      confirmForm.close // đóng dialog, không thoát nữa
    })

    btnOK.setOnAction(e => {
      Platform.exit // thoát chương trình
    })

    // thêm 2 nút vào danh sách
    val arrayBtn = new ArrayList[javafx.scene.Node]()
    arrayBtn.add(btnOK)
    arrayBtn.add(btnCancel)

    // thêm danh sách hai nút vào layout
    dialogLayout.setActions(arrayBtn)

    // set nội dung của dialog là layout
    confirmForm.setContent(dialogLayout)
    confirmForm.setTransitionType(JFXDialog.DialogTransition.BOTTOM) // hiệu ứng di chuyển từ dưới lên
    confirmForm.show(CaroGUI.stage.getScene.getRoot.asInstanceOf[StackPane]) // lấy control cha là tấm nền chính của cả chương trình
  }
}
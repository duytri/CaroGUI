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
import javafx.scene.layout.StackPane
import com.jfoenix.controls.JFXDialogLayout
import scalafx.scene.text.Text
import javafx.scene.Node
import java.util.List
import java.util.ArrayList
import javafx.event.EventHandler

@sfxml
class CaroController(
  @FXML private var boardPane: GridPane,
  @FXML private var playerX: JFXTextField,
  @FXML private var playerO: JFXTextField,
  @FXML private var boardSize: JFXTextField,
  @FXML private val cbTwoHead: JFXCheckBox,
  @FXML private val btnStart: JFXButton,
  @FXML private val btnStop: JFXButton) {

  def startGame(event: ActionEvent) {
    playerX.setText("Điền vào đường dẫn của người chơi X")
    playerO.setText("Điền vào đường dẫn của người chơi O")
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
    val arrayBtn = new ArrayList[Node]()
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
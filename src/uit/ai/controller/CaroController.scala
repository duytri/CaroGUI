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

@sfxml
class CaroController(
  @FXML private var boardPane: GridPane,
  @FXML private var playerSquare: JFXTextField,
  @FXML private var playerRound: JFXTextField,
  @FXML private var boardSize: JFXTextField,
  @FXML private val cbTwoHead: JFXCheckBox,
  @FXML private val btnStart: JFXButton,
  @FXML private val btnStop: JFXButton) {

  def startGame(event: ActionEvent) {
    playerSquare.setText("Điền vào đường dẫn của người chơi Vuông")
    playerRound.setText("Điền vào đường dẫn của người chơi Tròn")

    var hasBlock = cbTwoHead.isSelected()

    var size = boardSize.getText.toInt

    for (i <- 0 until size) {
      for (j <- 0 until size) {

        var label = new Label("Label " + i + "/" + j);
        label.setMouseTransparent(true);
        GridPane.setRowIndex(label, i);
        GridPane.setColumnIndex(label, j);

        boardPane.getChildren().add(label);
        boardPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler[javafx.scene.input.MouseEvent]() {
          @Override
          def handle(e: javafx.scene.input.MouseEvent) {

            for (node <- boardPane.getChildren.toArray) {

              if (node.isInstanceOf[Label]) {
                label = node.asInstanceOf[Label]
                if (label.getBoundsInParent().contains(e.getSceneX(), e.getSceneY())) {
                  println("Node: " + node + " at " + GridPane.getRowIndex(label) + "/" + GridPane.getColumnIndex(label))
                }
              }
            }
          }
        });
      }
    }
  }

  def playerClicked(event: MouseEvent) {
    val source = event.getSource().asInstanceOf[javafx.scene.Node]
    val colIndex = jfxsl.GridPane.getColumnIndex(source)
    val rowIndex = jfxsl.GridPane.getRowIndex(source)
    playerSquare.setText("Clicked: " + rowIndex + ":" + colIndex)
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
package uit.ai.controller

import scalafxml.core.macros.sfxml
import com.jfoenix.controls.JFXTextField
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXButton
import scalafx.application.Platform
import scalafx.scene.layout.GridPane
import javafx.fxml.FXML
import scalafx.event.ActionEvent
import uit.ai.CaroGUI
import com.jfoenix.controls.JFXDialog
import com.jfoenix.controls.JFXDialogLayout
import scalafx.scene.text.Text
import scalafx.scene.input.MouseEvent
import java.util.ArrayList
import javafx.scene.layout.StackPane
import javafx.scene.{ layout => jfxsl }
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.RowConstraints
import javafx.scene.layout.Priority
import scalafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import uit.ai.model.CaroBoard
import ai.dev.team.Square
import ai.dev.team.Circle
import uit.ai.model.GameResult
import uit.ai.model.AILoader
import ai.dev.team.Player

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
  var isSquareHuman = true; // biến cờ con người
  var isCircleHuman = true; // biến cờ con người
  var caroBoard: CaroBoard = null
  var aiSquare: Player = null
  var aiCircle: Player = null

  def startGame(event: ActionEvent) {
    boardPane.setDisable(false)

    // get biến
    var hasBlock = cbTwoHead.isSelected() //chặn hai đầu
    var size = boardSize.getText.toInt //kích thước bàn cờ

    // tạo biến bàn cờ
    caroBoard = new CaroBoard(size, size, hasBlock)
    isSquareTurn = true; // vuông luôn luôn đi trước

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
        /*var label = new Label("");
        label.setPrefHeight(boardPane.getHeight)
        label.setPrefWidth(boardPane.getWidth)
        label.setMouseTransparent(true)*/
        var canvas = new Canvas(boardPane.getWidth / size, boardPane.getHeight / size)
        GridPane.setRowIndex(canvas, i)
        GridPane.setColumnIndex(canvas, j)

        boardPane.getChildren().add(canvas);
      }
    }
    boardPane.setGridLinesVisible(true)

    // set Thông báo
    if (playerSquare.getText == "" || playerSquare.getText == "Con người") {
      isSquareHuman = true
      playerSquare.setText("Con người")
    } else isSquareHuman = false
    if (playerCircle.getText == "" || playerCircle.getText == "Con người") {
      isCircleHuman = true
      playerCircle.setText("Con người")
    } else isCircleHuman = false

    // nếu có AI đánh cờ, thực hiện việc đánh
    // ở đây chỉ giải quyết 2 trường hợp, AI vss AI và AI đi trước người đi sau
    if (!isSquareHuman) {
      if (isCircleHuman) { // AI đi trước người đi sau
        aiSquare = AILoader.load(playerSquare.getText)
        val move = aiSquare.nextMove(caroBoard.getBoard, Square)
        caroBoard.update(move._1, move._2, Square) // cập nhật lại biến bàn cờ

        //cập nhật hiển thị bàn cờ
        val canvas = CaroUtils.getNodeByRowColumnIndex(move._1, move._2, boardPane)
        val gc = canvas.getGraphicsContext2D
        CaroUtils.drawWithAnimation(gc, CaroUtils.SQUARE, Color.GREEN, 7, 7, canvas.getWidth - 14, canvas.getHeight - 14)
        isSquareTurn = false

      } else { // AI vs AI

      }
    }
  }

  def playerClicked(event: MouseEvent) {
    for (node <- boardPane.getChildren.toArray if node.isInstanceOf[javafx.scene.canvas.Canvas]) {
      var canvas = node.asInstanceOf[javafx.scene.canvas.Canvas]
      //for (node <- boardPane.getChildren.toArray if node.isInstanceOf[javafx.scene.control.Label]) {
      //var label = node.asInstanceOf[javafx.scene.control.Label]
      if (canvas.getBoundsInParent().contains(event.getSceneX() - 5, event.getSceneY() - 5)) { // 5 is a bias
        //playerSquare.setText("Clicked: " + jfxsl.GridPane.getRowIndex(canvas) + ":" + jfxsl.GridPane.getColumnIndex(canvas))
        val row = jfxsl.GridPane.getRowIndex(canvas)
        val col = jfxsl.GridPane.getColumnIndex(canvas)
        val gc = canvas.getGraphicsContext2D

        if (caroBoard.validMove(row, col))
          if (isSquareTurn) {
            //gc.setFill(Color.GREEN)
            //gc.fillRect(7, 7, canvas.getWidth - 14, canvas.getHeight - 14)
            caroBoard.update(row, col, Square)
            CaroUtils.drawWithAnimation(gc, CaroUtils.SQUARE, Color.GREEN, 7, 7, canvas.getWidth - 14, canvas.getHeight - 14)
            isSquareTurn = false
          } else {
            //gc.setFill(Color.RED)
            //gc.fillOval(7, 7, canvas.getWidth - 14, canvas.getHeight - 14)
            caroBoard.update(row, col, Circle)
            CaroUtils.drawWithAnimation(gc, CaroUtils.CIRCLE, Color.RED, 7, 7, canvas.getWidth - 14, canvas.getHeight - 14)
            isSquareTurn = true
          }
      }
    }

    // sau khi người đi, nếu tiếp theo là AI
    // 2 trường hợp: Người đi trước AI đi sau và AI đi trước người đi sau (nước đi tiếp theo)
    if (isSquareHuman) { // người đi trước AI đi sau
      aiCircle = AILoader.load(playerCircle.getText)
      val move = aiCircle.nextMove(caroBoard.getBoard, Circle)
      caroBoard.update(move._1, move._2, Circle) // cập nhật lại biến bàn cờ

      //cập nhật hiển thị bàn cờ
      val canvas = CaroUtils.getNodeByRowColumnIndex(move._1, move._2, boardPane)
      val gc = canvas.getGraphicsContext2D
      CaroUtils.drawWithAnimation(gc, CaroUtils.CIRCLE, Color.RED, 7, 7, canvas.getWidth - 14, canvas.getHeight - 14)
      isSquareTurn = true
      
    } else if (isCircleHuman) { // AI đi trước người đi sau (nước đi tiếp theo)
      val move = aiSquare.nextMove(caroBoard.getBoard, Square)
      caroBoard.update(move._1, move._2, Square) // cập nhật lại biến bàn cờ

      //cập nhật hiển thị bàn cờ
      val canvas = CaroUtils.getNodeByRowColumnIndex(move._1, move._2, boardPane)
      val gc = canvas.getGraphicsContext2D
      CaroUtils.drawWithAnimation(gc, CaroUtils.SQUARE, Color.GREEN, 7, 7, canvas.getWidth - 14, canvas.getHeight - 14)
      isSquareTurn = false
    }

    caroBoard.determineWinner match {
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
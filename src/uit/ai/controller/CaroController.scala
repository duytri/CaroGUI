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

case class CaroDependency(initialPath: String)

@sfxml
class CaroController(
  boardPane: GridPane,
  @FXML private var playerX: JFXTextField,
  @FXML private var playerO: JFXTextField,
  @FXML private var boardSize: JFXTextField,
  @FXML private val cbTwoHead: JFXCheckBox,
  @FXML private val btnStart: JFXButton,
  @FXML private val btnStop: JFXButton,
  caroDep: CaroDependency) {
  
  println(s"caroDep is $caroDep")
  
  playerO.setText(caroDep.initialPath)

  def startGame(event: ActionEvent) {
    println("AAAA")
    playerX.setText("aaa")
  }

  def endGame(event: ActionEvent) {
    Platform.exit()
  }
}
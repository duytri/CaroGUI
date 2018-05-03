package main.scala.uit.ai

import java.io.IOException
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.FXMLView
import scalafxml.core.NoDependencyResolver
import scalafx.scene.image.Image
import scalafx.scene.layout.StackPane

object CaroGUI extends JFXApp {

  // đọc file thiết kế giao diện
  val resource = getClass.getResource("/main/scala/uit/ai/view/CaroGUI.fxml")
  if (resource == null) {
    throw new IOException("Cannot load resource: CaroGUI.fxml")
  }

  // dựng giao diện lại dựa trên file thiết kế đã đọc
  val root = FXMLView(resource, NoDependencyResolver)
  val stackPane = new StackPane() // StackPane bao ngoài cùng để các dialog sử dụng làm control tham chiếu
  stackPane.getChildren.add(root)

  stage = new PrimaryStage {
    title = "CHUONG TRINH THI DAU CO CARO"
    icons += new Image("main/scala/uit/ai/view/uit_logo.png")
    scene = new Scene(stackPane)
  }
}
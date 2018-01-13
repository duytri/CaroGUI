package uit.ai

import java.io.IOException
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.FXMLView
import scalafxml.core.NoDependencyResolver
import scalafx.scene.image.Image

object CaroGUI extends JFXApp {

  val resource = getClass.getResource("/uit/ai/view/CaroGUI.fxml")
  if (resource == null) {
    throw new IOException("Cannot load resource: CaroGUI.fxml")
  }

  val root = FXMLView(resource, NoDependencyResolver)

  stage = new PrimaryStage {
    title = "CHUONG TRINH THI DAU CO CARO"
    icons += new Image("/uit/ai/view/uit_logo.png")
    scene = new Scene(root)
  }
}
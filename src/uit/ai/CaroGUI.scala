package uit.ai

import java.io.IOException
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{ DependenciesByType, FXMLView }
import scala.reflect.runtime.universe.typeOf
import uit.ai.controller.CaroDependency

object CaroGUI extends JFXApp {

  val resource = getClass.getResource("/uit/ai/view/CaroGUI.fxml")
  if (resource == null) {
    throw new IOException("Cannot load resource: CaroGUI.fxml")
  }

  val root = FXMLView(resource, new DependenciesByType(Map(
      typeOf[CaroDependency] -> new CaroDependency("Init string"))))

  stage = new PrimaryStage() {
    title = "CHUONG TRINH THI DAU CO CARO"
    fullScreen_=(true)
    scene = new Scene(root)
  }
}
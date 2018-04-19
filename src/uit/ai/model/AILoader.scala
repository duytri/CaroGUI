package uit.ai.model

import java.net.URLClassLoader
import java.io.File
import ai.dev.team.Player

object AILoader {
  def load(pathToJAR: String): Player = {
    var classLoader = new URLClassLoader(Array(new File(pathToJAR).toURI.toURL))
    var clazz = classLoader.loadClass("ai.dev.team.CaroPlayer")
    return clazz.newInstance().asInstanceOf[Player]
  }
}
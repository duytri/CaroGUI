package main.scala.uit.ai.model

import java.net.URLClassLoader
import java.io.File

object AILoader {
  def load(pathToJAR: String): Player = {
    var classLoader = new URLClassLoader(Array(new File(pathToJAR).toURI.toURL))
    var clazz = classLoader.loadClass("uit.ai.model.CaroPlayer")
    return clazz.newInstance().asInstanceOf[Player]
  }
}
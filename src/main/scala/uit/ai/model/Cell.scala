package main.scala.uit.ai.model

sealed abstract class Cell {
  val value: Byte = 0
}
case object Square extends Cell {
  override val value: Byte = 1
}
case object Circle extends Cell {
  override val value: Byte = -1
}
case object Blank extends Cell {
  override val value: Byte = 0
}
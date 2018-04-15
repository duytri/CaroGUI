package uit.ai.model

sealed abstract class Cell
case object Square extends Cell
case object Round extends Cell
case object Blank extends Cell {
    override def toString = " "
}
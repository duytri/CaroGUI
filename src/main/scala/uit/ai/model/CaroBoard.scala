package main.scala.uit.ai.model

import scala.collection.mutable.ListBuffer

class CaroBoard(val board: Array[Array[Cell]], val hasBlock: Boolean) {

  def this(rows: Int, cols: Int, hasBlock: Boolean) = this(Array.fill(rows, cols)(Blank): Array[Array[Cell]], hasBlock)
  def this(size: Int, hasBlock: Boolean) = this(size, size, hasBlock)

  val rowCount = board.length
  val columnCount = if (board.isEmpty) 0 else board(0).length

  val numInARowNeeded: Int = {
    // so nuoc di thang hang can de chien thang
    // numbers chosen rather arbitrarily by me. I looked at this: http://en.wikipedia.org/wiki/M,n,k-game
    // and tried to pick numbers that more or less made sense
    if (rowCount <= 3 || columnCount <= 3) {
      // tic tac toe or bizarre tiny variants
      scala.math.min(rowCount, columnCount)
    } else if (rowCount <= 5) {
      // connect 4, sort of
      4
    } else {
      // gomoku
      5
    }
  }

  def getBoard = board

  def getCandidates(): List[(Int, Int)] = {
    val candidates = new ListBuffer[(Int, Int)]
    val nonAvailableElems = new ListBuffer[(Int, Int)]
    for (r <- 0 until rowCount)
      for (c <- 0 until columnCount)
        if (board(r)(c) != Blank)
          nonAvailableElems.append((r, c))
    for (e <- nonAvailableElems) {
      if (e._1 + 1 < rowCount && !nonAvailableElems.contains((e._1 + 1, e._2)) && !candidates.contains((e._1 + 1, e._2))) //East
        candidates.append((e._1 + 1, e._2))
      if (e._1 - 1 >= 0 && !nonAvailableElems.contains((e._1 - 1, e._2)) && !candidates.contains((e._1 - 1, e._2))) //West
        candidates.append((e._1 - 1, e._2))
      if (e._2 - 1 >= 0 && !nonAvailableElems.contains((e._1, e._2 - 1)) && !candidates.contains((e._1, e._2 - 1))) //North
        candidates.append((e._1, e._2 - 1))
      if (e._2 + 1 < columnCount && !nonAvailableElems.contains((e._1, e._2 + 1)) && !candidates.contains((e._1, e._2 + 1))) //South
        candidates.append((e._1, e._2 + 1))
      if (e._1 + 1 < rowCount && e._2 - 1 >= 0 && !nonAvailableElems.contains((e._1 + 1, e._2 - 1)) && !candidates.contains((e._1 + 1, e._2 - 1))) //East-North
        candidates.append((e._1 + 1, e._2 - 1))
      if (e._1 + 1 < rowCount && e._2 + 1 < columnCount && !nonAvailableElems.contains((e._1 + 1, e._2 + 1)) && !candidates.contains((e._1 + 1, e._2 + 1))) //East-South
        candidates.append((e._1 + 1, e._2 + 1))
      if (e._1 - 1 >= 0 && e._2 + 1 < columnCount && !nonAvailableElems.contains((e._1 - 1, e._2 + 1)) && !candidates.contains((e._1 - 1, e._2 + 1))) //West-South
        candidates.append((e._1 - 1, e._2 + 1))
      if (e._1 - 1 >= 0 && e._2 - 1 >= 0 && !nonAvailableElems.contains((e._1 - 1, e._2 - 1)) && !candidates.contains((e._1 - 1, e._2 - 1))) //West-North
        candidates.append((e._1 - 1, e._2 - 1))
    }
    println("Candidate size: " + candidates.size)
    candidates.toList
  }

  override def clone(): CaroBoard = {
    val cloneBoard = new CaroBoard(rowCount, columnCount, hasBlock)
    for (r <- 0 until rowCount)
      for (c <- 0 until columnCount)
        cloneBoard.board(r)(c) = board(r)(c)
    cloneBoard
  }

  // get board as collection of rows
  def rows: Seq[Array[Cell]] = {
    for (r <- 0 until rowCount)
      yield board(r)
  }

  // get board as collection of columns
  def columns: Seq[Array[Cell]] = {
    for (c <- 0 until columnCount) yield (
      for (r <- (0 until rowCount))
        yield board(r)(c)).toArray
  }

  //get board as collection of diagonals from left to right
  def diagonalsLTR: Seq[Array[Cell]] = {
    for (offset <- (1 - columnCount) until columnCount) yield (
      for (row <- 0 until rowCount if offset + row < columnCount && offset + row > -1)
        yield (board(row)(row + offset))).toArray
  }

  //get board as collection of diagonals from right to left
  def diagonalsRTL: Seq[Array[Cell]] = {
    for (offset <- 0 until rowCount + rowCount - 1) yield (
      for (col <- 0 until columnCount if offset - col < rowCount && offset - col > -1)
        yield (board(offset - col)(col))).toArray
  }

  // find the winner
  def determineWinner: GameResult.Value = {
    val winnerText = "Player %s won!"
    val checkForWinner = { array: Array[Cell] =>
      CaroBoard.nInARow(numInARowNeeded, array, hasBlock) match {
        case Some(player) => return player match { // non-local return!
          case Square => GameResult.Square
          case Circle => GameResult.Circle
          case other => throw new Exception("Error, '" + other + "' is not a player.")
        }
        case None => // do nothing
      }
    }

    rows foreach checkForWinner
    columns foreach checkForWinner
    diagonalsLTR foreach checkForWinner
    diagonalsRTL foreach checkForWinner

    if (board.map(row => row.contains(Blank)).contains(true)) {
      return GameResult.NoResult
    }

    return GameResult.Tie
  }

  // neu toa do chua vuot ra ngoai ban co va vi tri do dang BLANK
  def validMove(row: Int, col: Int): Boolean = {
    return row < rowCount && row >= 0 && col < columnCount && col >= 0 && board(row)(col) == Blank
  }

  // cap nhat lai nuoc di nguoi choi Cell danh
  def update(row: Int, col: Int, Cell: Cell) = {
    board(row)(col) = Cell
  }

  // tao ban sao va cap nhat lai nuoc di nguoi choi Cell danh
  def updateAndGet(row: Int, col: Int, Cell: Cell): CaroBoard = {
    val cloneBoard = this.clone()
    cloneBoard.update(row, col, Cell)
    cloneBoard
  }

  //
  def getBoardAsByteArray(): Array[Array[Byte]] = {
    (for (r <- 0 until rowCount) yield (
      for (c <- 0 until columnCount)
        yield board(r)(c).value).toArray).toArray
  }
}

object CaroBoard {

  def threeInARow(list: List[Cell]): Option[Cell] = list match {
    case Nil => None
    case x :: y :: z :: tail if x == y && y == z && z != Blank => Some(z)
    case _ :: tail => threeInARow(tail)
  }

  def nInARow(n: Int, array: Array[Cell], hasBlock: Boolean): Option[Cell] = {
    for (i <- 0 until array.length - (n - 1)) {
      var allTrue = true;
      for (j <- i + 1 until i + n) {
        allTrue &= array(j - 1) == array(j)
      }
      if (allTrue && array(i) != Blank) {
        if (hasBlock) {
          if (i > 0 && i + n < array.length && array(i - 1) != array(i) && array(i + n) != array(i) && array(i - 1) != Blank && array(i + n) != Blank) // dieu kien chan hai dau thi khong thang
            return None
          else
            return Some(array(i))
        } else
          return Some(array(i))
      }
    }

    return None
  }

}
package uit.ai.model

trait Player {
  /**
   * Hàm nhận vào bàn cờ và trả về nước đi tiếp theo
   * @param board Ma trận hai chiều, biểu diễn bàn cờ. Cell có ba thể hiện là: Round, Circle và Blank
   * @param playerSide AI đang cầm quân nào?
   * @return Tọa độ x, y của nước đi tiếp theo
   */
  def nextMove(board: Array[Array[Cell]], playerSide: Cell): (Int, Int)
  
  /**
   * Hàm trả về tên của người chơi
   * @return Tên người chơi
   */
  def getName: String
}
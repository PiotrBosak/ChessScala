package lib

import java.util.UUID
import lib.logic.board.Position.apply
import lib.logic.board.Position
import lib.user.UserIdParam

object game {
 
  final case class MakeMove(from: Position, to: Position)
  final case class GameId(value: UUID)

  final case class GameData(userId: UserIdParam, gameId: GameId)
}

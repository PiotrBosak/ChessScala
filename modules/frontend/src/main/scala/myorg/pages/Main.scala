package myorg.pages

import cats.effect.IO
import myorg.utils.HtmlUtils.*
import lib.logic.pieces.Piece.*
import lib.logic.Color
import lib.logic.Color.*
import lib.logic.board.Rank.*
import lib.logic.board.File.*
import tyrian.*
import myorg.Route
import tyrian.Html.*
import lib.logic.board.MoveType.*
import myorg.pages.Login as LoginPage
import myorg.pages.Register as RegisterPage
import myorg.pages.ChessBoard as ChessBoardPage

import scala.scalajs.js.annotation.*
import lib.logic.game.FullGame.Turn
import lib.logic.board.{Board, File, MoveType, Position, Rank, Tile}
import lib.logic.pieces.Piece
import myorg.algebras.UserAlg.*
import myorg.pages.Main.Msg.{GotChessBoardMessage, GotLoginMessage, GotRegisterMessage}
import myorg.pages.Main.{Model, Msg}
import tyrian.Navigation.Result

@JSExportTopLevel("TyrianApp")
object Main extends TyrianApp[Msg, Model] {

  val address = "localhost:8080"

  enum Model {
    case Login(model: LoginPage.Model)
    case Register(model: RegisterPage.Model)
    case ChessBoard(model: ChessBoardPage.Model)
  }

  enum Msg {
    case NoOp
    case SwitchPage(page: Page)
    case GotLoginMessage(msg: Login.Msg)
    case GotRegisterMessage(msg: Register.Msg)
    case GotChessBoardMessage(msg: ChessBoard.Msg)
  }

  enum Page {
    case Login
    case Register
  }
  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) = msg => {
    println("XXXXXXXXXXXXXXXXXXXXXX")
    println((model, msg))
    (model, msg) match {
      case (Model.Login(model), GotLoginMessage(msg)) =>
        Login.update(msg, model).updateWith(Model.Login.apply, GotLoginMessage.apply)
      case (Model.Register(model), GotRegisterMessage(msg)) =>
        Register.update(msg, model).updateWith(Model.Register.apply, GotRegisterMessage.apply)
      case (Model.ChessBoard(model), GotChessBoardMessage(msg)) =>
        ChessBoard.update(msg, model).updateWith(Model.ChessBoard.apply, GotChessBoardMessage.apply)
      case (_, Msg.SwitchPage(Page.Register)) =>
        (Model.Register(RegisterPage.init()), Cmd.None)
      case (_, Msg.SwitchPage(Page.Login)) =>
        (Model.Login(LoginPage.init()), Cmd.None)
      case (_, _) => (model, Cmd.None)
    }
  }

  extension [SubModel, SubMsg] (tuple: (SubModel, Cmd[IO, SubMsg])) {
    private def updateWith(toMainModel: SubModel => Model, toMainMsg: SubMsg => Msg): (Model, Cmd[IO, Msg]) = {
      (toMainModel(tuple._1), tuple._2.map(toMainMsg))
    }
  }

  def init(flags: Map[String, String]): (Model, Cmd[cats.effect.IO, Msg]) =
    (Model.Login(LoginPage.init()), Cmd.None)
  override def subscriptions(model: Model): Sub[cats.effect.IO, Msg] =
    Navigation.onLocationHashChange {
      case Result.HashChange(_, _, _, newFragment) =>
        println("got into subscriptions")
        Route.fromString(newFragment) match {
          case Some(Route.Register) => Msg.SwitchPage(Page.Register)
          case Some(Route.Login) => Msg.SwitchPage(Page.Login)
          case _ => Msg.NoOp
        }
    }

  def view(model: Model): Html[Msg] = {
    model match {
      case Model.Login(model) => LoginPage.view(model).map(Msg.GotLoginMessage.apply)
      case Model.Register(model)  => RegisterPage.view(model).map(Msg.GotRegisterMessage.apply)
      case Model.ChessBoard(model) => ChessBoardPage.view(model).map(Msg.GotChessBoardMessage.apply)
    }

  }

}

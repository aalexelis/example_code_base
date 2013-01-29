package code.comet

import net.liftweb.util.Helpers._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import code.lib.{Player, LeaderBoard}
import xml.NodeSeq


/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/26/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
object TheLeaderboard extends SessionVar(new LeaderBoard())

object Selected extends SessionVar(ValueCell[Box[Player]](Empty))

class CometLeaderBoard extends CometActor with Loggable {

  private var leaderboard = TheLeaderboard.get
  private val screendata = leaderboard.players.lift(Selected.get: ValueCell[Box[Player]])((_,_))

  def setSelected(p: Player) = Selected.get.set(Full(p))

  def render = {
    WiringUI.apply(screendata)( (sd) => {
      val (ps,s) = sd

      ".leaderboard" #> ps.map( p => {
        ".player [onclick]" #> SHtml.ajaxInvoke(() => {setSelected(p);Noop}) &
          ".player [class+]" #> (s match {
            case Full(s1:Player) if (s1.name == p.name) => "selected"
            case _ => ""
          }) &
          ".name *" #> p.name &
          ".score *" #> p.score
      }) &
      ".details" #> ( s match {
        case Full(p: Player) => {
          "*" #> (
            ".name *" #> p.name &
            ".inc [onclick]" #> SHtml.ajaxInvoke(() => leaderboard.mutateLine(p.name){np => Player(np.name, np.score+5)})
          )
        }
        case _ => "*" #> NodeSeq.Empty
      }) &
      ".none" #> ( s match {
        case Full(p) => "*" #> NodeSeq.Empty
        case _ => "*" #> PassThru
      })

    })
  }

}

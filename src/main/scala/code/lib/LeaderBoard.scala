package code.lib

import net.liftweb.util.{FuncCell2, ValueCell}
import net.liftweb.common.{Full, Empty, Box}
import code.model.Players

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/26/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
class LeaderBoard {
  val players = ValueCell[List[Player]](initdata)

  def mutateLine(name: String)(f: Player => Player) {
    val all = players.get
    val head = all.filter(_.name == name).map(f)
    head.map(Players.update(_))
    val rest = all.filter(_.name != name)
    players.set((head ::: rest).sortWith((a,b) =>((a.score > b.score) || ((a.score == b.score) && (a.name > b.name)))))
  }

  def initdata = {
    Players.init()
    Players.loadall
  }

}

<<<<<<< HEAD
case class Player(name: String, score: Int){

}
=======
case class Player(name: String, score: Int)
>>>>>>> brush up

object Player{
  def apply(name:String) = new Player(name, (scala.math.floor(scala.math.random*10)*5).toInt)
}




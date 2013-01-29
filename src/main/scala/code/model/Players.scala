package code.model

import code.lib.Player
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.{JField, JValue, JObject}
import net.liftweb.mongodb.{JObjectParser, DefaultMongoIdentifier, MongoDB}
import com.mongodb._
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.mongodb.record.{MongoMetaRecord, MongoRecord}
import net.liftweb.record.field.{IntField, StringField}
import net.liftweb.http.js.JE.JsObj
import net.liftweb.json.DefaultFormats
import scala.collection.JavaConversions._
import net.liftweb.common.Loggable
import net.liftweb.util.Props
import net.liftweb.json.JsonAST.JField
import net.liftweb.json.JsonAST.JObject

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/29/13
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */

object MongoConfig {
  def init: Unit = {
    val srvr = new ServerAddress(
      Props.get("mongo.host", "127.0.0.1"),
      Props.getInt("mongo.port", 27017)
    )
    MongoDB.defineDb(DefaultMongoIdentifier, new Mongo(srvr), "myapp")
  }
}

object Players extends Loggable {

  val players: List[Player] = List(
    Player("Claude Shannon"),
    Player("Carl Friedrich Gauss"),
    Player("Grace Hopper"),
    Player("Marie Curie"),
    Player("Nikola Tesla"),
    Player("Ada Lovelace")
  )

  implicit val formats = DefaultFormats.lossless

  val jobj = players.map(p => decompose(p).asInstanceOf[JObject] )

  def init(){
    MongoDB.use(DefaultMongoIdentifier) { db =>
      val col1 = db.getCollection("leaderboard")
      col1.drop
      if (col1.find.count == 0) jobj.foreach(o => col1.save(JObjectParser.parse(o)))
    }
  }

  def loadall = MongoDB.use(DefaultMongoIdentifier) { db =>
    val col1 = db.getCollection("leaderboard")
    val dbcursor = col1.find().sort(JObjectParser.parse(JObject(List(JField("score", -1), JField("name", 1)))))

    dbcursor.toArray.toList.map(dbo => extract[Player](JObjectParser.serialize(dbo)))
  }

  def update(p: Player) = MongoDB.use(DefaultMongoIdentifier) { db =>
    val col1 = db.getCollection("leaderboard")
    col1.update(JObjectParser.parse(JObject(List(JField("name", p.name)))),JObjectParser.parse(JObject(List(JField("score", p.score)))))
  }

}
<<<<<<< HEAD

/*
class Players private() extends MongoRecord[Players] with ObjectIdPk[Players] {
  def meta = Players

  object name extends StringField(this, 12)
  object score extends IntField(this)
}

object Players extends Players with MongoMetaRecord[Players] {

}
*/
=======
>>>>>>> brush up

package code.model

import net.liftweb.mapper._
import net.liftweb.json.JsonAST.{JDouble, JInt, JValue}
import net.liftweb.common.{Loggable, Box}
import net.liftweb.util.Helpers
import net.liftweb.json.{MappingException, TypeInfo, Formats, Serializer}
import net.liftweb.http.js
import net.liftweb.http.js.JE.{JsTrue, JsObj, JsArray}
import net.liftweb.{util, json}
import json._
import net.liftweb.http.js.JE.{JsObj, JsTrue, JsArray}

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 3/4/13
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
object Task extends Task with LongKeyedMetaMapper[Task] with Loggable {
  override def dbTableName = "tasks"

}

class Task extends LongKeyedMapper[Task] with IdPK {
  def getSingleton = Task

  object title extends MappedString(this, 150)
  object isDone extends MappedBoolean(this)

}
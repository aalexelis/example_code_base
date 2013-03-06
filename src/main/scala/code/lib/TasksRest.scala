package code.lib

import net.liftweb.http.rest.RestHelper
import net.liftweb.http._
import net.liftweb.{util, json}
import json._
import util._
import code.model.Task
import net.liftweb.common.{Loggable, Box}
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Extraction._
import net.liftweb.http.InternalServerErrorResponse
import net.liftweb.common.Full


/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 3/4/13
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
object TasksRest extends RestHelper with Loggable {
  serve {
    case "tasks" :: Nil Get _ =>
      implicit val formats = DefaultFormats.lossless
      val jstasks = JsTasks(JsTasks.loadTasks())
      JsonResponse(decompose(jstasks))

    case "tasks" :: Nil JsonPost JsTasks(jstasks) -> _ =>
      jstasks.delAndSave()
      JsonResponse(JString("Saved"))
  }

}

case class JsTasks(val tasks: List[JsTask]) {
  def save() {
    tasks.foreach(t => new Task().title(t.title).isDone(t.isDone).save())
  }
  def delAndSave() {
    Task.findAll().foreach(Task.delete_!(_))
    save()
  }
  def this() = this(JsTasks.loadTasks())
}

object JsTasks {
  private implicit val formats = DefaultFormats.lossless
  def unapply(in: JValue): Option[JsTasks] = Helpers.tryo {in.extract[JsTasks]}
  def loadTasks() = Task.findAll().map(t => JsTask(t.title.is,t.isDone.is))
}

case class JsTask(val title: String, val isDone: Boolean){
  def this(title: String) = this(title, false)
}

object JsTask extends Loggable {
  private implicit val formats = net.liftweb.json.DefaultFormats
  def apply(in: JValue): Box[JsTask] = Helpers.tryo {in.extract[JsTask]}
}

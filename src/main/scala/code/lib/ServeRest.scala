package code.lib

import net.liftweb.http.rest.RestHelper
import net.liftweb.common.Loggable
import net.liftweb.json.DefaultFormats
import net.liftweb.http.{SessionVar, S, JsonResponse, SHtml}
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonAST._
import net.liftweb.util.ValueCell
import net.liftweb.util.Helpers._
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST.JString
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds.Alert

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 4/22/13
 * Time: 5:49 AM
 * To change this template use File | Settings | File Templates.
 */
object ServeRest extends RestHelper with Loggable {
  serve {
    case "bind-submit" :: Nil Get _ =>
      implicit val formats = DefaultFormats
      TheLines.get.setSubmit(process)
        val cbsubmit = TheLines.get.submit.get
        JsonResponse(decompose(cbsubmit))

    case "bind-newline" :: Nil Get _ =>
      logger.info("Params: "+S.request.map(_.paramNames))
      import net.liftweb.json.JsonDSL._
      val l = TheLines.get.newLine
      logger.info("TheLines: "+ TheLines.get.lines)
      val json = S.request.map(_.paramNames.map(p => ((p -> mani(l,p)): JObject)).foldRight[JObject](("dummy" -> JNothing))(_ ~ _)) openOr (("dummy" -> JNothing): JObject)
      JsonResponse(json)
  }

  def mani(l: Line, p:String):JString = p match {
    case "guid" => JString(l.guid)
    case s => JString(TheLines.get.mutateField(l)(s))
  }

  def process() = {
    val s = TheLines.get.lines.get.toString
    Alert("Line contents: "+s)//JsCmds.Noop
  }

}

case class Submit(val cbsubmit: String)

case class Line(val guid: String, val product: String, val unitprice: Int, val qty: Int)

class Lines {
  val submit = ValueCell[Submit](Submit(null))
  val lines = ValueCell[List[Line]](List())

  def setSubmit(f:() => JsCmd){
    submit.set(
      Submit( SHtml.ajaxCall(JsRaw("this.value"),(s:String) => f()).toJsCmd )
    )
  }

  def newLine = {
    val all = lines.get
    val nl = Line(nextFuncName,"",0,0)
    lines.set(nl :: all)
    nl
  }

  def mutateLine(guid: String)(f: Line => Line) {
    val all = lines.get
    val head = all.filter(_.guid == guid).map(f)
    val tail = all.filter(_.guid != guid)
    lines.set(head ::: tail)
  }

  def mutateField(l: Line)(field: String) = field match {
    case "product" => SHtml.ajaxCall(JsRaw("this.value"),
      (s:String) => mutateLine(l.guid){ml => new Line(ml.guid,s,ml.unitprice,ml.qty)}).toJsCmd
    case "unitprice_su" => SHtml.ajaxCall(JsRaw("this.value"),
      (s:String) => mutateLine(l.guid){ml => new Line(ml.guid,ml.product,s.toInt,ml.qty)}).toJsCmd
    case "qnty" => SHtml.ajaxCall(JsRaw("this.value"),
      (s:String) => mutateLine(l.guid){ml => new Line(ml.guid,ml.product,ml.unitprice,s.toInt)}).toJsCmd
    case _ => null
  }

}

object TheLines extends SessionVar(new Lines())


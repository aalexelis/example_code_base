package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.util.{ValueCell, PassThru}
import net.liftweb.http.SHtml
import net.liftweb.common.Loggable
import net.liftweb.http.js.JsCmds._

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/14/13
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
object TryMemoize extends Loggable {

  private class Line(val guid: String, val content: String)
  private def newLine = {
    logger.info("newLine!!!")
    val guid = nextFuncName
    new Line(guid,guid)
  }

  private val lines = ValueCell[List[Line]](List(newLine))
  private def appendLine: Line = {
    logger.info("appendLine!!!")
    val ret = newLine
    lines.set(ret :: lines.get)
    ret
  }
  private def mutateLine(guid: String)(f: Line => Line) {
    val all = lines.get
    val head = all.filter(_.guid == guid).map(f)
    val rest = all.filter(_.guid != guid)
    lines.set(head ::: rest)
  }
  private def removeLine(guid: String) {
    logger.info("removeLine!!!")
    val all = lines.get
    val rest = all.filter(_.guid != guid)
    lines.set(rest)
  }

  def render =
    "@Llistcontainer" #> SHtml.idMemoize( Llines =>
      "@Llines *" #> lines.map(l =>
        "li [id]" #> l.guid &
        "li *" #> {
          ".Lcontent *" #> l.content &
          ".Lremove [onclick]" #> SHtml.ajaxInvoke(() => {
            removeLine(l.guid)
            Llines.setHtml
          })
        }
      ) &
      "@Ladd [onclick]" #> SHtml.ajaxInvoke(() => {
        appendLine
        Llines.setHtml
      })
    )
}

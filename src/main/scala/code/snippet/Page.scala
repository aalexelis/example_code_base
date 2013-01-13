package code.snippet

import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._
import net.liftweb.http.js.jquery.JqJsCmds.Unblock

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 6/28/12
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */

case class PageId(id: String)

class Page(pi: PageId) {

  val page_db = Map( 1L -> "One", 2L ->"Two", 3L -> "Three")

  val pid = asLong(pi.id) openOr 0L

  def render = page_db.get(pid).map(renderPage(_) ) getOrElse("*" #> "No such page id.")

  def renderPage(pc: String) =
    "#page_content *" #> pc &
    ".close [onclick]" #> SHtml.ajaxInvoke( () => Unblock)


}

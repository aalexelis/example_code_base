package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, RequestVar, S}
import net.liftweb.http.js.jquery.JqJsCmds.Unblock

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 6/22/12
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */


case class PageId(id: String)

class Page(pi: PageId) {

  val page_db = Map( 1L -> "One", 2L ->"Two", 3L -> "Three")

  val page = page_db.get(asLong(pi.id) openOr 0L)

  def render = page.map(renderPage(_) ) getOrElse("*" #> "No such page id.")

  def renderPage(pc: String) = "#page_content *" #> pc

}


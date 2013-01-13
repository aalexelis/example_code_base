package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{S, SHtml}
import net.liftweb.http.js.jquery.JqJsCmds.ModalDialog
import net.liftweb.common.Loggable
import net.liftweb.http.js.JsCmds.Alert

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 6/22/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */

object MakeLinks extends Loggable {

  def md(pid: Long) = S.runTemplate(
    "page" :: Nil,
    "page" -> new Page(PageId(pid.toString)).render).map(ns => ModalDialog(ns)) openOr Alert("Error")

  def render1 = "#md1 [onclick]" #> SHtml.ajaxInvoke(() => md(1))

  def render2 = "#md2 [onclick]" #> SHtml.ajaxInvoke(() => md(2))



}

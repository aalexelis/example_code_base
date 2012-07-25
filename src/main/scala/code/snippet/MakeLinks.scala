package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{S, Templates, SHtml}
import net.liftweb.http.js.jquery.JqJsCmds.ModalDialog
import net.liftweb.http.js.JsCmds._
import xml.NodeSeq
import net.liftweb.common.Loggable

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 6/22/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */

object MakeLinks extends Loggable {

  def ns = S.runTemplate("page" :: Nil) openOr NodeSeq.Empty

  def render1 = "#md1 [onclick]" #> SHtml.ajaxInvoke( () =>  ModalDialog(temp("1")))
  def render2 =  "#md2 [onclick]" #> SHtml.ajaxInvoke( () =>  ModalDialog(temp("2")))
  def render3 =  "#md3 [onclick]" #> SHtml.ajaxInvoke( () =>  ModalDialog(temp("3")))
  def replace1 = "#replace1" #> temp("1")
  def replace2 = "#replace2" #> temp("2")

  def temp(id: String) = Templates("pageview" :: id :: Nil) open_!


}

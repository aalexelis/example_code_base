package code.view

import net.liftweb.http.{RequestVar, S, Templates, LiftView}
import xml.NodeSeq
import net.liftweb.common
import common.{Loggable, Empty}
import net.liftweb.util.Helpers._
import net.liftweb.util.PassThru

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 6/21/12
 * Time: 2:26 AM
 * To change this template use File | Settings | File Templates.
 */

object ns extends RequestVar[(NodeSeq) => NodeSeq](PassThru)

object PageView extends LiftView with Loggable {
  def dispatch = {
    case _ => () => Empty
  }

  def pageview(id: String): NodeSeq = {

    ns.set({".urgh [pid]" #> id})

    def tmpl = Templates("page" :: Nil) open_!
    var nstmpl = (ns.get)(tmpl)
    nstmpl
    /*
    logger.info("ns(tmpl): "+nstmpl)
    logger.info("eval: "+S.eval(nstmpl))
    S.eval(nstmpl) open_!
    */
  }

}

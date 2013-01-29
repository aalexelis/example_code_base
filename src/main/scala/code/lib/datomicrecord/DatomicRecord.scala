package code.lib.datomicrecord

import datomic.Connection

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/29/13
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
object DatomicRecord {

  def init(){}

  def initWithDatomicConnection(sessionFactory: => Session){
    init()
    SessionFactory.concreteFactory = Some(() => sessionFactory)
  }

}

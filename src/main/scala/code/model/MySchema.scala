package code.model

import net.liftweb.common.{Logger, Loggable}

import net.liftweb.util.Props

import datomic.Peer
import datomic.peer.Connection
import code.lib.datomicrecord.{Session, DatomicRecord}

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/14/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */

object MySchemaHelper extends Loggable {
  /*
   *  Helper object that initiates the db connection and creates the schema
   *  Do not use this (createShema) approach in production
   */

    def initDatomicRecordWithInMemoryDB {
      initDatomicRecord(new MemSettings)
    }

    private def initDatomicRecord(db: DBSettings) {
      DatomicRecord.initWithDatomicConnection {
        val session:Session = Session.create( DatomicManager.getConnection(db) )
        session
      }
    }

    def dropAndCreateSchema{}

    trait DBSettings {
      val conntype: String=""
      val dbname: String = ""
      val host: String = ""
      val port: String = ""
      val jdbcurl: String = ""
      val dbUrl: String=""
    }

    class MemSettings extends DBSettings with Loggable {
      override val conntype = "mem"
      override val dbname = Props.get("datomic.dbname").openOr("test")
      override val dbUrl = "datomic:mem://"+dbname
      logger.debug("MemSettings:" + dbUrl)
    }


  object DatomicManager{
    def getConnection(db: DBSettings) = {
      implicit val conn = Peer.connect(db.dbUrl)
      conn
    }
  }


}
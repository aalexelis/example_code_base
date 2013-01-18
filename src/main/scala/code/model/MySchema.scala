package code.model

import org.squeryl.{Session, Schema}
import net.liftweb.common.{Logger, Loggable}
import net.liftweb.squerylrecord.RecordTypeMode._
import net.liftweb.squerylrecord.SquerylRecord
import org.squeryl.internals.DatabaseAdapter
import org.squeryl.adapters.{MySQLAdapter, H2Adapter}
import net.liftweb.util.Props
import java.sql.Connection
import com.jolbox.bonecp.{BoneCPConfig, BoneCP}

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/14/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
object MySchema extends Schema {
  val user = table[User]("Users")

}

object MySchemaHelper extends Loggable {
  /*
   *  Helper object that initiates the db connection and creates the schema
   *  Do not use this (createShema) approach in production
   */

    private var usingH2Driver=false;
    /*Public init convenience methods */
    def initSquerylRecordWithInMemoryDB {
      usingH2Driver=true;
      initSquerylRecord(new MyH2DBSettings)
    }

    def initSquerylRecordWithMySqlDB {
      initSquerylRecord(new MyMySqlDBSettings)
    }

    def dropAndCreateSchema {
      transaction {
        try {
          MySchema.printDdl //just print what we going to do
          MySchema.drop //drop everything that has been created in previous run of the app.
          MySchema.create //create schema as shown in printDdl
        } catch {
          case e =>
            e.printStackTrace()
            throw e;
        }
      }
    }

    def isUsingH2Driver:Boolean = usingH2Driver
    /*The actual init method */
    private def initSquerylRecord(db: DBSettings) {
      logger.debug("initSquerylRecord with DBSettings: driver="+db.dbDriver+" url="+db.dbUrl+" user="+db.dbUser+" pw="+db.dbPass)
      SquerylRecord.initWithSquerylSession {
        Class.forName(db.dbDriver)
        val session = Session.create(
          //init without connection pool
          //DriverManager.getConnection(db.dbUrl, db.dbUser, db.dbPass),
          //init with connection pool
          PoolProvider.getPoolConnection(db),
          db.dbAdapter)
        session.setLogger(statement => Logger("SqlLog:").debug(statement))
        session
      }
    }

    trait DBSettings {
      val dbAdapter: DatabaseAdapter;
      val dbDriver: String = ""
      val dbUrl: String = ""
      val dbUser: String = ""
      val dbPass: String = ""
    }

    class MyH2DBSettings extends DBSettings with Loggable {
      override val dbAdapter = new H2Adapter;
      override val dbDriver = Props.get("h2.db.driver").openOr("org.h2.Driver")
      //"jdbc:h2:mem:testSqRecDB;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=3000"
      override val dbUrl = Props.get("h2.db.url").openOr("jdbc:h2:database/testXYZDB;FILE_LOCK=NO") //drop to h2 on harddrive
      override val dbUser = Props.get("h2.db.user").openOr("test")
      override val dbPass = Props.get("h2.db.pass").openOr("test")
      logger.debug("MyH2DBSettings: seting adapter=H2Adapter driver="+dbDriver+" url="+dbUrl+" user="+dbUser+" pw="+dbPass)
    }

    class MyMySqlDBSettings extends DBSettings with Loggable {
      override val dbAdapter = new MySQLAdapter;
      override val dbDriver = Props.get("mysql.db.driver").openOr("org.h2.Driver")
      override val dbUrl = Props.get("mysql.db.url").openOr("jdbc:h2:database/testXYZDB;FILE_LOCK=NO") //drop to h2 on harddrive
      override val dbUser = Props.get("mysql.db.user").openOr("test")
      override val dbPass = Props.get("mysql.db.pass").openOr("test")
      logger.debug("MyMySqlDBSettings: seting adapter=MySQLAdapter driver="+dbDriver+" url="+dbUrl+" user="+dbUser+" pw="+dbPass)
    }

    /* database connection pooling provider - we are using BoneCP */
    object PoolProvider extends Loggable {

      def getPoolConnection(db: DBSettings) : Connection = {
        if(pool==null){
          pool=initPool(db)
        }
        pool.getConnection
      }

      private var pool:BoneCP=null
      private def initPool(db: DBSettings):BoneCP = {
        // create a new configuration object
        lazy val config = new BoneCPConfig
        try {
          // load the DB driver class
          Class.forName(db.dbDriver)
          // set the JDBC url
          config.setJdbcUrl(db.dbUrl)
          // set the username
          config.setUsername(db.dbUser)
          // set the password
          config.setPassword(db.dbPass)
          // setup the connection pool
          //pool = Full(new BoneCP(config))
          logger.info("BoneCP connection pool is now initialized.")
          new BoneCP(config)
        } catch {
          case e: Exception => {
            logger.error("BoneCP - FAILED to initialize connection pool.")
            throw new java.lang.Exception("BoneCP - FAILED to initialize connection pool."+e.printStackTrace)
          }
        }
      }

    }

  }

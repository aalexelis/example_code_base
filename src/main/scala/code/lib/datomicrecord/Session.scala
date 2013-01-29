package code.lib.datomicrecord

import datomic.Connection

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/29/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
class Session(val connection: Connection) {
  def bindToCurrentThread = Session.currentSession = Some(this)

  def unbindFromCurrentThread = Session.currentSession = None

  private var _logger: String => Unit = null

  def logger_=(f: String => Unit) = _logger = f

  def setLogger(f: String => Unit) = _logger = f

  def isLoggingEnabled = _logger != null

  def log(s:String) = if(isLoggingEnabled) _logger(s)

  var logUnclosedStatements = false

  def cleanup = {
    /*
    _statements.foreach(s => {
      if (logUnclosedStatements && isLoggingEnabled && !s.isClosed) {
        val stackTrace = Thread.currentThread.getStackTrace.map("at " + _).mkString("\n")
        log("Statement is not closed: " + s + ": " + System.identityHashCode(s) + "\n" + stackTrace)
      }
      Utils.close(s)
    })
    _statements.clear
    _resultSets.foreach(rs => Utils.close(rs))
    _resultSets.clear
    */
  }

  def close = {
    cleanup
    //connection.close
  }
}

trait SessionFactory {
  def newSession: Session
}

object SessionFactory {
  var concreteFactory: Option[()=>Session] = None
  var externalTransactionManagementAdapter: Option[()=>Option[Session]] = None
  def newSession: Session =
    concreteFactory.getOrElse(
      throw new Error("Error during new Session creation at SessionFactory.newSession")
    ).apply
}

object Session{
  private val _currentSessionThreadLocal = new ThreadLocal[Session]

  def create(c: Connection) =
    new Session(c)

  def currentSessionOption: Option[Session] = {
    Option(_currentSessionThreadLocal.get) orElse {
      SessionFactory.externalTransactionManagementAdapter flatMap { _.apply() }
    }
  }

  def currentSession: Session =
    if(SessionFactory.externalTransactionManagementAdapter != None) {
      SessionFactory.externalTransactionManagementAdapter.get.apply getOrElse( throw new Error("SessionFactory.externalTransactionManagementAdapter was unable to supply a Session for the current scope"))
    }
    else currentSessionOption.getOrElse(
      throw new Error("No session is bound to current thread, a session must be created via Session.create \nand bound to the thread via 'work' or 'bindToCurrentThread'\n Usually this error occurs when a statement is executed outside of a transaction/inTrasaction block"))

  def hasCurrentSession =
    currentSessionOption != None

  def cleanupResources =
    currentSessionOption foreach (_.cleanup)

  private def currentSession_=(s: Option[Session]) =
    if (s == None) {
      _currentSessionThreadLocal.remove()
    } else {
      _currentSessionThreadLocal.set(s.get)
    }

}

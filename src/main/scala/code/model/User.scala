package code.model

import org.squeryl.annotations.Column
import net.liftweb.record._
import net.liftweb.record.field._
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.squerylrecord.RecordTypeMode._
import xml.Text
import net.liftweb.util.FieldError
import net.liftweb.common.{Loggable, Full, Empty, Box}


/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 1/14/13
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
class User  { //extends MegaProtoUser[User] {
  /*
  def meta = User

  override def valUnique(errorMsg: => String)(email: String): List[FieldError] = transaction {
    from(User.table)(u => where(User.email === email) select(u)).toList.
    filter(u => !(u.uniqueId.get == this.uniqueId.get)) match {
      case Nil => Nil
      case x :: _ => List(FieldError(User.email, Text(errorMsg)))
    }
  }
  */

}

object User { // extends User with MetaMegaProtoUser[User] with Loggable {
  /*
  def table = MySchema.user

  def idFromString(id: String) = id.toLong

  def getAllList() = transaction {
    from(table)(u => select(u)).toList
  }

  def findUserByUserName(email: String): Box[User] = getAllList.find(_.email.get == email)

  def findUserByUniqueId(id: String): Box[User] = getAllList.find(_.id.get == id.toLong)

  def userFromStringId(id: String): Box[User] = findUserByUniqueId(id)

  override def screenWrap = Full(<lift:surround with="default" at="content">
    <lift:bind /></lift:surround>)
  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
    locale, timezone, password)

  // comment this line out to require email validations
  override def skipEmailValidation = true
  */

  /*
  override protected implicit def typeToBridge(in: TheUserType): UserBridge =
    new MyNewUserBridge(in)
  protected class MyNewUserBridge(in: TheUserType) extends MyUserBridge(in) with Loggable {

    override def testPassword(toTest: Box[String]): Boolean = {

      logger.info("testPassword")
      toTest.map(in.password.match_?) openOr false
    }

    override def setPasswordFromListString(pwd: List[String]): TheUserType = {

      logger.info("setPasswordFromListString")
      in.password.setFromAny(pwd)
      in
    }
  }
  */

}

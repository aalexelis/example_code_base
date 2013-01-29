package code
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import datomic.{Database, Peer}
import datomics._
import clojure.lang.Keyword
import java.io.FileReader

import concurrent.ExecutionContext.Implicits.global

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  // replace the contents of the element with id "time" with the date
  def howdy ={
    val uri = "datomic:mem://hello"
    //val uri = "datomic:free://localhost:4334/hello"
    Peer.createDatabase(uri)

    implicit val conn = Peer.connect(uri)

    val db = conn.db
    implicit val typer = TypeStore.top.schema(db)

    val signature = typer.check("[:find ?entity :where [?entity :db/doc \"hello world\"]]")
    println(signature)

    val signature2 = typer.check("[:find ?n ?k :where [_ :db.install/attribute ?a] [?a :db/ident ?n] [?a :db/valueType ?t] [?t :db/ident ?a]]")
    println(signature2)  // fail

    println(Query[Keyword, Keyword]("[:find ?n ?k :where [_ :db.install/attribute ?a] [?a :db/ident ?n] [?a :db/valueType ?t] [?t :db/ident ?k]]", db))

    val transactions = readEdnAsTransactions(new FileReader("src/main/resources/social-news.edn"))
    // transactions :Vector[Transaction]
    val socialNews = Transaction.transactAll(transactions)
    // socialNews :Future[ITransactionResult]

    socialNews.onComplete(r => println("Social news: " + r))

    val newUserId = tempId()

    def allStories(db :Database) = Query.entities("[:find ?e :where [?e :story/url]]", db)

    def upvoteAllStories(db :Database) = allStories(db).foldLeft(Transaction())((t,e) => t.add(newUserId, "user/upVotes" -> id(e)))

    val newUser = Transaction().add(newUserId,
      "user/email" -> "john@example.com",
      "user/firstName" -> "John",
      "user/lastName" -> "Doe")

    val userStories = socialNews.map { result => (newUser ++ upvoteAllStories(result.after)).transact }

    // upsert
    val updateUser = userStories.map { _ => Transaction().add(tempId(),
      "user/email" -> "john@example.com",
      "user/firstName" -> "Johnatan").transact }

    val retractUpvote = userStories.flatMap { result =>

      val db = result.after

      for (john        <- QueryF.findBy(db, "user/email" -> "john@example.com");
           upvoteForPG <- QueryF.entity(
             "[:find ?story :in $ ?e :where [?e :user/upVotes ?story]" +
               "[?story :story/url \"http://www.paulgraham.com/avg.html\"]]", db, id(john));
           retract <- Transaction().retract(john, "user/upVotes" -> id(upvoteForPG)).transactF )
      yield retract
    }
    retractUpvote.onComplete( r => println("Retract Upvote: " + r) )

    Query.allowUnsafeResults = true
    println(Query[Long]("[:find ?a :where [(+ 1 1) ?a]]"))
    Query.allowUnsafeResults = false

    import reflect.runtime.universe._

    val typer2 = typer.bind("?a" -> typeOf[Long])
    println(Query[Long]("[:find ?a :where [(+ 1 1) ?a]]", typer2))

    val typer3 = typer.bindFn("+" -> DFTAtomic(typeOf[Long]))
    println(Query[Long]("[:find ?a :where [(+ 1 1) ?a]]", typer3))




    "#time *" #> date.map(_.toString) }

  /*
   lazy val date: Date = DependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> date.toString
   */
}


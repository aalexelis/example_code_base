name := "Lift 2.5 starter template"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.10.0"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases",
                "Datomic-scala"   at "https://dl.dropbox.com/u/1092243/fbellomi-public/repository/snapshots/",
                "spy"             at "http://files.couchbase.com/maven2/"
                )

seq(com.github.siasia.WebPlugin.webSettings :_*)

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5-SNAPSHOT"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftweb"      %% "lift-squeryl-record"  % liftVersion          % "compile->default",
    "net.liftmodules"   %% "lift-jquery-module" % "2.5-SNAPSHOT-2.1-SNAPSHOT",
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "com.jolbox"               % "bonecp"         % "0.7.1.RELEASE"    % "compile->default",
    "com.h2database"           % "h2"             % "1.3.167",
    "mysql"                    % "mysql-connector-java" % "5.1.18",
    "org.specs2"        %% "specs2"             % "1.13"           % "test",
    "datomic-scala" % "datomic-scala_2.10" % "0.1-SNAPSHOT"
  )
}


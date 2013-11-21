import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "app"
  val appVersion      = "1.0-SNAPSHOT"

  val dbDependencies = Seq(
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "com.typesafe.play" %% "play-slick" % "0.4.0",
    jdbc
  )

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.newrelic.agent.java" % "newrelic-agent" % "3.1.0"
  ) ++ dbDependencies

  val api = play.Project("api", "1.0", path = file("modules/api"))

  val db = play.Project("db", "1.0", dbDependencies, path = file("modules/db"))

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += Resolver.url("sbt-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
  ).dependsOn(api, db).aggregate(api, db)

}

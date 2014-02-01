import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "toolbox"
  val appVersion      = "1.0-SNAPSHOT"

  val dbDependencies = Seq(
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "com.typesafe.play" %% "play-slick" % "0.5.0.8" exclude("org.scala-stm", "scala-stm_2.10.0"),
    jdbc
  )

  val scrapeDependencies = Seq(
    "net.sourceforge.htmlunit" % "htmlunit" % "2.13",
    "org.apache.httpcomponents" % "httpclient" % "4.3.1",
    "com.itextpdf" % "itextpdf" % "5.4.5",
    "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2",
    "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"
  )

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.newrelic.agent.java" % "newrelic-agent" % "3.1.0"
  ) ++ dbDependencies ++ scrapeDependencies


  //mklink /D C:\workspace\joec\toolbox\modules\puzzles C:\workspace\joec\scala\puzzles
  val puzzles = Project("puzzles", file("modules/puzzles"), settings = Defaults.defaultSettings ++ play.Project.intellijCommandSettings)

  val api = play.Project("api", "1.0", path = file("modules/api"))

  val db = play.Project("db", "1.0", dbDependencies, path = file("modules/db"))

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += Resolver.url("sbt-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
  ).dependsOn(puzzles, api, db).aggregate(puzzles, api, db)

}

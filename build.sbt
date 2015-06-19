name := "toolbox"

version := "1.0"

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.typesafe.play" %% "play-mailer" % "2.4.1",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "font-awesome" % "4.3.0",
  "org.webjars" % "knockout" % "3.3.0",
  "org.webjars" % "knockout-projections" % "1.0.0",
  "org.webjars" % "swagger-ui" % "2.1.8-M1"
)

//mklink /D C:\workspace\joec\toolbox\modules\scala-puzzles C:\workspace\joec\scala-puzzles
lazy val puzzles = project.in(file("modules/scala-puzzles"))

//mklink /D C:\workspace\joec\toolbox\modules\scala-scrape C:\workspace\joec\scala-scrape
lazy val scrape = project.in(file("modules/scala-scrape"))

//mklink /D C:\workspace\joec\toolbox\modules\swagger2postman C:\workspace\joec\swagger2postman
lazy val swagger2postman = project.in(file("modules/swagger2postman"))

//base
lazy val toolbox = (project in file(".")).enablePlugins(PlayScala)
  .dependsOn(puzzles, scrape, swagger2postman)
  .aggregate(puzzles, scrape, swagger2postman)
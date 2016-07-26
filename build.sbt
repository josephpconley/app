name := "toolbox"

version := "1.0"

val puzzlesDeps = Seq(
  "net.sourceforge.htmlunit" % "htmlunit" % "2.13",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"
)

val scrapeDeps = Seq(
  "net.sourceforge.htmlunit" % "htmlunit" % "2.13",
  "org.apache.httpcomponents" % "httpclient" % "4.3.1",
  "com.itextpdf" % "itextpdf" % "5.4.5",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2",
  "com.github.simplyscala" %% "http-client" % "0.1"
)

val swaggerDeps = Seq(
  "com.typesafe.play" %% "play-json" % "2.3.4",
  "org.rogach" %% "scallop" % "0.9.5",
  "com.stackmob" %% "newman" % "1.3.5"
)

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
) ++ puzzlesDeps ++ scrapeDeps ++ swaggerDeps

//mklink /D C:\workspace\joec\toolbox\modules\scala-puzzles C:\workspace\joec\scala-puzzles
//lazy val puzzles = project.in(file("modules/scala-puzzles"))

//mklink /D C:\workspace\joec\toolbox\modules\scala-scrape C:\workspace\joec\scala-scrape
//lazy val scrape = project.in(file("modules/scala-scrape"))

//mklink /D C:\workspace\joec\toolbox\modules\swagger2postman C:\workspace\joec\swagger2postman
lazy val swagger2postman = project.in(file("modules/swagger2postman"))

//base
lazy val toolbox = (project in file(".")).enablePlugins(PlayScala)
  .dependsOn(swagger2postman)
  .aggregate(swagger2postman)
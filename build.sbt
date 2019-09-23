val ZIOVersion = "1.0.0-RC13"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-workshop",
    organization := "net.degoes",
    scalaVersion := "2.12.8",
    initialCommands in Compile in console := """
                                               |import zio._
                                               |import console._
                                               |import zio.stream._
                                               |object replRTS extends DefaultRuntime {}
                                               |import replRTS._
                                               |implicit class RunSyntax[E, A](io: ZIO[DefaultRuntime#Environment, E, A]){ def unsafeRun: A = replRTS.unsafeRun(io) }
                                         """.stripMargin
  )

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

libraryDependencies ++= Seq(
  // ZIO
  "dev.zio" %% "zio"         % ZIOVersion,
  "dev.zio" %% "zio-streams" % ZIOVersion,
  // URL parsing
  "io.lemonlabs" %% "scala-uri" % "1.4.1"
)

scalacOptions in Compile in console := Seq(
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:existentials",
  "-Yno-adapted-args",
  "-Xsource:2.13",
  "-Yrepl-class-based",
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-explaintypes",
  "-Yrangepos",
  "-feature",
  "-Xfuture",
  "-unchecked",
  "-Xlint:_,-type-parameter-shadow",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-opt-warnings",
  "-Ywarn-extra-implicit",
  "-Ywarn-unused:_,imports",
  "-Ywarn-unused:imports",
  "-opt:l:inline",
  "-opt-inline-from:<source>",
  "-Ypartial-unification",
  "-Yno-adapted-args",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit"
)

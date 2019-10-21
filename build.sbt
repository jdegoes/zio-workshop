val ZIOVersion        = "1.0.0-RC15"
val CatsEffectVersion = "2.0.0"
val MonixVersion      = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-workshop",
    organization := "net.degoes",
    scalaVersion := "2.12.8",
    initialCommands in Compile in console :=
      """|import zio._
         |import zio.console._
         |import zio.duration._
         |object replRTS extends DefaultRuntime {}
         |import replRTS._
         |implicit class RunSyntax[R >: replRTS.Environment, E, A](io: ZIO[R, E, A]){ def unsafeRun: A = replRTS.unsafeRun(io) }
    """.stripMargin
  )

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

libraryDependencies ++= Seq(
  // ZIO
  "dev.zio"       %% "zio"          % ZIOVersion,
  "dev.zio"       %% "zio-streams"  % ZIOVersion,
  "org.typelevel" %% "cats-effect"  % CatsEffectVersion,
  "io.monix"      %% "monix"        % MonixVersion,
  // URL parsing
  "io.lemonlabs"  %% "scala-uri"    % "1.4.1"
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

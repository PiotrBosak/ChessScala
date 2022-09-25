name := "ChessScala"

import Dependencies.{ Libraries, _ }

import sbtwelcome._
import sbt.Keys.libraryDependencies
ThisBuild / watchBeforeCommand := Watch.clearScreen

version := "0.1"

val catsVersion    = "2.7.0"
val monocleVersion = "3.0.0-M6"
lazy val root = (project in file("."))
  .settings(
    name := "chess"
  )
  .aggregate(frontend, backend, tests)
  .settings(scalafmtOnCompile := true)

lazy val tests = (project in file("tests"))
  .configs(IntegrationTest)
  .settings(
    name := "chess-backend-test-suite",
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.circeCore,
      Libraries.circeParser,
      Libraries.circeExtras,
      Libraries.circeRefined,
      Libraries.cirisCore,
      Libraries.cirisRefined,
      Libraries.fs2Core,
      Libraries.fs2Kafka,
      Libraries.http4sDsl,
      Libraries.http4sMetrics,
      Libraries.http4sServer,
      Libraries.kittens,
      Libraries.monocleCore,
      Libraries.neutronCore,
      Libraries.odin,
      Libraries.redis4catsEffects,
      Libraries.refinedCore,
      Libraries.refinedCats,
      Libraries.monocleLaw,
      Libraries.skunk("core"),
      Libraries.skunk("circe"),
      Libraries.scalacheck,
      Libraries.weaverCats,
      Libraries.weaverDiscipline,
      Libraries.weaverScalaCheck
    ),
    scalaVersion := "3.2.0"
  )
  .dependsOn(backend)

lazy val lib = (project in file("lib"))
  .settings(scalacOptions += "-explain")
  .settings(
    name := "lib",
    libraryDependencies ++= Seq(
      "org.typelevel"              %% "cats-core"     % catsVersion,
      "org.scalatest"              %% "scalatest"     % "3.2.9",
      "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
      "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
      Libraries.cats,
      Libraries.monocleCore,
      Libraries.squants,
      Libraries.monocleLaw,
      Libraries.cirisCore,
      Libraries.cirisRefined,
      Libraries.ip4s,
      Libraries.circeCore,
      Libraries.circeParser,
      Libraries.kittens
    ),
    scalaVersion := "3.2.0"
  )
  .settings(scalacOptions -= "-Ywarn-unused")
  .settings(scalacOptions -= "-Xfatal-warnings")

lazy val backend = (project in file("backend"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "chess-backend",
    Defaults.itSettings,
    dockerBaseImage       := "openjdk:11-jre-slim-buster",
    makeBatScripts        := Seq(),
    packageName in Docker := "chess",
    dockerExposedPorts ++= Seq(8080),
    dockerUpdateLatest := true,
    scalaVersion       := "3.2.0",
    libraryDependencies ++= Seq(
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.catsRetry,
      Libraries.jwtScala("core"),
      Libraries.jwtScala("circe"),
      Libraries.circeCore,
      Libraries.circeParser,
      Libraries.circeExtras,
      Libraries.circeRefined,
      Libraries.cirisCore,
      Libraries.cirisRefined,
      Libraries.fs2Core,
      Libraries.fs2Kafka,
      Libraries.http4sCirce,
      Libraries.http4sClient,
      Libraries.http4sDsl,
      Libraries.http4sMetrics,
      Libraries.http4sServer,
      Libraries.kittens,
      Libraries.monocleCore,
      Libraries.neutronCore,
      Libraries.odin,
      Libraries.redis4catsEffects,
      Libraries.refinedCore,
      Libraries.refinedCats,
      Libraries.skunk("core"),
      Libraries.skunk("circe"),
      Libraries.slf4j,
      Libraries.squants,
      Libraries.monocleLaw       % Test,
      Libraries.scalacheck       % Test,
      Libraries.weaverCats       % Test,
      Libraries.weaverDiscipline % Test,
      Libraries.weaverScalaCheck % Test
    )
  )
  .dependsOn(lib)

lazy val frontend =
  (project in file("frontend"))
    .enablePlugins(ScalaJSPlugin)
    .settings( // Normal settings
      name         := "chessfronttyrian",
      version      := "0.0.1",
      scalaVersion := "3.2.0",
      organization := "myorg",
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian-io" % "0.5.1",
        "org.scalameta"   %%% "munit"     % "0.7.29" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework"),
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      autoAPIMappings := true
    )
    .settings( // Welcome message
      logo := "ChessFrontTyrian (v" + version.value + ")",
      usefulTasks := Seq(
        UsefulTask("", "fastOptJS", "Rebuild the JS (use during development)"),
        UsefulTask(
          "",
          "fullOptJS",
          "Rebuild the JS and optimise (use in production)"
        ),
        UsefulTask("", "code", "Launch VSCode")
      ),
      logoColor        := scala.Console.MAGENTA,
      aliasColor       := scala.Console.BLUE,
      commandColor     := scala.Console.CYAN,
      descriptionColor := scala.Console.WHITE,
      libraryDependencies ++= Seq(
        "io.circe"      %%% s"circe-core"          % "0.14.2",
        "org.http4s"    %%% s"http4s-ember-client" % "1.0.0-M35",
        "io.circe"      %%% s"circe-parser"        % "0.14.2",
        "org.typelevel" %%% "kittens"              % "3.0.0-M4",
        "org.typelevel" %%% "cats-core"            % "2.7.0",
        "org.typelevel" %%% "cats-effect"          % "3.3.5",
        "org.scalatest" %%% "scalatest"            % "3.2.9"
      )
    )
    .dependsOn(lib)

addCommandAlias("runLinter", ";scalafixAll --rules OrganizeImports")

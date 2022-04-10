name := "ChessScala"

import Dependencies.{ Libraries, _ }
import sbt.Keys.libraryDependencies

version := "0.1"

val catsVersion    = "2.7.0"
val monocleVersion = "3.0.0-M6"
lazy val chessLogic = (project in file("chessLogic"))
  .settings(
    name := "chessLogic",
    libraryDependencies ++= Seq(
      "org.typelevel"              %% "cats-core"     % catsVersion,
      "org.scalatest"              %% "scalatest"     % "3.2.9",
      "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
      "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
      Libraries.circeCore,
      Libraries.circeParser,
      Libraries.kittens
    ),
    scalaVersion := "3.1.1"
  )
  .settings(scalacOptions -= "-Ywarn-unused")
  .settings(scalacOptions -= "-Xfatal-warnings")
lazy val root = (project in file("."))
  .settings(
    name := "chess"
  )
  .aggregate(chessLogic, core, tests)
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
    scalaVersion := "3.1.1"
  )
  .dependsOn(core)

lazy val core = (project in file("backend"))
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
    scalaVersion       := "3.1.1",
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
  .dependsOn(chessLogic)

addCommandAlias("runLinter", ";scalafixAll --rules OrganizeImports")

name := "ChessScala"

import Dependencies.{Libraries, _}
//todo dorob common settings

import sbtwelcome._
import sbt.Keys.libraryDependencies
ThisBuild / watchBeforeCommand := Watch.clearScreen

version := "0.1"

val catsVersion = "2.7.0"
val monocleVersion = "3.0.0-M6"
lazy val root = (project in file("."))
  .settings(
    name := "chess"
  )
  .aggregate(frontend, httpserver, gameMatcher, gameProcessor, ws, tests)

def dockerSettings(name: String) = List(
  Docker / packageName := s"trading-$name",
  dockerBaseImage := "jdk17-curl:latest",
  dockerExposedPorts ++= List(8080),
  makeBatScripts := Nil,
  dockerUpdateLatest := true
)

lazy val tests = (project in file("modules/tests"))
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
      Libraries.http4sCirce,
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

lazy val lib = (crossProject(JSPlatform, JVMPlatform) in file("modules/lib"))
  .settings(scalacOptions += "-explain")
  .settings(
    name := "lib",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % catsVersion,
      "org.scalatest" %% "scalatest" % "3.2.9",
      "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
      "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.monocleCore,
      Libraries.squants,
      Libraries.http4sCirce,
      Libraries.http4sDsl,
      Libraries.monocleLaw,
      Libraries.cirisCore,
      Libraries.cirisRefined,
      Libraries.neutronCore,
      Libraries.odin,
      Libraries.redis4catsEffects,
      Libraries.refinedCore,
      Libraries.refinedCats,
      Libraries.ip4s,
      Libraries.circeCore,
      Libraries.circeParser,
      Libraries.kittens
    ),
    scalaVersion := "3.2.0"
  )
  .settings(scalacOptions -= "-Ywarn-unused")
  .settings(scalacOptions -= "-Xfatal-warnings")

lazy val httpserver = (project in file("modules/http-server"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "chess-gateway",
    Defaults.itSettings,
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    makeBatScripts := Seq(),
    packageName in Docker := "chess-gateway",
    dockerExposedPorts ++= Seq(8080),
    dockerUpdateLatest := true,
    scalaVersion := "3.2.0",
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
      Libraries.monocleLaw % Test,
      Libraries.scalacheck % Test,
      Libraries.weaverCats % Test,
      Libraries.weaverDiscipline % Test,
      Libraries.weaverScalaCheck % Test
    )
  )
  .dependsOn(lib.jvm)

lazy val ws = (project in file("modules/ws-server"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(dockerSettings("ws"))
  .settings(scalaVersion := "3.2.0")
  .settings(
    libraryDependencies ++= List(
      Libraries.http4sCirce,
      Libraries.http4sMetrics,
      Libraries.http4sServer,
      Libraries.http4sServer,
      Libraries.http4sDsl,
      Libraries.neutronCore
    )
  )
  .dependsOn(lib.jvm)
lazy val gameMatcher = (project in file("modules/gameMatcher"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "chess-game-matcher",
    Defaults.itSettings,
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    makeBatScripts := Seq(),
    packageName in Docker := "chess-game-matcher",
    dockerExposedPorts ++= Seq(8080),
    dockerUpdateLatest := true,
    scalaVersion := "3.2.0",
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
      Libraries.monocleLaw % Test,
      Libraries.scalacheck % Test,
      Libraries.weaverCats % Test,
      Libraries.weaverDiscipline % Test,
      Libraries.weaverScalaCheck % Test
    )
  )
  .dependsOn(lib.jvm)

lazy val gameProcessor = (project in file("modules/gameProcessor"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "chess-game-processor",
    Defaults.itSettings,
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    makeBatScripts := Seq(),
    packageName in Docker := "gameProcessor",
    dockerExposedPorts ++= Seq(8080),
    dockerUpdateLatest := true,
    scalaVersion := "3.2.0",
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
      Libraries.monocleLaw % Test,
      Libraries.scalacheck % Test,
      Libraries.weaverCats % Test,
      Libraries.weaverDiscipline % Test,
      Libraries.weaverScalaCheck % Test
    )
  )
  .dependsOn(lib.jvm)

lazy val frontend = (project in file("modules/frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings( // Normal settings
    name := "chessfronttyrian",
    version := "0.0.1",
    scalaVersion := "3.2.0",
    organization := "myorg",
    libraryDependencies ++= Seq(
      "io.indigoengine" %%% "tyrian-io" % "0.6.0",
      "org.scalameta" %%% "munit" % "1.0.0-M6" % Test
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
    logoColor := scala.Console.MAGENTA,
    aliasColor := scala.Console.BLUE,
    commandColor := scala.Console.CYAN,
    descriptionColor := scala.Console.WHITE,
    libraryDependencies ++= Seq(
      "io.circe" %%% s"circe-core" % "0.14.2",
      "org.http4s" %%% s"http4s-ember-client" % "1.0.0-M35",
      "io.circe" %%% s"circe-parser" % "0.14.2",
      "org.typelevel" %%% "kittens" % "3.0.0-M4",
      "org.typelevel" %%% "cats-core" % "2.7.0",
      "org.typelevel" %%% "cats-effect" % "3.3.5",
      "org.scalatest" %%% "scalatest" % "3.2.9"
    )
  )
  .dependsOn(lib.js)

addCommandAlias("runLinter", ";scalafixAll --rules OrganizeImports")

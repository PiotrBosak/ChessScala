name := "ChessScala"

import Dependencies._
import sbt.Keys.libraryDependencies

version := "0.1"

lazy val commonSettings = Seq(
  scalacOptions ++= List("-Ymacro-annotations", "-Yrangepos", "-Wconf:cat=unused:info"),
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
  scalacOptions -= "-Ywarn-unused",
 scalacOptions -= "-Xfatal-warnings"
)
lazy val chessLogic = (project in file("chessLogic"))
  .settings(commonSettings)
  .settings(
    name := "chessLogic",
    scalaVersion := "2.13.7",
    libraryDependencies ++= Seq(
      Libraries.cats,
      Libraries.scalaTest,
      Libraries.derevoCore,
      Libraries.derevoCats,
      Libraries.derevoCirce,
    )
  )
lazy val root = (project in file("."))
  .settings(
    name := "chess"
  )
  .aggregate(chessLogic, core, tests)

lazy val tests = (project in file("tests"))
  .configs(IntegrationTest)
  .settings(commonSettings)
  .settings(
    name := "chess-backend-test-suite",
    scalaVersion := "2.13.7",
    testFrameworks += new TestFramework("weaver.framework.CatsEffect"),
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      CompilerPlugin.kindProjector,
      CompilerPlugin.betterMonadicFor,
      Libraries.catsLaws,
      Libraries.log4catsNoOp,
      Libraries.monocleLaw,
      Libraries.refinedScalacheck,
      Libraries.weaverCats,
      Libraries.weaverDiscipline,
      Libraries.weaverScalaCheck
    )
  )
  .dependsOn(core)

lazy val core = (project in file("backend"))
  .settings(
    name := "chess-backend",
    scalaVersion := "2.13.7",
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      CompilerPlugin.kindProjector,
      CompilerPlugin.betterMonadicFor,
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.catsRetry,
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.circeRefined,
      Libraries.cirisCore,
      Libraries.cirisEnum,
      Libraries.cirisRefined,
      Libraries.derevoCore,
      Libraries.derevoCats,
      Libraries.derevoCirce,
      Libraries.fs2,
      Libraries.http4sDsl,
      Libraries.http4sServer,
      Libraries.http4sClient,
      Libraries.http4sCirce,
      Libraries.http4sJwtAuth,
      Libraries.javaxCrypto,
      Libraries.log4cats,
      Libraries.logback % Runtime,
      Libraries.monocleCore,
      Libraries.newtype,
      Libraries.redis4catsEffects,
      Libraries.redis4catsLog4cats,
      Libraries.refinedCore,
      Libraries.refinedCats,
      Libraries.skunkCore,
      Libraries.skunkCirce,
      Libraries.squants
    )
  ).dependsOn(chessLogic)

addCommandAlias("runLinter", ";scalafixAll --rules OrganizeImports")

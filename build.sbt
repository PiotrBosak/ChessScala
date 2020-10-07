name := "ChessScala"

version := "0.1"

scalaVersion := "2.13.3"

scalaVersion := "2.13.3"
lazy val akkaVersion = "2.6.8"
val catsVersion = "2.1.1"
val monocleVersion = "2.0.3"
val akkaHttpVersion = "10.1.12"
libraryDependencies += "org.tpolecat" %% "skunk-core" % "0.0.20"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.2.0"



  ,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion
)
scalacOptions ++= Seq(
  "-language:higherKinds"
)
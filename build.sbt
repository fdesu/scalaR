import sbt.Keys.libraryDependencies

name := "scout24scala"
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  guice,
  javaJpa,
  evolutions,
  "com.h2database" % "h2" % "1.4.197",
  "org.hibernate" % "hibernate-core" % "5.3.7.Final"
)

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.assertj" % "assertj-core" % "3.11.1" % Test
)
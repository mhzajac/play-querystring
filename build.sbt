name := "play-querystring"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.7")

organization := "com.jaroop"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play" % "2.4.6",
    "com.typesafe.play" %% "play-functional" % "2.4.6",
    "org.specs2" %% "specs2-core" % "3.6.5" % "test",
    "org.specs2" %% "specs2-matcher-extra" % "3.6.5" % "test"
)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

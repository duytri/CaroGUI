name := "CaroGUI"
version := "0.1"
organization := "uit.ai"
scalaVersion := "2.12.3"

assemblyJarName in assembly := "CaroGUI.jar"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.12.3"
)

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
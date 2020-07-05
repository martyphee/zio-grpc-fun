val zioGrpcVersion = "0.3.0"

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.0")
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.34")

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % zioGrpcVersion,
  "com.thesamet.scalapb" %% "compilerplugin" % "0.10.6"
)

// For Scala.js:
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.1.1")

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")

addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.18.0")

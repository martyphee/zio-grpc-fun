ThisBuild / resolvers += Resolver.sonatypeRepo("snapshots")

ThisBuild / scalaVersion := "2.13.1"

ThisBuild / cancelable in Global := true

ThisBuild / connectInput := true

val grpcVersion = "1.30.1"
val zioGrpcVersion = "0.3.0"

lazy val protos = crossProject(JVMPlatform)
  .in(file("protos"))
  .settings(
    PB.targets in Compile := Seq(
      scalapb.gen(grpc = true) -> (sourceManaged in Compile).value,
      scalapb.zio_grpc.ZioCodeGenerator -> (sourceManaged in Compile).value
    ),
    PB.protoSources in Compile := Seq(
      (baseDirectory in ThisBuild).value / "protos" / "src" / "main" / "protobuf"
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %%% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
    )
  )

lazy val server = project
    .dependsOn(protos.jvm)
    .settings(
      libraryDependencies ++= Seq(
        "io.grpc" % "grpc-netty" % grpcVersion,
      ),
    )

package main

import examples.greeter.ZioGreeter.GreeterClient
import examples.greeter._
import io.grpc.ManagedChannelBuilder
import scalapb.zio_grpc.ZManagedChannel
import zio.Layer
import zio.console._

object Client extends zio.App {
  def run(args: List[String]) =
    myAppLogic.exitCode

  // putStr("Client is running")
  // putStr(s"The exitcode ${myAppLogic.exitCode}")

  def clientLayer: Layer[Throwable, GreeterClient] =
    GreeterClient.live(
      ZManagedChannel(
        ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext()
      )
    )

  def myAppLogic =
    (for {
      r <- GreeterClient.greet(Request("Hello"))
      _ <- putStrLn(r.resp)
      f <- GreeterClient.greet(Request("Bye"))
      _ <- putStrLn(f.resp)
    } yield ())
      .onError { c => putStrLn(c.prettyPrint) }
      .provideLayer(Console.live ++ clientLayer)
}

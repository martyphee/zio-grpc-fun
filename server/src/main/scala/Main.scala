import examples.greeter.ZioGreeter.Greeter
import examples.greeter.{Point, Request, Response}
import io.grpc.{ServerBuilder, Status}
import scalapb.zio_grpc.Server
import zio.clock.Clock
import zio.console.{Console, _}
import zio.duration._
import zio.stream.Stream
import zio._
import env.ServiceA
import env.ServiceB

object GreeterService {
  type GreeterService = Has[Greeter]

  class LiveService(clock: Clock.Service, service: ServiceA.Service) extends Greeter {
    def greet(req: Request): IO[Status, Response] =
      clock.sleep(300.millis) *> zio.IO.succeed(
        Response(resp = "hello " + req.name)
      )

    def points(
                request: Request
              ): Stream[Status, Point] =
      (Stream(Point(3, 4))
        .scheduleElements(Schedule.spaced(1000.millis))
        .forever
        .take(5) ++
        Stream.fail(
          Status.INTERNAL
            .withDescription("There was an error!")
            .withCause(new RuntimeException)
        )).provide(Has(clock))

    def bidi(
              request: Stream[Status, Point]
            ): Stream[Status, Response] = {
      request.grouped(chunkSize = 3).map(r => Response(r.toString()))
    }
  }

  val live: ZLayer[Clock with ServiceA, Nothing, GreeterService] =
    ZLayer.fromServices[Clock.Service, ServiceA.Service, Greeter] (new LiveService(_,_))
}

object ExampleServer extends App {
  def serverWait: ZIO[Console with Clock, Throwable, Unit] =
    for {
      _ <- putStrLn("Server is running. Press Ctrl-C to stop.")
      _ <- (putStr(".") *> ZIO.sleep(30.second)).forever
    } yield ()

  def serverLive(port: Int): ZLayer[Console, Nothing, Server] = {
    val layerServiceB: ZLayer[Console, Nothing, Console with ServiceB] = Console.live ++ ServiceB.live
    val layerServiceA: ZLayer[Console, Nothing, ServiceA] = layerServiceB >>> ServiceA.live
    val layer: ZLayer[Console, Nothing, GreeterService.GreeterService] = (Clock.live ++ layerServiceA) >>> GreeterService.live

    layer >>> Server.live[Greeter](
      ServerBuilder.forPort(port)
    )
  }

  def run(args: List[String]): URIO[Any with Console, ExitCode] = myAppLogic.exitCode

  val myAppLogic =
    serverWait.provideLayer(serverLive(9090) ++ Console.live ++ Clock.live)
}

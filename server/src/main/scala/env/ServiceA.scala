package env

import zio._
import zio.console._

object ServiceA {

    class Service(c: Console.Service, b: ServiceB.Service) {
        def sayHiA(s: String): IO[Nothing, Unit] = {
            c.putStrLn(s"Hello ${s} from service!") *> b.sayHiB(s)
        }
    }

    val live: ZLayer[Console with ServiceB, Nothing, ServiceA] = 
        ZLayer.fromServices[Console.Service, ServiceB.Service, ServiceA.Service](
            new Service(_,_)
        )

}

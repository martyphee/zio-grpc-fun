package env

import zio._
import zio.console._

object ServiceB {
    trait Service {
        def sayHiB(s: String): Task[Unit]
    }

    // class LiveService(c: Console) extends Service {
    //     def sayHiB(s: String): Unit = ???
    // }

    val live: ZLayer[Console, Nothing, ServiceB] = ZLayer.fromService { console =>
        new Service {
            def sayHiB(s: String): Task[Unit] = ???
        }
    }
}
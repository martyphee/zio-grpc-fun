import zio._
import zio.console._

package object env {
    type ServiceA = Has[ServiceA.Service]
    type ServiceB = Has[ServiceB.Service]
}


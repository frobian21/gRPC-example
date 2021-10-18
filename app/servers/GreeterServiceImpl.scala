package servers

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import proto._

class GreeterServiceImpl(implicit mat: Materializer) extends GreeterService {
  override def itKeepsReplying(in: HelloRequest): Source[HelloReply, NotUsed] = {
    println(s"sayHello to ${in.limit} with stream of chars...")
    Source(1.to(in.limit).map(int => HelloReply(int)))
  }

} 
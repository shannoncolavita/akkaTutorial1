package akkatutorial

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinRouter
import akka.util.duration._

class Master(numberOfWorkers: Int, numberOfMessages: Int, numberOfElements: Int, listener: ActorRef)
  extends Actor {

  var pi: Double = _
  var numberOfResults: Int = _
  val start: Long = System.currentTimeMillis

  val workerRouter = context.actorOf(
    Props[Worker].withRouter(RoundRobinRouter(numberOfWorkers)), name = "workerRouter")

  def receive = {
    case Calculate ⇒
      for (i ← 0 until numberOfMessages) workerRouter ! Work(i * numberOfElements, numberOfElements)
    case Result(value) ⇒
      pi += value
      numberOfResults += 1
      if (numberOfResults == numberOfMessages) {
        // Send the result to the listener
        listener ! PiApproximation(pi, duration = (System.currentTimeMillis - start).millis)
        // Stops this actor and all its supervised children
        context.stop(self)
      }
  }

}
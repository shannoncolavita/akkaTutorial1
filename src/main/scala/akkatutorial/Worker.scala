package akkatutorial

import akka.actor.Actor

class Worker extends Actor {

  def receive = {
    case Work(start, numberOfElements) =>
      sender ! Result(calculatePiFor(start, numberOfElements))
  }

  def calculatePiFor(start: Int, numberOfElements: Int): Double = {
    var acc = 0.0
    for (i ← start until (start + numberOfElements))
      acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    acc
  }
}

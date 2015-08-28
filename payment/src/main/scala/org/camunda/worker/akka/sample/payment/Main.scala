

package org.camunda.worker.akka/**
 * @author Philipp Ossler
 */

import scala.collection.JavaConversions._
import org.camunda.worker.akka.PollActor.Poll
import org.camunda.worker.dto.LockedTaskDto
import akka.actor._
import org.camunda.worker.akka.worker._
import scala.io.StdIn._

/**
 * @author Philipp Ossler
 */
object Main extends App {
 
  println("")
  println("starting...........")
  println("press ENTER to exit")
  println("===================")
  println("")
  
  // create actor system
  val system = ActorSystem("MyActorSystem")
  
  // create worker
  val worker = system.actorOf(UnreliableWorker.props(delay = 200, reliability = 0.75), name = "worker-1")
  
  // start polling
  val pollActor = system.actorOf(PollActor.props(hostAddress = "http://localhost:8080/engine-rest", maxTasks = 5, waitTime= 100, lockTime = 600), name = "poller")
  pollActor ! Poll(topicName = "payment", worker)
  
  // waiting for end
  val input = readLine()
  println("")
  println("===================")
  println("shutting down......")
  println("")
  
  system.shutdown
}


package org.camunda.worker.akka/**
 * @author Philipp Ossler
 */

import akka.actor._
import scala.io.StdIn._
import org.camunda.worker.akka.PollActor.Poll
import org.camunda.worker.akka.sample.payment.PaymentWorker

/**
 * @author Philipp Ossler
 */
object Main extends App {
 
  // extract the host from arguments
  val host = if(args.size == 0) {
    "http://localhost:8080/engine-rest"
  } else { 
    args(0)
  }
  
  println("")
  println("starting...........")
  println("press ENTER to exit")
  println("===================")
  println("")
  
  // create actor system
  val system = ActorSystem("MyActorSystem")
  
  // create worker
  val worker = system.actorOf(PaymentWorker.props(), name = "payment-worker")
  
  // start polling
  val pollActor = system.actorOf(PollActor.props(hostAddress = host, maxTasks = 5, waitTime= 100, lockTime = 600))
  pollActor ! Poll(topicName = "orderProcess:payment", worker, variableNames = List("order"))
  
  // waiting for end
  val input = readLine()
  println("")
  println("===================")
  println("shutting down......")
  println("")
  
  system.shutdown
  // hard end - any thread till hanging
  System.exit(0)
}
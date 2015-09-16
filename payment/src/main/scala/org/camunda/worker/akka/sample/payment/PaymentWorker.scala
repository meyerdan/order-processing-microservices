package org.camunda.worker.akka.sample.payment

import org.camunda.worker.akka.client.LockedTask
import org.camunda.worker.akka.PollActor._
import akka.routing._
import akka.actor.Props
import scala.concurrent._
import ExecutionContext.Implicits.global
import org.camunda.worker.akka.client.VariableValue
import org.camunda.worker.akka.client.VariableValue.anyToVariableValue
import org.camunda.worker.akka.Worker
import scala.concurrent.duration._
import scala.util.Random

/**
 * sample worker
 */
class PaymentWorker(delay: Duration) extends Worker {

  def work(task: LockedTask): Future[Map[String, VariableValue]] = Future {
    
    val payment = calculatePayment
    
    Map("payment" -> payment)
  }

  private def calculatePayment = {
    // simulate calculation
    java.lang.Thread.sleep(delay.toMillis)
    // return result
    Random.nextInt(1000)
  }

}

object PaymentWorker {

  def props(delay: Duration = 250 millis): Props =
    Props(new PaymentWorker(delay))

}
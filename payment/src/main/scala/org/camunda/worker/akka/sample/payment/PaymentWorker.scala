package org.camunda.worker.akka.sample.payment

import org.camunda.worker.akka.client.LockedTask
import org.camunda.worker.akka.PollActor._
import akka.routing._
import akka.actor.Props
import org.camunda.worker.akka.client.VariableValue
import org.camunda.worker.akka.Worker
import scala.concurrent.duration._
import scala.util.Random
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

/**
 * sample worker
 */
class PaymentWorker(delay: Duration) extends Worker {

  // default formats for json
  implicit val formats = DefaultFormats
  
  def work(task: LockedTask): Map[String, VariableValue] = {
    
    val orderId: String = task.variable[JValue]("order") match {
      case Some(json) => (json \ "orderId").extract[String]
      case None       => "" // throw new IllegalArgumentException("order is not available")
    }
    
    val payment = calculatePayment(orderId)
    
    Map(s"payment-$orderId" -> payment)
  }

  private def calculatePayment(orderId: String) = {
    // simulate calculation
    java.lang.Thread.sleep(delay.toMillis)
    // return result
    Random.nextInt(1000)
  }

}

object PaymentWorker {

  def props(delay: Duration = 100 millis): Props =
    Props(new PaymentWorker(delay))

}
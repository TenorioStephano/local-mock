package br.com.tenoriostephano.localmock.service

import com.ibm.mq.MQException
import com.ibm.mq.jms.MQQueue
import org.slf4j.LoggerFactory
import org.springframework.jms.JmsException
import org.springframework.stereotype.Service
import java.lang.Exception
import javax.jms.*

@Service
class MockMQService {

    private val log = LoggerFactory.getLogger(javaClass)


    fun createProducer(session: Session,destination: Destination): MessageProducer? {
        try {
            return session.createProducer(destination)

        } catch (e: Exception){
            log.error("Error during the creation of the producer, {}", e)
        }
        return null
    }

    fun createTextMessage(session: Session,textMessage: String): TextMessage {
        return session.createTextMessage(textMessage)
    }

    fun sendMessage(producer: MessageProducer?, message: Message){
        try {
            producer?.send(message)
        }catch (e: Exception){
            log.error("Error during the creation or sending a message, by theses causes: {0}",e)
        }
    }

    fun getDestination(message: Message): MQQueue? {
        if(!existsDestination(message.jmsReplyTo)){
            return null
        }
        return message.jmsReplyTo as MQQueue
    }

    fun existsDestination(destination: Destination?): Boolean{
        return destination != null && destination is MQQueue
    }
}
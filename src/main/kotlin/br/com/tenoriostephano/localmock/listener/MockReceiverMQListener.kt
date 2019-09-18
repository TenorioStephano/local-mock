package br.com.tenoriostephano.localmock.listener

import br.com.tenoriostephano.localmock.exception.NotFoundException
import br.com.tenoriostephano.localmock.service.MockMQService
import br.com.tenoriostephano.localmock.service.MockService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.listener.SessionAwareMessageListener
import org.springframework.stereotype.Component
import javax.jms.Message
import javax.jms.Session

@Component
class MockReceiverMQListener (@Autowired private val mqService: MockMQService,
                              @Autowired private val service: MockService)
    : SessionAwareMessageListener<Message> {

    private val log = LoggerFactory.getLogger(javaClass)

    @JmsListener(destination = "\${mock.queue.name}")
    override fun onMessage(message: Message, session: Session) {
        log.debug("Receiving message...")
        val destination = mqService.getDestination(message)
        if(destination == null){
            log.debug("Ignoring message, reply to is null!")
            return
        }
        log.debug("The destination queue is {}", destination.queueName)

        try {
            mqService.sendMessage(mqService.createProducer(session,destination),
                    mqService.createTextMessage(session,
                            service.getResponseFromIdentification(destination.baseQueueName)))
            log.info("The message was replied!")
        } catch (e: NotFoundException){
            log.warn("The mock for destination (${destination.baseQueueName}) not found!")
        }


    }


}
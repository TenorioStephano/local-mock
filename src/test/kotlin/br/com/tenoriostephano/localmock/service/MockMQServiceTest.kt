package br.com.tenoriostephano.localmock.service

import br.com.tenoriostephano.localmock.configuration.AbstractMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import javax.jms.Queue
import javax.jms.Session
import javax.jms.TextMessage

class MockMQServiceTest : AbstractMockTest() {

    @Autowired
    private lateinit var service: MockMQService

    @Autowired
    private lateinit var template: JmsTemplate

    private lateinit var session: Session

    private val queueExample = "DEV.QUEUE.1"

    @Test
    override fun contextLoads() {
        assertThat(service)
                .isNotNull
    }
    @Before
    fun setUp() {
        session = createMQSession()
    }

    @After
    fun tearDown() {
        session.close()
    }

    @Test
    fun createProducer_sendSessionAndDestinationValid_returnProducer() {
        val producer = service.createProducer(session
                , session.createQueue(queueExample))
        assertThat(producer)
                .isNotNull
        assertThat(producer?.destination)
                .isNotNull
                .isInstanceOf(Queue::class.java)
        assertThat((producer?.destination as Queue).queueName)
                .contains(queueExample)
    }

    @Test
    fun createProducer_sendSessionAndDestinationInvalid_returnNull() {
        val producer = service.createProducer(session
                , session.createQueue("INVALID.QUEUE"))
        assertThat(producer)
                .isNull()
    }

    @Test
    fun createTextMessage_sendMessage_returnMessage() {
        val message = service.createTextMessage(session, "Text Message")
        assertThat(message)
                .isNotNull
        assertThat(message.text)
                .isNotNull()
                .isEqualTo("Text Message")
    }

    @Test
    fun sendMessage_sendProducerValid_returnMessageSent() {
        val queue = "DEV.QUEUE.2"
        val destination = session.createQueue(queue)
        val producer = session.createProducer(destination)
        val messageText = "Message Text"
        val textMessage = session.createTextMessage(messageText)

        service.sendMessage(producer,textMessage)

        template.receiveTimeout = 5000
        val received = template.receive(destination)

        assertThat(received)
                .isNotNull
                .isInstanceOf(TextMessage::class.java)
        assertThat((received as TextMessage).text)
                .isEqualTo(messageText)
    }

    @Test
    fun sendMessage_sendProducerInvalid_returnNull() {
        val destination = session.createQueue(queueExample)
        val producer = session.createProducer(null)
        val messageText = "Message Text"
        val textMessage = session.createTextMessage(messageText)

        service.sendMessage(producer,textMessage)

        template.receiveTimeout = 5000
        val received = template.receive(destination)

        assertThat(received)
                .isNull()
    }

    @Test
    fun getDestination_sendMessageWithoutReplyTo_returnNull() {
        val messageText = "Message Text"
        val textMessage = session.createTextMessage(messageText)
        val destination = service.getDestination(textMessage)
        assertThat(destination)
                .isNull()
    }

    @Test
    fun getDestination_sendMessageWithReplyTo_returnDestinationOfReplyTo() {
        val messageText = "Message Text"
        val queueReplyTo = "DEV.QUEUE.2"
        val textMessage = session.createTextMessage(messageText)
        val replyTo = session.createQueue(queueReplyTo)
        textMessage.jmsReplyTo = replyTo
        val destination = service.getDestination(textMessage)
        assertThat(destination)
                .isNotNull
        assertThat(destination?.baseQueueName)
                .isEqualTo(queueReplyTo)
    }

    @Test
    fun existsDestination_sendDestination_returnTrue() {
        val existsDestination = service.existsDestination(session.createQueue(queueExample))
        assertThat(existsDestination)
                .isNotNull()
                .isTrue()
    }

    @Test
    fun existsDestination_sendNull_returnFalse() {
        val existsDestination = service.existsDestination(null)
        assertThat(existsDestination)
                .isNotNull()
                .isFalse()
    }
}
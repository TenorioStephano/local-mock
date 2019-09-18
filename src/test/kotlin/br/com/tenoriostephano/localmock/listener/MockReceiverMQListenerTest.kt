package br.com.tenoriostephano.localmock.listener

import br.com.tenoriostephano.localmock.configuration.AbstractMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import java.io.File
import javax.jms.Destination
import javax.jms.Session
import javax.jms.TextMessage

class MockReceiverMQListenerTest : AbstractMockTest() {

    @Autowired
    private lateinit var receiver: MockReceiverMQListener

    @Autowired
    private lateinit var template: JmsTemplate

    private val contentFile = "Content of file test"

    private lateinit var session: Session

    private lateinit var destinationIn: Destination

    private lateinit var destinationOut: Destination

    private lateinit var file: File

    @Test
    override fun contextLoads() {
        assertThat(receiver).isNotNull
    }

    @Before
    fun setUp() {
        session = createMQSession()

        destinationIn = session.createQueue("DEV.QUEUE.1")
        destinationOut = session.createQueue("DEV.QUEUE.2")

        file = File("testMQ.txt")
        file.writeText(contentFile)
    }

    @After
    fun tearDown() {
        session.close()
        file.delete()
    }

    @Test
    fun onMessage_sendWithReplyToAndExistMock_returnContentMockFile() {
        val producer = session.createProducer(destinationIn)
        val createTextMessage = session.createTextMessage()
        createTextMessage.jmsReplyTo = destinationOut
        producer.send(createTextMessage)

        template.receiveTimeout = 2000
        val receive = template.receive(destinationOut)

        println("Receive: ${receive?.getBody(String::class.java)}")

        assertThat(receive)
                .isNotNull
                .isInstanceOf(TextMessage::class.java)
        assertThat((receive as TextMessage).text)
                .isNotNull()
                .isEqualTo(contentFile)
    }

    @Test
    fun onMessage_sendWithReplyToAndNotExistsMock_returnNull() {
        val producer = session.createProducer(destinationIn)
        val createTextMessage = session.createTextMessage()

        createTextMessage.jmsReplyTo = session.createQueue("DEV.QUEUE.3")
        producer.send(createTextMessage)

        template.receiveTimeout = 2000
        val receive = template.receive(destinationOut)

        assertThat(receive).isNull()
    }

    @Test
    fun onMessage_sendWithoutReplyTo_returnNull() {
        val producer = session.createProducer(destinationIn)
        val createTextMessage = session.createTextMessage()
        producer.send(createTextMessage)

        template.receiveTimeout = 2000
        val receive = template.receive(destinationOut)

        assertThat(receive).isNull()
    }
}
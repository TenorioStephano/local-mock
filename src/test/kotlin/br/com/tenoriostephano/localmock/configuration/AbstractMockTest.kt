package br.com.tenoriostephano.localmock.configuration

import com.ibm.mq.jms.MQConnectionFactory
import com.ibm.msg.client.wmq.WMQConstants
import com.ibm.msg.client.wmq.compat.jms.internal.JMSC
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import javax.jms.Session

@RunWith(SpringJUnit4ClassRunner::class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
abstract class AbstractMockTest : ContextLoadsTest {

    @Autowired
    private lateinit var env: Environment

    fun createMQSession(): Session {
        val connectionFactory = MQConnectionFactory()
        val hostAndPort = env.getProperty("ibm.mq.connName")
        connectionFactory.hostName = hostAndPort?.split("(")?.get(0)
        connectionFactory.port = hostAndPort?.split("(")?.get(1)?.replace(")","")?.toIntOrNull()!!
        connectionFactory.transportType = JMSC.MQJMS_TP_CLIENT_MQ_TCPIP
        connectionFactory.queueManager = env.getProperty("ibm.mq.queueManager")
        connectionFactory.channel = env.getProperty("ibm.mq.channel")
        connectionFactory.setStringProperty(WMQConstants.USERID,env.getProperty("ibm.mq.user"))
        connectionFactory.setStringProperty(WMQConstants.PASSWORD,env.getProperty("ibm.mq.password"))
        val connection = connectionFactory.createConnection()
        return connection.createSession()
    }

}
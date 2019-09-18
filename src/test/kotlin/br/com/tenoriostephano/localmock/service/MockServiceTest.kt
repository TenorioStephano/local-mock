package br.com.tenoriostephano.localmock.service

import br.com.tenoriostephano.localmock.configuration.AbstractMockTest
import br.com.tenoriostephano.localmock.entity.Mock
import br.com.tenoriostephano.localmock.exception.NotFoundException
import br.com.tenoriostephano.localmock.repository.MockRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.TransactionSystemException
import java.io.File

@TestPropertySource(properties = ["context/anotherContext=testContext.txt",
    "contextFileNotFound=notIsFile"])
class MockServiceTest : AbstractMockTest() {

    @Autowired
    private lateinit var service: MockService

    @Autowired
    private lateinit var repository: MockRepository

    private lateinit var mockToInsert: Mock

    private val context: String = "context/anotherContext"

    private lateinit var file: File

    @Test
    override fun contextLoads() {
        assertThat(service).isNotNull
    }

    @Before
    fun setUp() {
        mockToInsert = Mock(context, "value")
        repository.save(mockToInsert)
        file = File("testContext.txt")
        file.writeText("test Context Test")
    }

    @After
    fun tearDown() {
        file.delete()
    }

    @Test
    fun getResponseFromIdentification_sendContext_returnTextFromIdentification() {
        val textFromIdentification = service.getResponseFromIdentification(context)
        val mock = repository.findFirstByContextOrderByChangeDateDesc(context)

        assertThat(mock)
                .isNotNull
        assertThat(textFromIdentification)
                .isNotNull()
                .isEqualTo(mock?.value)
    }

    @Test(expected = NotFoundException::class)
    fun getResponseFromIdentification_sendContextButNotIsFile_expectNotFoundException() {
        service.getResponseFromIdentification("contextFileNotFound")
    }

    @Test(expected = NotFoundException::class)
    fun getResponseFromIdentification_sendContextEmpty_expectNotFoundException() {
        service.getResponseFromIdentification("")
    }

    @Test
    fun save_sendWithContext_returnMockWithContext() {
        service.save(mockToInsert)
        val mock = repository.findFirstByContextOrderByChangeDateDesc(context)
        Assertions.assertThat(mock)
                .isNotNull
                .isEqualToIgnoringGivenFields(mockToInsert,"changeDate")
    }

    @Test(expected = TransactionSystemException::class)
    fun save_sendWithContextEmpty_expectException(){
        service.save(Mock("", ""))
    }

    @Test
    fun findByContext_sendContext_returnMock() {
        val mockFound = service.findByContext(context)
        assertThat(mockFound)
                .isNotNull
                .isEqualToIgnoringGivenFields(mockToInsert,"changeDate")
    }

    @Test
    fun findByContext_sendContextEmpty_returnNull() {
        val mockFound = service.findByContext("")
        assertThat(mockFound)
                .isNull()
    }

}

package br.com.tenoriostephano.localmock.repository

import br.com.tenoriostephano.localmock.configuration.AbstractMockTest
import br.com.tenoriostephano.localmock.entity.Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MockRepositoryTest : AbstractMockTest() {

    @Autowired
    private lateinit var repository: MockRepository

    @Test
    override fun contextLoads() {
        assertThat(repository).isNotNull
    }

    @Test
    fun findFirstByContextOrderByChangeDateDesc_sendInvalidContext_thenReturnNull() {
        val mock = repository.findFirstByContextOrderByChangeDateDesc("aa")
        assertThat(mock).isNull()
    }

    @Test
    fun findFirstByContextOrderByChangeDateDesc_sendNotEmpty_thenReturnNotEmpty(){
        val context = "context"
        val mockToInsert = Mock(context, "value")
        repository.save(mockToInsert)
        repository.flush()
        val mock = repository.findFirstByContextOrderByChangeDateDesc(context)
        assertThat(mock)
                .isNotNull
                .isEqualToIgnoringGivenFields(mockToInsert,"changeDate")
    }

}
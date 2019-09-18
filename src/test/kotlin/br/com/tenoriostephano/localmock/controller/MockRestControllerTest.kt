package br.com.tenoriostephano.localmock.controller

import br.com.tenoriostephano.localmock.configuration.AbstractMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.io.File

@TestPropertySource(properties = ["context/valid=testContext.txt"])
class MockRestControllerTest : AbstractMockTest(){

    @Autowired
    private lateinit var controller : MockController

    @Autowired
    private lateinit var mvc: MockMvc

    private lateinit var file: File

    @Before
    fun setUp() {
        file = File("testContext.txt")
        file.writeText("test Context Test")
    }

    @After
    fun tearDown() {
        file.delete()
    }

    @Test
    override fun contextLoads() {
        assertThat(controller).isNotNull
    }

    @Test
    fun mock_sendContextNull_expectNotFound() {
        mvc.perform(get("/mock")).andExpect(status().isNotFound)
    }

    @Test
    fun mock_sendContextEmpty_expectNotFound() {
        mvc.perform(get("/mock?contextPath=")).andExpect(status().isNotFound)
    }

    @Test
    fun mock_sendContextInvalidNotBeginsSlash_expectNotFound() {
        mvc.perform(get("/mock?contextPath=context/invalid")).andExpect(status().isNotFound)
    }

    @Test
    fun mock_sendContextInvalidBeginsSlash_expectNotFound() {
        mvc.perform(get("/mock?contextPath=/context/invalid")).andExpect(status().isNotFound)
    }

    @Test
    fun mock_sendContextValidNotBeginsSlash_returnMockValue() {
        val response = mvc.perform(get("/mock?contextPath=context/valid")).andExpect(status().isOk).andReturn().response
        assertThat(response).isNotNull
        assertThat(response.contentAsString).isNotNull()
    }

    @Test
    fun mock_sendContextValidBeginsSlash_returnMockValue() {
        val response = mvc.perform(get("/mock?contextPath=/context/valid")).andExpect(status().isOk).andReturn().response
        assertThat(response).isNotNull
        assertThat(response.contentAsString).isNotNull()
    }

}
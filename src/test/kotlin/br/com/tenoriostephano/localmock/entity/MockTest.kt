package br.com.tenoriostephano.localmock.entity

import br.com.tenoriostephano.localmock.configuration.AbstractMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class MockTest : AbstractMockTest(){

    val mock: Mock = Mock("","")

    @Test
    override fun contextLoads() {
        assertThat(mock).isNotNull
    }

    @Test
    fun getSetId(){
        mock.id = 10
        assertThat(mock).isNotNull
                .hasFieldOrPropertyWithValue("id",10)
    }

    @Test
    fun getSetContext(){
        val context = "ContextTest"
        val mockTest = Mock(context,"")
        assertThat(mockTest).isNotNull
                .hasFieldOrPropertyWithValue("context",context)
    }

    @Test
    fun getSetValue(){
        val value = "valueTest"
        val mockTest = Mock("",value)
        assertThat(mockTest).isNotNull
                .hasFieldOrPropertyWithValue("value",value)
    }

    @Test
    fun getChangeDate(){
        assertThat(mock).isNotNull
        assertThat(mock.changeDate).isNotNull()
    }

    @Test
    fun getSetChangeDate(){
        val mockTest = Mock("","")
        val now = Calendar.getInstance()
        mockTest.changeDate = now.time
        assertThat(mockTest).isNotNull
                .hasFieldOrPropertyWithValue("changeDate",now.time)
    }

}
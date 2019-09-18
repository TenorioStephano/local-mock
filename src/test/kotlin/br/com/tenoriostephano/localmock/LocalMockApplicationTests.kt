package br.com.tenoriostephano.localmock

import br.com.tenoriostephano.localmock.configuration.AbstractMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class LocalMockApplicationTests() : AbstractMockTest() {

	@Autowired
	private lateinit var application: LocalMockApplication

	@Test
	override fun contextLoads() {
		assertThat(application).isNotNull
	}

	@Test
	fun main_sendArgsEmpty_returnContext() {
		val args :Array<String> = arrayOf()
		val context = main(args)

		assertThat(context)
				.isNotNull
		context.close()
	}
}

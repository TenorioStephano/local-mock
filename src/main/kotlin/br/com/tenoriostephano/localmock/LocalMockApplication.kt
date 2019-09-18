package br.com.tenoriostephano.localmock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.jms.annotation.EnableJms

@SpringBootApplication
@EnableJms
class LocalMockApplication

fun main(args: Array<String>): ConfigurableApplicationContext {
	return runApplication<LocalMockApplication>(*args)
}








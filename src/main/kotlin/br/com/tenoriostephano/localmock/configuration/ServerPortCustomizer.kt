package br.com.tenoriostephano.localmock.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component

@Configuration
class ServerPortCustomizer(@Autowired
                           val env: Environment) : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    override fun customize(factory: ConfigurableWebServerFactory?) {
        var port = env.get("port")
        if(port.isNullOrBlank()){
            port = "8081"
        }
        factory?.setPort(port?.toInt()!!)
    }
}
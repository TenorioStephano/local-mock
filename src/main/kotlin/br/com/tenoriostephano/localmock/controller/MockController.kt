package br.com.tenoriostephano.localmock.controller

import br.com.tenoriostephano.localmock.exception.NotFoundException
import br.com.tenoriostephano.localmock.service.MockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MockController(@Autowired private val service: MockService) {

    private val context = "/"

    @RequestMapping("/mock")
    fun mock(@RequestParam("contextPath") contextPath:String? ): String {
        contextPath ?: throw NotFoundException()

        return service.getResponseFromIdentification(contextPath
                .takeIf { it.startsWith(context) }
                .let { it?.replaceFirst(context, "") } ?: contextPath)
    }
}
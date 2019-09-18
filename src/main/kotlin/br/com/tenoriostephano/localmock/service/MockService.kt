package br.com.tenoriostephano.localmock.service

import br.com.tenoriostephano.localmock.entity.Mock
import br.com.tenoriostephano.localmock.exception.NotFoundException
import br.com.tenoriostephano.localmock.repository.MockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.io.File

@Service
class MockService (
        @Autowired private val repository: MockRepository,
        @Autowired private val env: Environment){

    fun getResponseFromIdentification(id: String): String {
        val textFromFile = getTextFromFile(getFileFromProperty(id))
        saveMockWhenHasChanged(findByContext(id),textFromFile,id)
        return textFromFile
    }

    private fun getFileFromProperty(id: String): File {
//        val fileName: String? = env.getProperty(id)
//        fileName ?: throw NotFoundException()
//
//        return File(fileName)
        val fileName: String? = env.getProperty(id)
        if (fileName.isNullOrBlank()){
            throw NotFoundException()
        }
        return File(fileName!!)
    }

    private fun getTextFromFile(fileFound: File): String {
        if(!fileFound.isFile){
            throw NotFoundException()
        }

        return fileFound.readText()
    }

    private fun saveMockWhenHasChanged(lastMockIncluded: Mock?, textFromFile : String, id: String){
        if(textFromFile != lastMockIncluded?.value){
            save(Mock(id, textFromFile))
        }
    }

    fun save(mock: Mock) {
        repository.save(mock)
    }

    fun findByContext(context: String): Mock? {
        return repository.findFirstByContextOrderByChangeDateDesc(context)
    }
}
package br.com.tenoriostephano.localmock.repository

import br.com.tenoriostephano.localmock.entity.Mock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MockRepository : JpaRepository<Mock,Int> {
    fun findFirstByContextOrderByChangeDateDesc(context: String): Mock?
}
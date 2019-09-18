package br.com.tenoriostephano.localmock.entity

import org.jetbrains.annotations.NotNull
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Mock (context: String,value: String) {

    @Id
    @NotNull
    @GeneratedValue
    var id: Int = 0

    @NotNull
    @NotBlank
    val context = context

    @Lob
    @Column( length = 1000000 )
    val value = value

    @Temporal(TemporalType.TIMESTAMP)
    var changeDate: Date = Date()
}
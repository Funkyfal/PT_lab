package org.example.spring_boot

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
data class Client(
    @Column(name = "name_of_a_client")
    var name_of_a_client: String,
    var email: String,
    var phone: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var clientID: Long? = null
){
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "client")
    @JsonIgnore
    var tours = mutableListOf<Tour>()
}

@Entity
data class Tour(
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "clientid")
    val client: Client? = null,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val tour_id: Long? = null,
    var price: Double,
    var destination: String,
    var availability: Boolean
)


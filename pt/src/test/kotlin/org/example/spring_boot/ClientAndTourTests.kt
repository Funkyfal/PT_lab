package org.example.spring_boot

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test

@DataJpaTest
class ClientAndTourTests {

    @Autowired
    lateinit var entityManager: jakarta.persistence.EntityManager

    @Test
    fun `should save and load Client`() {
        val client = Client(
            name_of_a_client = "John Doe",
            email = "johndoe@example.com",
            phone = "1234567890"
        )
        entityManager.persist(client)
        entityManager.flush()

        val foundClient = entityManager.find(Client::class.java, client.clientID)
        assertNotNull(foundClient)
        assertEquals(client.name_of_a_client, foundClient.name_of_a_client)
        assertEquals(client.email, foundClient.email)
        assertEquals(client.phone, foundClient.phone)
    }

    @Test
    fun `should save and load Tour with Client`() {
        val client = Client(
            name_of_a_client = "Jane Doe",
            email = "janedoe@example.com",
            phone = "0987654321"
        )
        entityManager.persist(client)

        val tour = Tour(
            client = client,
            price = 1500.0,
            destination = "Hawaii",
            availability = 10
        )
        entityManager.persist(tour)
        entityManager.flush()

        val foundTour = entityManager.find(Tour::class.java, tour.tour_id)
        assertNotNull(foundTour)
        assertEquals(tour.price, foundTour.price)
        assertEquals(tour.destination, foundTour.destination)
        assertEquals(tour.availability, foundTour.availability)
        assertNotNull(foundTour.client)
        assertEquals(client.clientID, foundTour.client?.clientID)
    }

    @Test
    fun `should handle multiple Tours for a Client`() {
        val client = Client(
            name_of_a_client = "Alex Smith",
            email = "alexsmith@example.com",
            phone = "1122334455"
        )
        entityManager.persist(client)

        val tour1 = Tour(
            client = client,
            price = 1200.0,
            destination = "Paris",
            availability = 5
        )
        val tour2 = Tour(
            client = client,
            price = 1800.0,
            destination = "Maldives",
            availability = 2
        )

        client.tours.add(tour1)
        client.tours.add(tour2)

        entityManager.persist(tour1)
        entityManager.persist(tour2)
        entityManager.flush()

        val foundClient = entityManager.find(Client::class.java, client.clientID)
        assertNotNull(foundClient)
        assertEquals(2, foundClient.tours.size)
    }

}
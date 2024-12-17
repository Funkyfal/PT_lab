package org.example.spring_boot

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class RepositoryTests {

    @Autowired
    lateinit var clientRepository: ClientRepository

    @Autowired
    lateinit var tourRepository: TourRepository

    @Test
    fun `should save and retrieve Client`() {
        val client = Client(
            name_of_a_client = "John Doe",
            email = "john.doe@example.com",
            phone = "123456789"
        )
        val savedClient = clientRepository.save(client)

        val foundClient = clientRepository.findById(savedClient.clientID!!).orElse(null)
        assertNotNull(foundClient)
        assertEquals("John Doe", foundClient.name_of_a_client)
        assertEquals("john.doe@example.com", foundClient.email)
    }

    @Test
    fun `should save and retrieve Tour`() {
        val client = Client(
            name_of_a_client = "Jane Doe",
            email = "jane.doe@example.com",
            phone = "987654321"
        )
        val savedClient = clientRepository.save(client)

        val tour = Tour(
            client = savedClient,
            price = 1500.0,
            destination = "Hawaii",
            availability = 10
        )
        val savedTour = tourRepository.save(tour)

        val foundTour = tourRepository.findById(savedTour.tour_id!!).orElse(null)
        assertNotNull(foundTour)
        assertEquals(1500.0, foundTour.price)
        assertEquals("Hawaii", foundTour.destination)
        assertEquals(savedClient.clientID, foundTour.client?.clientID)
    }

    @Test
    fun `should find all Tours for a Client`() {
        val client = Client(
            name_of_a_client = "Alex Johnson",
            email = "alex.johnson@example.com",
            phone = "555123456"
        )
        val savedClient = clientRepository.save(client)

        val tour1 = Tour(client = savedClient, price = 2000.0, destination = "Bali", availability = 5)
        val tour2 = Tour(client = savedClient, price = 3000.0, destination = "Rome", availability = 2)

        tourRepository.save(tour1)
        tourRepository.save(tour2)

        val tours = tourRepository.findAll().filter { it.client?.clientID == savedClient.clientID }
        assertEquals(2, tours.size)
        assertTrue(tours.any { it.destination == "Bali" })
        assertTrue(tours.any { it.destination == "Rome" })
    }
}

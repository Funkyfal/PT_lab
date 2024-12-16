package org.example.spring_boot

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

//сделать availability
@Service
class ClientService(
    private val clientRepository: ClientRepository
) {
    fun getAllClients(): MutableList<Client> {
        return clientRepository.findAll()
    }

    fun getClientByID(id: Long?): Client? {
        return clientRepository.findByIdOrNull(id)
    }

    fun createClient(client: Client) {
        clientRepository.save(client)
    }

    fun updateClient(id: Long, updatedClient: Client): Client? {
        val existingClient = clientRepository.findByIdOrNull(id)
        return if (existingClient != null) {
            val updated = existingClient.copy(
                name_of_a_client = updatedClient.name_of_a_client,
                email = updatedClient.email,
                phone = updatedClient.phone
            )
            clientRepository.save(updated)
        } else {
            null
        }
    }

    fun getClientTours(id: Long?): List<Tour> {
        val client = clientRepository.findByIdOrNull(id)
        return client?.tours ?: emptyList()
    }
}

@Service
class TourService(
    private val tourRepository: TourRepository,
    private val clientRepository: ClientRepository
) {
    fun getAllTours(): MutableList<Tour> {
        return tourRepository.findAll()
    }

    fun getTourByID(id: Long?): Tour? {
        return tourRepository.findByIdOrNull(id)
    }

    fun createTour(tour: Tour): Tour {
        return tourRepository.save(tour)
    }

    fun updateTour(tourId: Long, updatedTour: Tour): Tour? {
        val tour = tourRepository.findByIdOrNull(tourId)
        return if (tour != null) {
            val updated = tour.copy(
                destination = updatedTour.destination,
                price = updatedTour.price,
                availability = updatedTour.availability
            )
            tourRepository.save(updated)
        } else {
            null
        }
    }

    fun bookTour(clientId: Long, tourId: Long): Tour? {
        val client = clientRepository.findByIdOrNull(clientId)
        val tour = tourRepository.findByIdOrNull(tourId)

        return if (client != null && tour != null && tour.availability) {
            val updatedTour = tour.copy(client = client)
            updatedTour.availability = false
            tourRepository.save(updatedTour)
        } else {
            null
        }
    }
}
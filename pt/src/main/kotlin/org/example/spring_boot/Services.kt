package org.example.spring_boot

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

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

    fun createTour(clientId: Long, tour: Tour): Tour? {
        val client = clientRepository.findByIdOrNull(clientId)
        return if (client != null) {
            val newTour = tour.copy(client = client) // Привязываем клиента к туру
            tourRepository.save(newTour)

        } else {
            null // Клиент с таким ID не найден
        }
    }

}
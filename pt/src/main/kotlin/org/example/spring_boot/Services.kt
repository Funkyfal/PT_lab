package org.example.spring_boot

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val sessionLoggerService: SessionLoggerService
) {

    fun getAllClients(): MutableList<Client> {
        sessionLoggerService.logAction("Просмотрены все клиенты")
        return clientRepository.findAll()
    }

    fun getClientByID(id: Long?): Client? {
        val client = clientRepository.findByIdOrNull(id)
        client?.let {
            sessionLoggerService.logAction("Просмотр клиента", it.name_of_a_client)
        }
        return client
    }

    fun createClient(client: Client) {
        clientRepository.save(client)
        sessionLoggerService.logAction("Создан новый клиент", client.name_of_a_client)
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
            sessionLoggerService.logAction("Обновлены данные клиента", updated.name_of_a_client)
            updated
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
    private val clientRepository: ClientRepository,
    private val sessionLoggerService: SessionLoggerService
) {

    fun getAllTours(): MutableList<Tour> {
        sessionLoggerService.logAction("Просмотрены все туры")
        return tourRepository.findAll()
    }

    fun getTourByID(id: Long?): Tour? {
        val tour = tourRepository.findByIdOrNull(id)
        tour?.let {
            sessionLoggerService.logAction("Просмотр тура", tour.destination)
        }
        return tour
    }

    fun createTour(tour: Tour): Tour {
        val newTour = tourRepository.save(tour)
        sessionLoggerService.logAction("Создан новый тур", tour.destination)
        return newTour
    }

    fun updateTour(tourId: Long, updatedTour: Tour): Tour? {
        val existingTour = tourRepository.findByIdOrNull(tourId)
        return if (existingTour != null) {
            val updated = existingTour.copy(
                destination = updatedTour.destination,
                price = updatedTour.price,
                availability = updatedTour.availability
            )
            tourRepository.save(updated)
            sessionLoggerService.logAction("Обновлен тур", updated.destination)
            updated
        } else {
            null
        }
    }

    fun bookTour(clientId: Long, tourId: Long): Tour? {
        val client = clientRepository.findByIdOrNull(clientId)
        val tour = tourRepository.findByIdOrNull(tourId)

        return if (client != null && tour != null && tour.availability > 0) {
            val updatedTour = tour.copy(client = client)
            updatedTour.availability--
            tourRepository.save(updatedTour)

            sessionLoggerService.logAction("Клиент забронировал тур", client.name_of_a_client, tour.destination)

            updatedTour
        } else {
            null
        }
    }

    fun getClientsByTour(tourId: Long): List<Client> {
        val tour = tourRepository.findByIdOrNull(tourId)
        return tour?.client?.let { listOf(it) } ?: emptyList()
    }
}

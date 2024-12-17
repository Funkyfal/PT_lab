package org.example.spring_boot

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test

@SpringBootTest
class ClientServiceTests {
    @Mock
    private lateinit var clientRepository: ClientRepository
    @Mock
    private lateinit var sessionLoggerService: SessionLoggerService

    private val clientService: ClientService by lazy {
        ClientService(clientRepository, sessionLoggerService)
    }

    @Test
    fun `should return all clients`() {
        val clients = listOf(
            Client(name_of_a_client = "John", email = "john@example.com", phone = "123456"),
            Client(name_of_a_client = "Jane", email = "jane@example.com", phone = "987654")
        )

        `when`(clientRepository.findAll()).thenReturn(clients)

        val result = clientService.getAllClients()

        assertEquals(2, result.size)
        verify(sessionLoggerService).logAction("Просмотрены все клиенты")
    }

    @Test
    fun `should return client by ID`() {
        val client = Client(clientID = 1L, name_of_a_client = "John", email = "john@example.com", phone = "123456")

        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))

        val result = clientService.getClientByID(1L)

        assertNotNull(result)
        assertEquals("John", result?.name_of_a_client)
        verify(sessionLoggerService).logAction("Просмотр клиента", "John")
    }

    @Test
    fun `should create a new client`() {
        val client = Client(name_of_a_client = "John", email = "john@example.com", phone = "123456")

        clientService.createClient(client)

        verify(clientRepository).save(client)
        verify(sessionLoggerService).logAction("Создан новый клиент", "John")
    }

    @Test
    fun `should update an existing client`() {
        val existingClient = Client(clientID = 1L, name_of_a_client = "Old Name", email = "old@example.com", phone = "111111")
        val updatedClient = Client(name_of_a_client = "New Name", email = "new@example.com", phone = "222222")

        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient))

        val result = clientService.updateClient(1L, updatedClient)

        assertNotNull(result)
        assertEquals("New Name", result?.name_of_a_client)
        assertEquals("new@example.com", result?.email)
        verify(clientRepository).save(result!!)
        verify(sessionLoggerService).logAction("Обновлены данные клиента", "New Name")
    }

    @Test
    fun `should return all tours of a client`() {
        val tours = listOf(
            Tour(destination = "Paris", price = 1000.0, availability = 5),
            Tour(destination = "London", price = 800.0, availability = 2)
        )
        val client = Client(clientID = 1L, name_of_a_client = "John", email = "john@example.com", phone = "123456")
        client.tours = tours.toMutableList()

        // Возвращаем Optional вместо напрямую клиента
        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))

        val result = clientService.getClientTours(1L)

        assertEquals(2, result.size)
        assertTrue(result.any { it.destination == "Paris" })
        assertTrue(result.any { it.destination == "London" })
    }

}
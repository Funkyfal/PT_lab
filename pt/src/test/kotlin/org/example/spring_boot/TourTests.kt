package org.example.spring_boot

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional
import kotlin.test.Test

@SpringBootTest
class TourServiceTests {
    @Mock
    private lateinit var tourRepository: TourRepository

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var sessionLoggerService: SessionLoggerService

    private val tourService: TourService by lazy {
        TourService(tourRepository, clientRepository, sessionLoggerService)
    }

    @Test
    fun `should return all tours`() {
        val tours = listOf(
            Tour(destination = "Paris", price = 1000.0, availability = 5),
            Tour(destination = "London", price = 800.0, availability = 3)
        )

        `when`(tourRepository.findAll()).thenReturn(tours)

        val result = tourService.getAllTours()

        assertEquals(2, result.size)
        assertTrue(result.any { it.destination == "Paris" })
        verify(sessionLoggerService).logAction("Просмотрены все туры")
    }

    @Test
    fun `should return tour by ID`() {
        val tour = Tour(tour_id = 1L, destination = "Paris", price = 1000.0, availability = 5)

        // Мокируем findById для возврата Optional
        `when`(tourRepository.findById(1L)).thenReturn(Optional.of(tour))

        val result = tourService.getTourByID(1L)

        assertNotNull(result)
        assertEquals("Paris", result?.destination)
        verify(sessionLoggerService).logAction("Просмотр тура", "Paris")
    }

    @Test
    fun `should create a new tour`() {
        val tour = Tour(destination = "Rome", price = 1200.0, availability = 7)

        `when`(tourRepository.save(tour)).thenReturn(tour)

        val result = tourService.createTour(tour)

        assertNotNull(result)
        assertEquals("Rome", result.destination)
        verify(tourRepository).save(tour)
        verify(sessionLoggerService).logAction("Создан новый тур", "Rome")
    }

    @Test
    fun `should update an existing tour`() {
        val existingTour = Tour(tour_id = 1L, destination = "Paris", price = 1000.0, availability = 5)
        val updatedTour = Tour(destination = "Paris Updated", price = 1100.0, availability = 4)

        // Мокируем findById для возврата Optional
        `when`(tourRepository.findById(1L)).thenReturn(Optional.of(existingTour))
        `when`(tourRepository.save(any(Tour::class.java))).thenAnswer { it.arguments[0] }

        val result = tourService.updateTour(1L, updatedTour)

        assertNotNull(result)
        assertEquals("Paris Updated", result?.destination)
        assertEquals(1100.0, result?.price)
        verify(tourRepository).save(result!!)
        verify(sessionLoggerService).logAction("Обновлен тур", "Paris Updated")
    }

    @Test
    fun `should book a tour`() {
        val client = Client(clientID = 1L, name_of_a_client = "John", email = "john@example.com", phone = "123456")
        val tour = Tour(tour_id = 1L, destination = "Paris", price = 1000.0, availability = 5)

        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))
        `when`(tourRepository.findById(1L)).thenReturn(Optional.of(tour))
        `when`(tourRepository.save(any(Tour::class.java))).thenAnswer { it.arguments[0] }

        val result = tourService.bookTour(1L, 1L)

        assertNotNull(result)
        if (result != null) {
            assertEquals(4, result.availability)
        }
        if (result != null) {
            assertEquals(client, result.client)
        }
        if (result != null) {
            verify(tourRepository).save(result)
        }
        verify(sessionLoggerService).logAction("Клиент забронировал тур", "John", "Paris")
    }

    @Test
    fun `should not book a tour if no availability`() {
        val client = Client(clientID = 1L, name_of_a_client = "John", email = "john@example.com", phone = "123456")
        val tour = Tour(tour_id = 1L, destination = "Paris", price = 1000.0, availability = 0)

        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))
        `when`(tourRepository.findById(1L)).thenReturn(Optional.of(tour))

        val result = tourService.bookTour(1L, 1L)

        assertNull(result)
        verify(tourRepository, never()).save(any(Tour::class.java))
        verify(sessionLoggerService, never()).logAction(anyString(), anyString(), anyString())
    }

    @Test
    fun `should return all clients of a tour`() {
        val client = Client(clientID = 1L, name_of_a_client = "John", email = "john@example.com", phone = "123456")
        val tour = Tour(tour_id = 1L, destination = "Paris", price = 1000.0, availability = 5, client = client)

        `when`(tourRepository.findById(1L)).thenReturn(Optional.of(tour))

        val result = tourService.getClientsByTour(1L)

        assertEquals(1, result.size)
        assertTrue(result.any { it.name_of_a_client == "John" })
    }
}
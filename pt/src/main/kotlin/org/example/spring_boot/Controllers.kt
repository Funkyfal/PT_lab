package org.example.spring_boot

import ch.qos.logback.core.model.Model
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/clients")
class ClientController(
    private val clientService: ClientService
) {
    @GetMapping
    fun getAllClients(): MutableList<Client> {
        return clientService.getAllClients()
    }

    @GetMapping("/{id}")
    fun getClientById(@PathVariable id: Long?): Client? {
        return clientService.getClientByID(id)
    }

    @PostMapping()
    fun createClient(@RequestBody client: Client) {
        return clientService.createClient(client)
    }

    @PutMapping("/{id}")
    fun updateClient(@PathVariable id: Long, @RequestBody updatedClient: Client): Client? {
        return clientService.updateClient(id, updatedClient)
    }

    @GetMapping("/{id}/tours")
    fun getClientTours(@PathVariable id: Long?): List<Tour> {
        return clientService.getClientTours(id)
    }

    @GetMapping("/{clientId}/booked-tours")
    fun getBookedToursByClient(@PathVariable clientId: Long): List<Tour> {
        return clientService.getClientTours(clientId)
    }

}

@RestController
@RequestMapping("/tours")
class TourController(
    private val tourService: TourService
) {
    @GetMapping
    fun getAllTours(): MutableList<Tour> {
        return tourService.getAllTours()
    }

    @GetMapping("/{id}")
    fun getTourByID(@PathVariable id: Long?): Tour? {
        return tourService.getTourByID(id)
    }

    @PostMapping
    fun createTour(@RequestBody tour: Tour): Tour {
        return tourService.createTour(tour)
    }

    @PutMapping("/{tourId}")
    fun updateTour(
        @PathVariable tourId: Long,
        @RequestBody updatedTour: Tour
    ): Tour? {
        return tourService.updateTour(tourId, updatedTour)
    }

    @PostMapping("/book/{clientId}/{tourId}")
    fun bookTour(
        @PathVariable clientId: Long,
        @PathVariable tourId: Long
    ): Tour? {
        return tourService.bookTour(clientId, tourId)
    }

    @GetMapping("/{tourId}/clients")
    fun getClientsByTour(@PathVariable tourId: Long): List<Client> {
        return tourService.getClientsByTour(tourId)
    }

}

@Controller
class WebController {
    @GetMapping("/")
    fun index(model: Model): String {
        return "index"
    }

    @GetMapping("/book-tour")
    fun bookTourPage(model: Model): String {
        return "book-tour"
    }

    @GetMapping("/tour-client-view")
    fun tourClientView(model: Model): String {
        return "tour-client-view"
    }

}

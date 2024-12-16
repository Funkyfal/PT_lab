package org.example.spring_boot

import org.springframework.data.jpa.repository.JpaRepository

interface ClientRepository: JpaRepository<Client, Long>

interface TourRepository: JpaRepository<Tour, Long>
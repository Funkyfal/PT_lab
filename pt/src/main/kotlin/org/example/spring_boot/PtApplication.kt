package org.example.spring_boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PtApplication

fun main(args: Array<String>) {
	runApplication<PtApplication>(*args)
}

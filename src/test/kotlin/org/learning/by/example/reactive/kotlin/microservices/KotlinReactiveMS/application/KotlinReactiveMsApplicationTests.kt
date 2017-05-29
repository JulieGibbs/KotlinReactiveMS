package org.learning.by.example.reactive.kotlin.microservices.KotlinReactiveMS.application

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.learning.by.example.reactive.kotlin.microservices.KotlinReactiveMS.model.ErrorResponse
import org.learning.by.example.reactive.kotlin.microservices.KotlinReactiveMS.model.LocationRequest
import org.learning.by.example.reactive.kotlin.microservices.KotlinReactiveMS.model.LocationResponse
import org.learning.by.example.reactive.kotlin.microservices.KotlinReactiveMS.test.BasicIntegrationTest
import org.learning.by.example.reactive.kotlin.microservices.KotlinReactiveMS.test.isNull
import org.learning.by.example.reactive.kotlin.microservices.KotlinReactiveMS.test.tags.SystemTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus.NOT_FOUND


@SystemTest
@DisplayName("KotlinReactiveMsApplication System Tests")
private class KotlinReactiveMsApplicationTests : BasicIntegrationTest() {

    companion object {
        const val GOOGLE_ADDRESS = "1600 Amphitheatre Parkway, Mountain View, CA"
        const val API_LOCATION = "/api/location"
        const val API_BAD_URL = "/api/bad/url"
        const val NOT_FOUND_MESSAGE = "not found"
    }

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setup() = bindToPort(port)

    @Test
    fun getLocation() {
        val locationResponse = get(url = "${API_LOCATION}/${GOOGLE_ADDRESS}", type = LocationResponse::class)
        assert.that(locationResponse.geographicCoordinates, !isNull())
    }

    @Test
    fun getLocationNotFound() {
        val errorResponse = get(url = API_BAD_URL, httpStatus = NOT_FOUND, type = ErrorResponse::class)
        assert.that(errorResponse.message, equalTo(NOT_FOUND_MESSAGE))
    }


    @Test
    fun postLocation() {
        val locationResponse = post(url = API_LOCATION, value = LocationRequest(GOOGLE_ADDRESS),
                type = LocationResponse::class)

        assert.that(locationResponse.geographicCoordinates, !isNull())
    }

    @Test
    fun postLocationNotFound() {
        val errorResponse = post(url = API_BAD_URL, httpStatus = NOT_FOUND, value = LocationRequest(GOOGLE_ADDRESS),
                type = ErrorResponse::class)

        assert.that(errorResponse.message, equalTo(NOT_FOUND_MESSAGE))
    }

}

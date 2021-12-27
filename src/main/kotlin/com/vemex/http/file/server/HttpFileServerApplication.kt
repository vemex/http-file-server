package com.vemex.http.file.server

import com.vemex.http.file.server.properties.FileServerProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableWebFluxSecurity
@EnableConfigurationProperties(FileServerProperties::class)
class HttpFileServerApplication

fun main(args: Array<String>) {
    runApplication<HttpFileServerApplication>(*args)
}

package com.vemex.http.file.server

import com.vemex.http.file.server.properties.AuthType
import com.vemex.http.file.server.properties.FileServerProperties
import com.vemex.http.file.server.properties.UserProvider
import org.springframework.boot.autoconfigure.ldap.LdapProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.ldap.LdapUserServiceBeanDefinitionParser
import org.springframework.security.config.web.server.ServerHttpSecurity

import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono


/**
 *
 * @author wangweiwei
 * @date 2021/12/27 11:56 上午
 */
@Configuration
class WebfluxSecurityConfig(val fileServerProperties: FileServerProperties) {


    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        //logger.info("WebFlux Security begin")
//        var securityWebFilterBuilder = http
//            .authorizeExchange()
//            .pathMatchers("/**")
//            .authenticated()
//        if (fileServerProperties.authType == AuthType.BASIC) {
//            var httpBasic = securityWebFilterBuilder.and().httpBasic();
//            if (fileServerProperties.userProvider == UserProvider.LDAP) {
//                httpBasic.authenticationManager (LdapUserServiceBeanDefinitionParser)
//            }
//        }

        return http.authorizeExchange()
            .pathMatchers("/**")
            .authenticated()
            .and()
            .httpBasic()
            .authenticationManager {
                return@authenticationManager Mono.just(it);
            }
            .and()
            .build()
    }
}
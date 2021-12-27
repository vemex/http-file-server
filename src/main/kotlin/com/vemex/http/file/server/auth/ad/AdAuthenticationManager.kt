package com.vemex.http.file.server.auth.ad

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

/**
 *
 * @author wangweiwei
 * @date 2021/12/27 2:37 下午
 */
class AdAuthenticationManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        TODO("Not yet implemented")
    }
}
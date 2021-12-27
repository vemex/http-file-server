package com.vemex.http.file.server.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 *
 * @author wangweiwei
 * @date 2021/12/27 2:40 下午
 */
@ConfigurationProperties(prefix = "file.server")
class FileServerProperties {
    lateinit var authType: AuthType;
    lateinit var userProvider: UserProvider;
    lateinit var ldapConfig: LdapConfig;
}

class LdapConfig {
    lateinit var url: String;
    lateinit var user: String;
    lateinit var password: String;
    lateinit var baseDN: String;
    lateinit var searchDN: String;
    lateinit var groupDN: String;
}

enum class UserProvider {
    LDAP,
    DATABASE
}

enum class AuthType {
    BASIC,
    FORM
}
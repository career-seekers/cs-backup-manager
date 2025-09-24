package org.careerseekers.csbackupmanager.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "cloud.disk")
class YandexDiskProperties {
    lateinit var baseUrl: String
    lateinit var oauthToken: String
}
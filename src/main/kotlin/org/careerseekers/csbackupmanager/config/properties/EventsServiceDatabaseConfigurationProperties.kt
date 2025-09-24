package org.careerseekers.csbackupmanager.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "database.es-pg")
class EventsServiceDatabaseConfigurationProperties {
    lateinit var url: String
    lateinit var username: String
    lateinit var password: String
    lateinit var schema: String
    lateinit var host: String
    lateinit var port: String
    lateinit var containerName: String
    lateinit var backupName: String
    lateinit var dbName: String
}
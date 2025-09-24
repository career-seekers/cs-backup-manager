package org.careerseekers.csbackupmanager.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "database.us-pg")
class UserServiceDatabaseConfigurationProperties : IDatabaseProperties {
    lateinit var url: String
    override lateinit var username: String
    lateinit var password: String
    lateinit var schema: String
    lateinit var host: String
    lateinit var port: String
    override lateinit var containerName: String
    override lateinit var backupName: String
    override lateinit var dbName: String
}
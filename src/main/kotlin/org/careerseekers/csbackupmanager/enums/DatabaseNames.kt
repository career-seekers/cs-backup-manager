package org.careerseekers.csbackupmanager.enums

enum class DatabaseNames(val alias: String) {
    USERS_SERVICE_PG("us-postgres"),
    EVENTS_SERVICE_PG("es-postgres"),
    FILES_SERVICE_PG("fs-postgres");

    companion object {
        fun DatabaseNames.getAlias() = this.alias
    }
}
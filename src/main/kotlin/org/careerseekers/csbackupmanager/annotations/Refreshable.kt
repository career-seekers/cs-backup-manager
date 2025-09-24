package org.careerseekers.csbackupmanager.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Refreshable(val periodMillis: Long)
package org.careerseekers.csbackupmanager.config

import org.careerseekers.csbackupmanager.annotations.Refreshable
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Configuration
import kotlin.reflect.full.findAnnotation

@Configuration
class RefreshableClassesBeanPostProcessor : BeanPostProcessor {
    val refreshableBeans = mutableMapOf<String, RefreshableInfo>()

    data class RefreshableInfo(
        val beanClass: Class<*>,
        val refreshPeriod: Long,
        var lastRefreshTime: Long,
        var currentBeanInstance: Any
    )

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val clazz = bean::class
        val refreshable = clazz.findAnnotation<Refreshable>()
        refreshable?.let {
            refreshableBeans[beanName] = RefreshableInfo(
                beanClass = bean.javaClass,
                refreshPeriod = it.periodMillis,
                lastRefreshTime = System.currentTimeMillis(),
                currentBeanInstance = bean
            )
        }
        return bean
    }
}
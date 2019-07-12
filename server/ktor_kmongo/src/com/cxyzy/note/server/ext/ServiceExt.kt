package com.cxyzy.note.server.ext

import com.cxyzy.note.server.service.BaseService

import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.scope.ScopeInstance

/**
 * Ktor Koin extensions for Routing class
 *
 * @author Arnaud Giuliani
 * @author Laurent Baresse
 */

/**
 * inject lazily given dependency
 * @param name - bean name / optional
 * @param module - module path
 * @param parameters
 */
inline fun <reified T : Any> BaseService.inject(
    name: String = "",
    scope: ScopeInstance? = null,
    noinline parameters: ParametersDefinition? = null
) =
    lazy { get<T>(name, scope, parameters) }

/**
 * Retrieve given dependency for KoinComponent
 * @param name - bean name / optional
 * @param module - module path
 * @param parameters
 */
inline fun <reified T : Any> BaseService.get(
    name: String = "",
    scope: ScopeInstance? = null,
    noinline parameters: ParametersDefinition? = null
) =
    getKoin().get<T>(name, scope, parameters)

/**
 * Retrieve given property for KoinComponent
 * @param key - key property
 * throw MissingPropertyException if property is not found
 */
inline fun <reified T> BaseService.getProperty(key: String) =
    getKoin().getProperty<T>(key)

/**
 * Retrieve given property for KoinComponent
 * give a default value if property is missing
 *
 * @param key - key property
 * @param defaultValue - default value if property is missing
 *
 */
inline fun <reified T> BaseService.getProperty(key: String, defaultValue: T) =
    getKoin().getProperty(key) ?: defaultValue

/**
 * Help work on ModuleDefinition
 */
fun BaseService.getKoin() = GlobalContext.get().koin
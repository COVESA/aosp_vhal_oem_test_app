/*
 * Copyright (C) 2026 BMW Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package global.covesa.aosp.vhal.test.app

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KProperty0

fun property(property: KProperty0<Int>, units: String = "") = PropertyDefinition(
		type = Float::class,
		name = property.name,
		id = property.get(),
		unitsOrEnum = units)

fun property(property: KProperty0<Int>, enabled: String, disabled: String) = PropertyDefinition(
		type = Boolean::class,
		name = property.name,
		id = property.get(),
		unitsOrEnum = "${enabled}|${disabled}")


private val <T : Any> PropertyDefinition<T>.units: String?
	get() = if (type == Float::class) unitsOrEnum else null

private val <T : Any> PropertyDefinition<T>.options: List<String>
	get() = if (type == Boolean::class) unitsOrEnum.split("|") else emptyList()

fun <T : Any> PropertyDefinition<T>.format(value: T?): String = if (value == null) "---" else when (type) {
	Float::class -> listOfNotNull("%.1f".format(value), units).joinToString(" ")
	Boolean::class -> if (value as? Boolean == true)
		options.getOrNull(0) ?: "ON" else options.getOrNull(1) ?: "OFF"
	else -> value.toString()
}

fun <T> MutableStateFlow<T>.ensure(value: T) {
	if (this.value != value) this.value = value
}

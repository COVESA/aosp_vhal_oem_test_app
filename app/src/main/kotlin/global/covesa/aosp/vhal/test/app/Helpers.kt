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

import android.content.Context
import android.content.pm.PackageManager
import kotlin.reflect.KProperty0

val VEHICLE_PROPERTIES = mutableListOf<PropertyDefinition<*>>()

inline fun <reified T : Any> defineProperty(property: KProperty0<Int>,
											units: String,
											areaId: Int = 0,
											isWriteable: Boolean = false) = PropertyDefinition(
		id = property.get(),
		name = property.name,
		type = T::class,
		areaId = areaId,
		readPermission = "android.car.permission.oem.${property.name}_READ",
		writePermission = "android.car.permission.oem.${property.name}_WRITE".takeIf { isWriteable },
		unitsOrEnum = units)
		.also { VEHICLE_PROPERTIES.add(it) }
		.let { PropertyValue(definition = it) }

fun defineProperty(property: KProperty0<Int>,
				   enabled: String,
				   disabled: String,
				   areaId: Int = 0,
				   isWriteable: Boolean = true) = PropertyDefinition(
		id = property.get(),
		name = property.name,
		type = Boolean::class,
		areaId = areaId,
		readPermission = "android.car.permission.oem.${property.name}_READ",
		writePermission = "android.car.permission.oem.${property.name}_WRITE".takeIf { isWriteable },
		unitsOrEnum = "${enabled}|${disabled}")
		.also { VEHICLE_PROPERTIES.add(it) }
		.let { PropertyValue(definition = it) }

fun Context.isGranted(permission: String) =
	checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

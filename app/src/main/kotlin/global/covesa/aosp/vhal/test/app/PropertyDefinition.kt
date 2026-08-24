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

import kotlin.reflect.KClass

data class PropertyDefinition<T : Any>(val id: Int,
									   val name: String,
									   val type: KClass<T>,
									   val areaId: Int = 0,
									   val readPermission: String,
									   val writePermission: String?,
									   val unitsOrEnum: String) {

	val uid: String
		get() = "$id-$areaId"

	val permissions: List<String>
		get() = listOfNotNull(readPermission, writePermission)

	val units: String?
		get() = when (type) {
			Float::class, Int::class -> unitsOrEnum
			else -> null
		}

	val options: List<String>
		get() = if (type == Boolean::class) unitsOrEnum.split("|") else emptyList()
}

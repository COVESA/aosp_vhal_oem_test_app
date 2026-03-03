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
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.KClass

data class PropertyDefinition<T : Any>(val type: KClass<T>,
									   val name: String,
									   val id: Int,
									   val unitsOrEnum: String) {
	val mutableValueStateFlow = MutableStateFlow<T?>(null)
	val valueStateFlow = mutableValueStateFlow.asStateFlow()

	val mutableErrorStateFlow = MutableStateFlow(false)
	val errorStateFlow = mutableErrorStateFlow.asStateFlow()

}

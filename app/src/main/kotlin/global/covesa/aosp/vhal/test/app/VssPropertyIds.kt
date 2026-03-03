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

/**
 * List of vehicle property IDs.
 */
interface VssPropertyIds {
	companion object {
		const val AMBIENT_LIGHT: Int = 1092617646
		const val CABIN_SUNROOF_SHADE_IS_OPEN: Int = 1092616553
		const val CABIN_REAR_SHADE_IS_OPEN: Int = 1092616476
		const val ADAS_ABS_IS_ENABLED: Int = 1092618486
		const val ADAS_CRUISE_CONTROL_IS_ACTIVE: Int = 1092618441
	}
}

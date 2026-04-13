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

import android.car.oem.VehiclePropertyIdsOem

object VehicleProperty {

	val AMBIENT_LIGHT = defineProperty(
			VehiclePropertyIdsOem::AMBIENT_LIGHT, enabled = "On", disabled = "Off")

	val ADAS_ABS_IS_ENABLED = defineProperty(
			VehiclePropertyIdsOem::ADAS_ABS_IS_ENABLED, enabled = "Enabled", disabled = "Disabled")

	val ADAS_CRUISE_CONTROL_IS_ACTIVE = defineProperty(
			VehiclePropertyIdsOem::ADAS_CRUISE_CONTROL_IS_ACTIVE,
			enabled = "Active", disabled = "Off")

	val CABIN_REAR_SHADE_IS_OPEN = defineProperty(
			VehiclePropertyIdsOem::CABIN_REAR_SHADE_IS_OPEN, enabled = "Open", disabled = "Closed")

	val CABIN_SUNROOF_SHARE_IS_OPEN = defineProperty(
			VehiclePropertyIdsOem::CABIN_SUNROOF_SHADE_IS_OPEN,
			enabled = "Open", disabled = "Closed")

	val POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL = defineProperty<Float>(
			VehiclePropertyIdsOem::POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL, units = "l")

	val SPEED = defineProperty<Float>(
			VehiclePropertyIdsOem::SPEED, units = "km/h")

	val TRAVELED_DISTANCE = defineProperty<Float>(
			VehiclePropertyIdsOem::TRAVELED_DISTANCE, units = "km")
}
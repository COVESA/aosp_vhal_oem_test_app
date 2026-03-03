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

import android.car.VehiclePropertyIds

val VEHICLE_PROPERTIES = mutableSetOf<PropertyDefinition<*>>()

/** AMBIENT_LIGHT, bool, VSS property added as new standard Android property, dangerous read, write permission */
val VEHICLE_PROPERTY_AMBIENT_LIGHT = property(VssPropertyIds::AMBIENT_LIGHT,
		enabled = "On", disabled = "Off")
		.also(VEHICLE_PROPERTIES::add)

/** INFO_FUEL_CAPACITY, float, standard Android property, normal permission, read-only */
val VEHICLE_PROPERTY_INFO_FUEL_CAPACITY = property(VehiclePropertyIds::INFO_FUEL_CAPACITY,
		units = "ml")
		.also(VEHICLE_PROPERTIES::add)

/** PERF_ODOMETER, float, standard Android property, signature|privileged permission, read-only, requires CAR_MILEAGE permission */
val VEHICLE_PROPERTY_PERF_ODOMETER = property(VehiclePropertyIds::PERF_ODOMETER,
		units = "km")
		.also(VEHICLE_PROPERTIES::add)

/** CABIN_REAR_SHADE_IS_OPEN, bool, VSS property added as new standard Android property, dangerous read, write permission */
val VEHICLE_PROPERTY_CABIN_REAR_SHADE_IS_OPEN = property(VssPropertyIds::CABIN_REAR_SHADE_IS_OPEN,
		enabled = "Open", disabled = "Closed")
		.also(VEHICLE_PROPERTIES::add)

val VEHICLE_PROPERTY_CABIN_SUNROOF_SHARE_IS_OPEN = property(VssPropertyIds::CABIN_SUNROOF_SHADE_IS_OPEN,
		enabled = "Open", disabled = "Closed")
		.also(VEHICLE_PROPERTIES::add)

val VEHICLE_PROPERTY_ADAS_ABS_IS_ENABLED = property(VssPropertyIds::ADAS_ABS_IS_ENABLED,
		enabled = "Enabled", disabled = "Disabled")
		.also(VEHICLE_PROPERTIES::add)

val VEHICLE_PROPERTY_ADAS_CRUISE_CONTROL_IS_ACTIVE = property(VssPropertyIds::ADAS_CRUISE_CONTROL_IS_ACTIVE,
		enabled = "Active", disabled = "Off")
		.also(VEHICLE_PROPERTIES::add)

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

import android.car.Car
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.car.hardware.property.CarPropertyManager.CarPropertyEventCallback
import android.content.Context
import android.util.Log
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(@param:ApplicationContext private val context: Context) :
		ViewModel() {
	companion object {
		private val LOG_TAG = PropertyViewModel::class.simpleName
	}

	private val CarPropertyValue<*>.uid: String
		get() = "${propertyId}-${areaId}"

	private var car: Car? = null
	private var carPropertyManager: CarPropertyManager? = null
	private var carPropertyEventCallback: CarPropertyEventCallback = object : CarPropertyEventCallback {
		override fun onChangeEvent(value: CarPropertyValue<*>) {
			val property = VEHICLE_PROPERTIES.firstOrNull { it.uid == value.uid }
			Log.d(LOG_TAG, "Changed ${property?.name}: ${value.value} (${value})")
			update(value.uid, value.value)
		}

		override fun onErrorEvent(propId: Int, zone: Int) {
			showError("Error reading property: ${propId}")
		}
	}

	private val errorQueue = mutableListOf<String>()
	private val _error = MutableStateFlow<String?>(null)
	val error = _error.asStateFlow()

	private val _adasAbsIsEnabled = MutableStateFlow(VehicleProperty.ADAS_ABS_IS_ENABLED)
	val adasAbsIsEnabled = _adasAbsIsEnabled.asStateFlow()

	private val _adasCruiseControlIsActive =
		MutableStateFlow(VehicleProperty.ADAS_CRUISE_CONTROL_IS_ACTIVE)
	val adasCruiseControlIsActive = _adasCruiseControlIsActive.asStateFlow()

	private val _cabinSeatRow1LeftPosition = MutableStateFlow(VehicleProperty.CABIN_SEAT_ROW1_LEFT_POSITION)
	val cabinSeatRow1LeftPosition = _cabinSeatRow1LeftPosition.asStateFlow()

	private val _cabinSeatRow1RightPosition = MutableStateFlow(VehicleProperty.CABIN_SEAT_ROW1_RIGHT_POSITION)
	val cabinSeatRow1RightPosition = _cabinSeatRow1RightPosition.asStateFlow()

	private val _cabinSeatRow1LeftHeight = MutableStateFlow(VehicleProperty.CABIN_SEAT_ROW1_LEFT_HEIGHT)
	val cabinSeatRow1LeftHeight = _cabinSeatRow1LeftHeight.asStateFlow()

	private val _cabinSeatRow1RightHeight = MutableStateFlow(VehicleProperty.CABIN_SEAT_ROW1_RIGHT_HEIGHT)
	val cabinSeatRow1RightHeight = _cabinSeatRow1RightHeight.asStateFlow()

	private val _cabinSunroofShareIsOpen =
		MutableStateFlow(VehicleProperty.CABIN_SUNROOF_SHARE_IS_OPEN)
	val cabinSunroofShareIsOpen = _cabinSunroofShareIsOpen.asStateFlow()

	private val _cabinRearShadeIsOpen = MutableStateFlow(VehicleProperty.CABIN_REAR_SHADE_IS_OPEN)
	val cabinRearShadeIsOpen = _cabinRearShadeIsOpen.asStateFlow()

	private val _powertrainFuelSystemAbsoluteLevel =
		MutableStateFlow(VehicleProperty.POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL)
	val powertrainFuelSystemAbsoluteLevel = _powertrainFuelSystemAbsoluteLevel.asStateFlow()

	private val _speed = MutableStateFlow(VehicleProperty.SPEED)
	val speed = _speed.asStateFlow()

	private val _traveledDistance = MutableStateFlow(VehicleProperty.TRAVELED_DISTANCE)
	val traveledDistance = _traveledDistance.asStateFlow()

	init {
		viewModelScope.launch(Dispatchers.Default) {
			while (isActive) {
				for (property in VEHICLE_PROPERTIES) {
					val canRead = context.isGranted(property.readPermission)
					val canWrite = property.writePermission?.let { context.isGranted(it) } ?: false

					when (property.uid) {
						_adasAbsIsEnabled.value.definition.uid -> update(_adasAbsIsEnabled,
								canRead,
								canWrite)

						_adasCruiseControlIsActive.value.definition.uid -> update(
								_adasCruiseControlIsActive,
								canRead,
								canWrite)

						_cabinSeatRow1LeftPosition.value.definition.uid -> update(_cabinSeatRow1LeftPosition,
								canRead,
								canWrite)

						_cabinSeatRow1RightPosition.value.definition.uid -> update(_cabinSeatRow1RightPosition,
								canRead,
								canWrite)

						_cabinSeatRow1LeftHeight.value.definition.uid -> update(_cabinSeatRow1LeftHeight,
								canRead,
								canWrite)

						_cabinSeatRow1RightHeight.value.definition.uid -> update(_cabinSeatRow1RightHeight,
								canRead,
								canWrite)

						_cabinSunroofShareIsOpen.value.definition.uid -> update(
								_cabinSunroofShareIsOpen,
								canRead,
								canWrite)

						_cabinRearShadeIsOpen.value.definition.uid -> update(_cabinRearShadeIsOpen,
								canRead,
								canWrite)

						_powertrainFuelSystemAbsoluteLevel.value.definition.uid -> update(
								_powertrainFuelSystemAbsoluteLevel,
								canRead,
								canWrite)

						_speed.value.definition.uid -> update(_speed, canRead, canWrite)
						_traveledDistance.value.definition.uid -> update(_traveledDistance,
								canRead,
								canWrite)
					}
				}
				delay(1000)
			}
		}

		// Get Car instance using the latest API
		Log.d(LOG_TAG, "initializeCar: START")
		car = Car.createCar(context, null, Car.CAR_WAIT_TIMEOUT_DO_NOT_WAIT) { car, ready ->
			if (ready) {
				Log.d(LOG_TAG, "initializeCar: Connected to Car Service")
				carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
				viewModelScope.launch(Dispatchers.Default) { startVehicleDataCollection() }
			} else {
				Log.d(LOG_TAG, "initializeCar: Disconnected from Car Service")
				carPropertyManager = null
			}
		}

		Log.i(LOG_TAG, "initializeCar: Succeeded to create Car instance")
	}


	fun <T : Any> set(property: PropertyValue<T>, value: T) {
		writeProperty(property, value)
		update(property.definition.uid, value)
	}

	override fun onCleared() {
		carPropertyManager?.unregisterCallback(carPropertyEventCallback)
		car?.disconnect()
		super.onCleared()
	}

	private fun startVehicleDataCollection() {
		Log.d(LOG_TAG, "startVehicleDataCollection: START")
		try {
			if (carPropertyManager == null) {
				val message = "startVehicleDataCollection: CarPropertyManager is null"
				Log.e(LOG_TAG, message)
				return
			}

			for (property in VEHICLE_PROPERTIES) {
				val propertyId = property.id
				Log.d(LOG_TAG, "Registering callback for ${property.name}")
				carPropertyManager?.registerCallback(carPropertyEventCallback, propertyId,
						CarPropertyManager.SENSOR_RATE_NORMAL)
			}

			// Get initial values
			try {
				_adasAbsIsEnabled.value = readProperty(VehicleProperty.ADAS_ABS_IS_ENABLED)
				_adasCruiseControlIsActive.value = readProperty(VehicleProperty.ADAS_CRUISE_CONTROL_IS_ACTIVE)
				_cabinSeatRow1LeftPosition.value = readProperty(VehicleProperty.CABIN_SEAT_ROW1_LEFT_POSITION)
				_cabinSeatRow1RightPosition.value = readProperty(VehicleProperty.CABIN_SEAT_ROW1_RIGHT_POSITION)
				_cabinSeatRow1LeftHeight.value = readProperty(VehicleProperty.CABIN_SEAT_ROW1_LEFT_HEIGHT)
				_cabinSeatRow1RightHeight.value = readProperty(VehicleProperty.CABIN_SEAT_ROW1_RIGHT_HEIGHT)
				_cabinRearShadeIsOpen.value = readProperty(VehicleProperty.CABIN_REAR_SHADE_IS_OPEN)
				_cabinSunroofShareIsOpen.value = readProperty(VehicleProperty.CABIN_SUNROOF_SHARE_IS_OPEN)
				_powertrainFuelSystemAbsoluteLevel.value = readProperty(VehicleProperty.POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL)
				_speed.value = readProperty(VehicleProperty.SPEED)
				_traveledDistance.value = readProperty(VehicleProperty.TRAVELED_DISTANCE)
			} catch (e: Exception) {
				Log.e(LOG_TAG, e.message, e)
			}
		} catch (e: Exception) {
			Log.e(LOG_TAG, e.message, e)
		}
		Log.d(LOG_TAG, "startVehicleDataCollection: DONE")
	}

	private fun <T : Any> readProperty(property: PropertyValue<T>) = try {
		val value: T? = carPropertyManager?.getProperty<T>(property.definition.id, property.definition.areaId)?.getValue()
		Log.d(LOG_TAG, "readPropertyBool: ${property.definition.name} = ${value}")
		property.copy(value = value, hasError = value != null)
	} catch (e: Exception) {
		Log.d(LOG_TAG, "readPropertyBool: ${property.definition.name} threw ${e.message}")
		showError("Error reading value for property ${property.definition.name}!", e)
		property.copy(value = null, hasError = true)
	}

	private fun <T : Any> update(state: MutableStateFlow<PropertyValue<T>>, value: Any?) {
		if (state.value == value) return
		val property = state.value
		state.value = try {
			if (value != null) property.copy(value = value as T, hasError = false)
			else property.copy(value = null, hasError = true)
		} catch (e: ClassCastException) {
			showError("Error updating property ${property.definition.name} with value \"${value}\" of type ${value?.javaClass}!",
					e)
			property.copy(value = null, hasError = true)
		}
	}

	private fun <T : Any> update(state: MutableStateFlow<PropertyValue<T>>,
								 canRead: Boolean,
								 canWrite: Boolean) {
		if (state.value.canRead != canRead || state.value.canWrite != canWrite) {
			state.value = state.value.copy(canRead = canRead, canWrite = canWrite)
			if (canRead) {
				carPropertyManager?.registerCallback(carPropertyEventCallback,
						state.value.definition.id,
						CarPropertyManager.SENSOR_RATE_NORMAL)
			}
		}
	}

	private fun update(uniqueId: String, value: Any) {
		when (uniqueId) {
			_adasAbsIsEnabled.value.definition.uid -> update(_adasAbsIsEnabled, value)
			_adasCruiseControlIsActive.value.definition.uid -> update(_adasCruiseControlIsActive, value)
			_cabinSeatRow1LeftPosition.value.definition.uid -> update(_cabinSeatRow1LeftPosition, value)
			_cabinSeatRow1RightPosition.value.definition.uid -> update(_cabinSeatRow1RightPosition, value)
			_cabinSeatRow1LeftHeight.value.definition.uid -> update(_cabinSeatRow1LeftHeight, value)
			_cabinSeatRow1RightHeight.value.definition.uid -> update(_cabinSeatRow1RightHeight, value)
			_cabinSunroofShareIsOpen.value.definition.uid -> update(_cabinSunroofShareIsOpen, value)
			_cabinRearShadeIsOpen.value.definition.uid -> update(_cabinRearShadeIsOpen, value)
			_powertrainFuelSystemAbsoluteLevel.value.definition.uid -> update(
					_powertrainFuelSystemAbsoluteLevel,
					value)

			_speed.value.definition.uid -> update(_speed, value)
			_traveledDistance.value.definition.uid -> update(_traveledDistance, value)
			else -> {}
		}
	}

	private fun <T : Any> writeProperty(property: PropertyValue<T>, value: T) {
		Log.d(LOG_TAG, "writeProperty: ${property.definition.name} (areaId=${property.definition.areaId}) = ${property.value} -> ${value}")
		if (property.value != value) try {
			carPropertyManager!!.setProperty(property.definition.type.java,
					property.definition.id,
					property.definition.areaId,
					value)
			update(property.definition.uid, value)
		} catch (_: Exception) {
			showError("Error setting value for property ${property.definition.name}!")
		}
	}

	private fun showError(message: String, exception: Exception? = null) {
		Log.e(LOG_TAG, listOfNotNull(message, exception?.message).fastJoinToString("\n"), exception)
		if (_error.value == null) _error.value = message
		else if (message !in errorQueue) errorQueue.add(message)
	}

	fun dismissError() {
		_error.value = errorQueue.removeFirstOrNull()
	}
}

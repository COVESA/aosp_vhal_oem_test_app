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
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.car.hardware.property.CarPropertyManager.CarPropertyEventCallback
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(@param:ApplicationContext private val context: Context) : ViewModel() {
	companion object {
		private val LOG_TAG = PropertyViewModel::class.simpleName
	}


	private var car: Car? = null
	private var carPropertyManager: CarPropertyManager? = null
	private var carPropertyEventCallback: CarPropertyEventCallback? = null


	private val handlerThread = HandlerThread("MyHandlerThread")
	private var backgroundHandler: Handler? = null

	private val errorQueue = mutableListOf<String>()
	private val _error = MutableStateFlow<String?>(null)
	val error = _error.asStateFlow()

	init {
		handlerThread.start()
		backgroundHandler = Handler(handlerThread.getLooper())

		// Get Car instance using the latest API
		Log.d(LOG_TAG, "initializeCar: START")
		car = Car.createCar(context, null, Car.CAR_WAIT_TIMEOUT_DO_NOT_WAIT) { car, ready ->
			if (ready) {
				Log.d(LOG_TAG, "initializeCar: Connected to Car Service")
				carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
				backgroundHandler?.post { startVehicleDataCollection() }
			} else {
				Log.d(LOG_TAG, "initializeCar: Disconnected from Car Service")
				carPropertyManager = null
			}
		}

		// Check connection to car service
		if (car != null) {
			Log.i(LOG_TAG, "initializeCar: Succeeded to create Car instance")
		} else {
			Log.e(LOG_TAG, "initializeCar: Failed to create Car instance")
		}
	}

	fun <T : Any> set(property: PropertyDefinition<T>, value: T) {
		writeProperty(property, value)
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


			carPropertyEventCallback = object : CarPropertyEventCallback {
				override fun onChangeEvent(value: CarPropertyValue<*>) {
					val property = VEHICLE_PROPERTIES.firstOrNull { it.id == value.propertyId }
					if (property != null) updateProperty(property, value.value)
				}

				override fun onErrorEvent(propId: Int, zone: Int) {
					showError("Error reading property: ${propId}")
				}
			}

			// Register callbacks
			val propertyIds = intArrayOf(
					VehiclePropertyIds.INFO_FUEL_CAPACITY,  // normal permission
					VehiclePropertyIds.PERF_ODOMETER,  // privileged permission
					VssPropertyIds.AMBIENT_LIGHT,  // normal read, dangerous write permission, new standard prop
					VssPropertyIds.CABIN_SUNROOF_SHADE_IS_OPEN,  // normal read, dangerous write permission, new standard prop
					VssPropertyIds.CABIN_REAR_SHADE_IS_OPEN,  // normal read, dangerous write permission, new standard prop
					VssPropertyIds.ADAS_ABS_IS_ENABLED,  // normal read, dangerous write permission, new standard prop
					VssPropertyIds.ADAS_CRUISE_CONTROL_IS_ACTIVE // normal read, dangerous write permission, new standard prop
			)
			for (propertyId in propertyIds) {
				carPropertyManager?.registerCallback(carPropertyEventCallback, propertyId,
						CarPropertyManager.SENSOR_RATE_NORMAL)
			}

			// Get initial values
			try {
				readProperty(VEHICLE_PROPERTY_INFO_FUEL_CAPACITY)
				readProperty(VEHICLE_PROPERTY_PERF_ODOMETER)
				readProperty(VEHICLE_PROPERTY_AMBIENT_LIGHT)
				readProperty(VEHICLE_PROPERTY_CABIN_SUNROOF_SHARE_IS_OPEN)
				readProperty(VEHICLE_PROPERTY_CABIN_REAR_SHADE_IS_OPEN)
				readProperty(VEHICLE_PROPERTY_ADAS_ABS_IS_ENABLED)
				readProperty(VEHICLE_PROPERTY_ADAS_CRUISE_CONTROL_IS_ACTIVE)
			} catch (e: Exception) {
				Log.e(LOG_TAG, e.message, e)
			}
		} catch (e: Exception) {
			Log.e(LOG_TAG, e.message, e)
		}
		Log.d(LOG_TAG, "startVehicleDataCollection: DONE")
	}

	private fun <T : Any> readProperty(property: PropertyDefinition<T>) {
		try {
			val value: T? = carPropertyManager?.getProperty<T>(property.id, 0)?.getValue()
			Log.d(LOG_TAG, "readPropertyBool: ${property.name} = ${value}")
			updateProperty(property, value)
		} catch (e: Exception) {
			Log.d(LOG_TAG, "readPropertyBool: ${property.name} threw ${e.message}")
			property.mutableValueStateFlow.value = null
			property.mutableErrorStateFlow.ensure(true)
			showError("Error reading value for property ${property.name}!", e)
		}
	}

	private fun <T : Any> updateProperty(property: PropertyDefinition<T>, value: Any?) = try {
		if (value != null) {
			property.mutableValueStateFlow.value = value as T
			property.mutableErrorStateFlow.ensure(false)
		} else {
			property.mutableValueStateFlow.value = null
			property.mutableErrorStateFlow.ensure(true)
		}
	} catch (e: ClassCastException) {
		property.mutableValueStateFlow.value = null
		property.mutableErrorStateFlow.ensure(true)
		showError("Error updating property ${property.name} with value \"${value}\" of type ${value?.javaClass}!", e)
	}

	private fun <T : Any> writeProperty(property: PropertyDefinition<T>, value: T) {
		Log.d(LOG_TAG, "writePropertyBool: ${property.name} = ${property.mutableValueStateFlow.value} -> ${value}")
		if (property.mutableValueStateFlow.value != value) try {
			carPropertyManager!!.setProperty(property::type.javaClass, property.id, 0, value)
			property.mutableValueStateFlow.value = value
		} catch (e: Exception) {
			showError("Error setting value for property ${property.name}!")
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

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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import global.covesa.aosp.vhal.test.app.theme.AppTheme

@Composable
fun MainView(viewModel: PropertyViewModel = hiltViewModel()) {
	val ambientLightValue by VEHICLE_PROPERTY_AMBIENT_LIGHT.valueStateFlow.collectAsState()
	val ambientLightText by remember(ambientLightValue) {
		derivedStateOf { VEHICLE_PROPERTY_AMBIENT_LIGHT.format(ambientLightValue) }
	}
	val ambientLightError by VEHICLE_PROPERTY_AMBIENT_LIGHT.errorStateFlow.collectAsState()

	val infoFuelCapacityValue by VEHICLE_PROPERTY_INFO_FUEL_CAPACITY.valueStateFlow.collectAsState()
	val infoFuelCapacityText by remember(infoFuelCapacityValue) {
		derivedStateOf { VEHICLE_PROPERTY_INFO_FUEL_CAPACITY.format(infoFuelCapacityValue) }
	}
	val infoFuelCapacityError by VEHICLE_PROPERTY_INFO_FUEL_CAPACITY.errorStateFlow.collectAsState()

	val perfOdometerValue by VEHICLE_PROPERTY_PERF_ODOMETER.valueStateFlow.collectAsState()
	val perfOdometerText by remember(perfOdometerValue) {
		derivedStateOf { VEHICLE_PROPERTY_PERF_ODOMETER.format(perfOdometerValue) }
	}
	val perfOdometerError by VEHICLE_PROPERTY_PERF_ODOMETER.errorStateFlow.collectAsState()

	val cabinRearShadeIsOpenValue by VEHICLE_PROPERTY_CABIN_REAR_SHADE_IS_OPEN.valueStateFlow.collectAsState()
	val cabinRearShadeIsOpenText by remember(cabinRearShadeIsOpenValue) {
		derivedStateOf { VEHICLE_PROPERTY_CABIN_REAR_SHADE_IS_OPEN.format(cabinRearShadeIsOpenValue) }
	}
	val cabinRearShadeIsOpenError by VEHICLE_PROPERTY_CABIN_REAR_SHADE_IS_OPEN.errorStateFlow.collectAsState()

	val cabinSunroofShareIsOpenValue by VEHICLE_PROPERTY_CABIN_SUNROOF_SHARE_IS_OPEN.valueStateFlow.collectAsState()
	val cabinSunroofShareIsOpenText by remember(cabinSunroofShareIsOpenValue) {
		derivedStateOf { VEHICLE_PROPERTY_CABIN_SUNROOF_SHARE_IS_OPEN.format(cabinSunroofShareIsOpenValue) }
	}
	val cabinSunroofShareIsOpenError by VEHICLE_PROPERTY_CABIN_SUNROOF_SHARE_IS_OPEN.errorStateFlow.collectAsState()

	val adasAbsIsEnabledValue by VEHICLE_PROPERTY_ADAS_ABS_IS_ENABLED.valueStateFlow.collectAsState()
	val adasAbsIsEnabledText by remember(adasAbsIsEnabledValue) {
		derivedStateOf { VEHICLE_PROPERTY_ADAS_ABS_IS_ENABLED.format(adasAbsIsEnabledValue) }
	}
	val adasAbsIsEnabledError by VEHICLE_PROPERTY_ADAS_ABS_IS_ENABLED.errorStateFlow.collectAsState()

	val adasCruiseControlIsActiveValue by VEHICLE_PROPERTY_ADAS_CRUISE_CONTROL_IS_ACTIVE.valueStateFlow.collectAsState()
	val adasCruiseControlIsActiveText by remember(adasCruiseControlIsActiveValue) {
		derivedStateOf { VEHICLE_PROPERTY_ADAS_CRUISE_CONTROL_IS_ACTIVE.format(adasCruiseControlIsActiveValue) }
	}
	val adasCruiseControlIsActiveError by VEHICLE_PROPERTY_ADAS_CRUISE_CONTROL_IS_ACTIVE.errorStateFlow.collectAsState()
	val error by viewModel.error.collectAsState()

	MainView(ambientLightValue = ambientLightValue,
			ambientLightText = ambientLightText,
			ambientLightError = ambientLightError,
			infoFuelCapacityValue = infoFuelCapacityValue,
			infoFuelCapacityText = infoFuelCapacityText,
			infoFuelCapacityError = infoFuelCapacityError,
			perfOdometerValue = perfOdometerValue,
			perfOdometerText = perfOdometerText,
			perfOdometerError = perfOdometerError,
			cabinRearShadeIsOpenValue = cabinRearShadeIsOpenValue,
			cabinRearShadeIsOpenText = cabinRearShadeIsOpenText,
			cabinRearShadeIsOpenError = cabinRearShadeIsOpenError,
			cabinSunroofShareIsOpenValue = cabinSunroofShareIsOpenValue,
			cabinSunroofShareIsOpenText = cabinSunroofShareIsOpenText,
			cabinSunroofShareIsOpenError = cabinSunroofShareIsOpenError,
			onCabinSunroofShadeIsOpen = { viewModel.set(VEHICLE_PROPERTY_CABIN_SUNROOF_SHARE_IS_OPEN, it) },
			adasAbsIsEnabledValue = adasAbsIsEnabledValue,
			adasAbsIsEnabledText = adasAbsIsEnabledText,
			adasAbsIsEnabledError = adasAbsIsEnabledError,
			onAdasAbsIsEnabled = { viewModel.set(VEHICLE_PROPERTY_ADAS_ABS_IS_ENABLED, it) },
			adasCruiseControlIsActiveValue = adasCruiseControlIsActiveValue,
			adasCruiseControlIsActiveText = adasCruiseControlIsActiveText,
			adasCruiseControlIsActiveError = adasCruiseControlIsActiveError,
			onAdasCruiseControlIsActive = { viewModel.set(VEHICLE_PROPERTY_ADAS_CRUISE_CONTROL_IS_ACTIVE, it) },
			error = error,
			onDismissError = viewModel::dismissError)
}

@Composable
fun MainView(ambientLightValue: Boolean?,
			 ambientLightText: String,
			 ambientLightError: Boolean,
			 infoFuelCapacityValue: Float?,
			 infoFuelCapacityText: String,
			 infoFuelCapacityError: Boolean,
			 perfOdometerValue: Float?,
			 perfOdometerText: String,
			 perfOdometerError: Boolean,
			 cabinRearShadeIsOpenValue: Boolean?,
			 cabinRearShadeIsOpenText: String,
			 cabinRearShadeIsOpenError: Boolean,
			 cabinSunroofShareIsOpenValue: Boolean?,
			 cabinSunroofShareIsOpenText: String,
			 cabinSunroofShareIsOpenError: Boolean,
			 onCabinSunroofShadeIsOpen: (Boolean) -> Unit,
			 adasAbsIsEnabledValue: Boolean?,
			 adasAbsIsEnabledText: String,
			 adasAbsIsEnabledError: Boolean,
			 onAdasAbsIsEnabled: (Boolean) -> Unit,
			 adasCruiseControlIsActiveValue: Boolean?,
			 adasCruiseControlIsActiveText: String,
			 adasCruiseControlIsActiveError: Boolean,
			 onAdasCruiseControlIsActive: (Boolean) -> Unit,
			 error: String?,
			 onDismissError: () -> Unit) {
	Surface {
		Box(modifier = Modifier.fillMaxSize()) {
			Column(modifier = Modifier
					.fillMaxSize()
					.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(16.dp)) {
				Row(modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically) {
					Text(text = "The", style = MaterialTheme.typography.headlineLarge)
					Image(modifier = Modifier
							.padding(horizontal = 24.dp)
							.height(60.dp),
							painter = painterResource(id = R.mipmap.covesa_logo), // reference mipmap image
							contentDescription = "Logo",
							contentScale = ContentScale.Fit)
					Text("VHAL Test App", style = MaterialTheme.typography.headlineLarge)
				}

				Text(text = stringResource(R.string.ambient_light, ambientLightText),
						color = if (ambientLightError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
				Text(text = stringResource(R.string.info_fuel_capacity, infoFuelCapacityText),
						color = if (infoFuelCapacityError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
				Text(text = stringResource(R.string.perf_odometer, perfOdometerText),
						color = if (perfOdometerError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
				Text(text = stringResource(R.string.cabin_rear_shade_is_open, cabinRearShadeIsOpenText),
						color = if (cabinRearShadeIsOpenError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)

				SwitchButton(
						modifier = Modifier.fillMaxWidth(),
						text = stringResource(R.string.cabin_sunroof_shade_is_open, cabinSunroofShareIsOpenText),
						isChecked = cabinSunroofShareIsOpenValue == true,
						isEnabled = cabinSunroofShareIsOpenValue != null,
						hasError = cabinSunroofShareIsOpenError,
						onChecked = onCabinSunroofShadeIsOpen,
				)
				SwitchButton(
						modifier = Modifier.fillMaxWidth(),
						text = stringResource(R.string.adas_abs_is_enabled, adasAbsIsEnabledText),
						isChecked = adasAbsIsEnabledValue == true,
						isEnabled = adasAbsIsEnabledValue != null,
						hasError = adasAbsIsEnabledError,
						onChecked = onAdasAbsIsEnabled)
				SwitchButton(
						modifier = Modifier.fillMaxWidth(),
						text = stringResource(R.string.adas_cruise_control_is_active, adasCruiseControlIsActiveText),
						isChecked = adasCruiseControlIsActiveValue == true,
						isEnabled = adasCruiseControlIsActiveValue != null,
						hasError = adasCruiseControlIsActiveError,
						onChecked = onAdasCruiseControlIsActive)
			}
			if (error != null) Snackbar(modifier = Modifier.align(Alignment.BottomCenter),
					dismissAction = { TextButton(onClick = onDismissError) { Text("Dismiss") } },
					content = { Text(error) })
		}
	}
}

@Composable
private fun SwitchButton(text: String,
						 isChecked: Boolean,
						 isEnabled: Boolean,
						 hasError: Boolean,
						 onChecked: (Boolean) -> Unit,
						 modifier: Modifier = Modifier) {
	Row(
			modifier = modifier,
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
	) {
		Text(text = text,
				color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
		Switch(checked = isChecked,
				enabled = isEnabled,
				onCheckedChange = onChecked)
	}
}

@Preview(device = "id:automotive_1408p_landscape_with_google_apis")
@Composable
private fun MainViewPreview() {
	AppTheme {
		var cabinSunroofShareIsOpenValue by remember { mutableStateOf(true) }
		var adasAbsIsEnabledValue by remember { mutableStateOf(true) }
		var adasCruiseControlIsActiveValue by remember { mutableStateOf<Boolean?>(null) }
		MainView(
				ambientLightValue = null,
				ambientLightText = "Off",
				ambientLightError = false,
				infoFuelCapacityValue = 12574.0f,
				infoFuelCapacityText = "12574 ml",
				infoFuelCapacityError = false,
				perfOdometerValue = 25357.0f,
				perfOdometerText = "25357 km",
				perfOdometerError = false,
				cabinRearShadeIsOpenValue = false,
				cabinRearShadeIsOpenText = "Closed",
				cabinRearShadeIsOpenError = false,
				cabinSunroofShareIsOpenValue = cabinSunroofShareIsOpenValue,
				cabinSunroofShareIsOpenText = if (cabinSunroofShareIsOpenValue) "Open" else "Closed",
				cabinSunroofShareIsOpenError = false,
				onCabinSunroofShadeIsOpen = { cabinSunroofShareIsOpenValue = it },
				adasAbsIsEnabledValue = adasAbsIsEnabledValue,
				adasAbsIsEnabledText = if (adasAbsIsEnabledValue) "Enabled" else "Disabled",
				adasAbsIsEnabledError = true,
				onAdasAbsIsEnabled = { adasAbsIsEnabledValue = it },
				adasCruiseControlIsActiveValue = adasCruiseControlIsActiveValue,
				adasCruiseControlIsActiveText = if (adasCruiseControlIsActiveValue == true) "Active" else "Off",
				adasCruiseControlIsActiveError = false,
				onAdasCruiseControlIsActive = { adasCruiseControlIsActiveValue = it },
				error = "This is a test message",
				onDismissError = {})
	}
}

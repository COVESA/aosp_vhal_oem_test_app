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

import android.car.VehicleAreaSeat
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import global.covesa.aosp.vhal.test.app.theme.AppTheme

@Composable
fun MainView(viewModel: PropertyViewModel = hiltViewModel()) {
	val adasAbsIsEnabled by viewModel.adasAbsIsEnabled.collectAsState()
	val adasCruiseControlIsActive by viewModel.adasCruiseControlIsActive.collectAsState()
	val cabinRearShadeIsOpen by viewModel.cabinRearShadeIsOpen.collectAsState()
	val cabinSeatRow1LeftPosition by viewModel.cabinSeatRow1LeftPosition.collectAsState()
	val cabinSeatRow1RightPosition by viewModel.cabinSeatRow1RightPosition.collectAsState()
	val cabinSeatRow1LeftHeight by viewModel.cabinSeatRow1LeftHeight.collectAsState()
	val cabinSeatRow1RightHeight by viewModel.cabinSeatRow1RightHeight.collectAsState()
	val cabinSunroofShareIsOpen by viewModel.cabinSunroofShareIsOpen.collectAsState()
	val powertrainFuelSystemAbsoluteLevel by viewModel.powertrainFuelSystemAbsoluteLevel.collectAsState()
	val speed by viewModel.speed.collectAsState()
	val traveledDistance by viewModel.traveledDistance.collectAsState()
	val error by viewModel.error.collectAsState()

	MainView(
			cabinSeatRow1LeftPosition = cabinSeatRow1LeftPosition,
			cabinSeatRow1RightPosition = cabinSeatRow1RightPosition,
			cabinSeatRow1LeftHeight = cabinSeatRow1LeftHeight,
			cabinSeatRow1RightHeight = cabinSeatRow1RightHeight,
			adasAbsIsEnabled = adasAbsIsEnabled,
			adasCruiseControlIsActive = adasCruiseControlIsActive,
			cabinRearShadeIsOpen = cabinRearShadeIsOpen,
			cabinSunroofShareIsOpen = cabinSunroofShareIsOpen,
			powertrainFuelSystemAbsoluteLevel = powertrainFuelSystemAbsoluteLevel,
			speed = speed,
			traveledDistance = traveledDistance,
			error = error,
			onSwitch = { prop, value -> viewModel.set(prop, value) },
			onChange = { prop, value -> viewModel.set(prop, value) },
			onDismissError = viewModel::dismissError)
}

@Composable
fun MainView(cabinSeatRow1LeftPosition: PropertyValue<Int>,
			 cabinSeatRow1RightPosition: PropertyValue<Int>,
			 cabinSeatRow1LeftHeight: PropertyValue<Int>,
			 cabinSeatRow1RightHeight: PropertyValue<Int>,
			 adasAbsIsEnabled: PropertyValue<Boolean>,
			 adasCruiseControlIsActive: PropertyValue<Boolean>,
			 cabinRearShadeIsOpen: PropertyValue<Boolean>,
			 cabinSunroofShareIsOpen: PropertyValue<Boolean>,
			 powertrainFuelSystemAbsoluteLevel: PropertyValue<Float>,
			 speed: PropertyValue<Float>,
			 traveledDistance: PropertyValue<Float>,
			 error: String?,
			 onSwitch: (PropertyValue<Boolean>, Boolean) -> Unit,
			 onChange: (PropertyValue<Int>, Int) -> Unit,
			 onDismissError: () -> Unit) {
	Surface {
		Box(modifier = Modifier.fillMaxSize()) {
			Column(modifier = Modifier
					.fillMaxSize()
					.padding(top = 16.dp, bottom = 64.dp),
					verticalArrangement = Arrangement.SpaceBetween) {
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

				if (!speed.canRead
					&& !powertrainFuelSystemAbsoluteLevel.canRead
					&& !traveledDistance.canRead
					&& !cabinSeatRow1LeftPosition.canRead
					&& !cabinSeatRow1RightPosition.canRead
					&& !cabinSeatRow1LeftHeight.canRead
					&& !cabinSeatRow1RightHeight.canRead
					&& !adasAbsIsEnabled.canRead
					&& !adasCruiseControlIsActive.canRead
					&& !cabinRearShadeIsOpen.canRead
					&& !cabinSunroofShareIsOpen.canRead) Text(
						modifier = Modifier.align(Alignment.CenterHorizontally),
						text = "This vehicle does not provide any required data or functions",
						style = MaterialTheme.typography.displayMedium,
						color = MaterialTheme.colorScheme.error)

				Row(modifier = Modifier
						.padding(top = 16.dp)
						.align(Alignment.CenterHorizontally),
						horizontalArrangement = Arrangement.spacedBy(64.dp)) {
					if (speed.canRead) Gauge(modifier = Modifier,
							value = speed.value,
							format = "%.0f",
							max = 250,
							units = speed.definition.units ?: "km/h",
							hasError = speed.hasError)
					if (powertrainFuelSystemAbsoluteLevel.canRead) Gauge(modifier = Modifier,
							value = powertrainFuelSystemAbsoluteLevel.value,
							format = "%.1f",
							max = 60,
							units = powertrainFuelSystemAbsoluteLevel.definition.units ?: "l",
							hasError = speed.hasError)
				}

				if (traveledDistance.canRead) Text(modifier = Modifier.align(Alignment.CenterHorizontally),
						text = traveledDistance.format(),
						style = MaterialTheme.typography.displayLarge,
						/*color = if (traveledDistance.hasError)
							MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface*/)

				Row(modifier = Modifier.fillMaxWidth().padding(start = 32.dp),
						horizontalArrangement = Arrangement.SpaceEvenly) {
					Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
						Slider(textId = R.string.cabin_seat_pos_row_1_left,
								property = cabinSeatRow1LeftPosition,
								min = 0,
								max = 1000,
								onValueChange = onChange)
						Slider(textId = R.string.cabin_seat_pos_row_1_right,
								property = cabinSeatRow1RightPosition,
								min = 0,
								max = 1000,
								onValueChange = onChange)
					}
					Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
						Slider(textId = R.string.cabin_seat_height_row_1_left,
								property = cabinSeatRow1LeftHeight,
								min = 0,
								max = 1000,
								onValueChange = onChange)
						Slider(textId = R.string.cabin_seat_height_row_1_right,
								property = cabinSeatRow1RightHeight,
								min = 0,
								max = 1000,
								onValueChange = onChange)
					}
					Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
						SwitchButton(
								textId = R.string.adas_abs_is_enabled,
								property = adasAbsIsEnabled,
								onChecked = onSwitch)
						SwitchButton(
								textId = R.string.adas_cruise_control_is_active,
								property = adasCruiseControlIsActive,
								onChecked = onSwitch)
					}
					Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
						SwitchButton(
								textId = R.string.cabin_rear_shade_is_open,
								property = cabinRearShadeIsOpen,
								onChecked = onSwitch)
						SwitchButton(
								textId = R.string.cabin_sunroof_shade_is_open,
								property = cabinSunroofShareIsOpen,
								onChecked = onSwitch)
					}
				}
			}
			//if (error != null) Snackbar(modifier = Modifier.align(Alignment.BottomCenter),
			//		dismissAction = { TextButton(onClick = onDismissError) { Text("Dismiss") } },
			//		content = { Text(error) })
		}
	}
}

@Composable
private fun Gauge(
	value: Number?,
	format: String,
	max: Number,
	units: String,
	hasError: Boolean,
	modifier: Modifier = Modifier) {
	val rawProgress = ((value ?: 0).toFloat() / max.toFloat()).coerceIn(0f, 1f)
	val progress by animateFloatAsState(
			targetValue = rawProgress,
			animationSpec = tween(durationMillis = 500),
			label = "speedGaugeProgress")
	val gaugeColor = /*if (hasError)
		MaterialTheme.colorScheme.error else*/ MaterialTheme.colorScheme.primary
	val gaugeTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

	Box(modifier = modifier) {
		Canvas(modifier = Modifier.size(280.dp)) {
			val stroke = 24.dp.toPx()
			val arcSize = size.minDimension - stroke
			val topLeft = androidx.compose.ui.geometry.Offset((size.width - arcSize) / 2f,
					(size.height - arcSize) / 2f)
			val arc = androidx.compose.ui.geometry.Size(arcSize, arcSize)

			drawArc(
					color = gaugeTrackColor,
					startAngle = 150f,
					sweepAngle = 240f,
					useCenter = false,
					topLeft = topLeft,
					size = arc,
					style = Stroke(width = stroke, cap = StrokeCap.Round),
			)

			drawArc(
					color = gaugeColor,
					startAngle = 150f,
					sweepAngle = 240f * progress,
					useCenter = false,
					topLeft = topLeft,
					size = arc,
					style = Stroke(width = stroke, cap = StrokeCap.Round),
			)
		}

		Column(modifier = Modifier.align(Alignment.Center),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(4.dp)) {
			Text(text = value?.let { format.format(it) } ?: "---",
					style = MaterialTheme.typography.displayLarge,
					color = /*if (hasError)
						MaterialTheme.colorScheme.error else*/ MaterialTheme.colorScheme.onSurface)
			Text(text = units,
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant)
			//if (hasError) Text(text = "Read error",
			//			color = MaterialTheme.colorScheme.error,
			//			style = MaterialTheme.typography.bodyMedium)
		}
	}
}


@Composable
private fun SwitchButton(@StringRes textId: Int,
						 property: PropertyValue<Boolean>,
						 onChecked: (PropertyValue<Boolean>, Boolean) -> Unit,
						 modifier: Modifier = Modifier) {
	if (property.canRead) Row(
			modifier = modifier,
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			verticalAlignment = Alignment.CenterVertically) {
		Switch(checked = property.value == true,
				enabled = property.canWrite,
				onCheckedChange = { onChecked(property, it) })
		Text(text = stringResource(textId, property.format()),
				/*color = if (property.hasError) MaterialTheme.colorScheme.error else
					MaterialTheme.colorScheme.onSurface*/)
	}
}

@Composable
private fun Slider(@StringRes textId: Int,
				   property: PropertyValue<Int>,
				   onValueChange: (PropertyValue<Int>, Int) -> Unit,
				   modifier: Modifier = Modifier,
				   min: Int = 0,
				   max: Int = 100) {
	if (property.canRead) Row(
			modifier = modifier,
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			verticalAlignment = Alignment.CenterVertically) {
		Text(text = stringResource(textId, property.format()),
				/*color = if (property.hasError) MaterialTheme.colorScheme.error else
					MaterialTheme.colorScheme.onSurface*/)
		Slider(modifier = Modifier.width(150.dp),
				value = property.value?.toFloat() ?: min.toFloat(),
				valueRange = min.toFloat()..max.toFloat(),
				steps = max - min + 1,
				enabled = property.canWrite,
				onValueChange = { onValueChange(property, it.toInt()) })
	}
}

@Preview(device = "id:automotive_1408p_landscape_with_google_apis")
@Composable
private fun MainViewPreview() {
	AppTheme {
		var cabinSeatRow1LeftPosition by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_POSITION",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_LEFT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_POSITION_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_POSITION_WRITE",
							unitsOrEnum = "mm"),
					value = 100,
					hasError = false,
					canRead = true,
					canWrite = false))
		}
		var cabinSeatRow1RightPosition by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_POSITION",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_RIGHT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_POSITION_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_POSITION_WRITE",
							unitsOrEnum = "mm"),
					value = 200,
					hasError = false,
					canRead = true,
					canWrite = true))
		}
		var cabinSeatRow1LeftHeight by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_HEIGHT",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_LEFT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_WRITE",
							unitsOrEnum = "mm"),
					value = 300,
					hasError = false,
					canRead = true,
					canWrite = false))
		}
		var cabinSeatRow1RightHeight by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_HEIGHT",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_RIGHT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_WRITE",
							unitsOrEnum = "mm"),
					value = 400,
					hasError = false,
					canRead = true,
					canWrite = true))
		}
		var adasAbsIsEnabled by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 2,
							name = "ADAS_ABS_IS_ENABLED",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.ADAS_ABS_IS_ENABLED_READ",
							writePermission = null,
							unitsOrEnum = "ENABLED|DISABLED"),
					value = false,
					hasError = false,
					canRead = true,
					canWrite = false))
		}
		var adasCruiseControlIsActive by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 3,
							name = "ADAS_CRUISE_CONTROL_IS_ACTIVE",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.ADAS_CRUISE_CONTROL_IS_ACTIVE_READ",
							writePermission = "android.car.permission.oem.ADAS_CRUISE_CONTROL_IS_ACTIVE_WRITE",
							unitsOrEnum = "ACTIVE|OFF"),
					value = true,
					hasError = false,
					canRead = true,
					canWrite = true))
		}
		var cabinRearShadeIsOpen by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 4,
							name = "CABIN_REAR_SHADE_IS_OPEN",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.CABIN_REAR_SHADE_IS_OPEN_READ",
							writePermission = "android.car.permission.oem.CABIN_REAR_SHADE_IS_OPEN_WRITE",
							unitsOrEnum = "OPEN|CLOSED"),
					value = false,
					hasError = false,
					canRead = true,
					canWrite = true))
		}
		var cabinSunroofShareIsOpen by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 5,
							name = "CABIN_SUNROOF_SHADE_IS_OPEN",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.CABIN_SUNROOF_SHADE_IS_OPEN_READ",
							writePermission = null,
							unitsOrEnum = "OPEN|CLOSED"),
					value = true,
					hasError = false,
					canRead = true,
					canWrite = false))
		}
		MainView(
				cabinSeatRow1LeftHeight = cabinSeatRow1LeftHeight,
				cabinSeatRow1RightHeight = cabinSeatRow1RightHeight,
				cabinSeatRow1LeftPosition = cabinSeatRow1LeftPosition,
				cabinSeatRow1RightPosition = cabinSeatRow1RightPosition,
				adasAbsIsEnabled = adasAbsIsEnabled,
				adasCruiseControlIsActive = adasCruiseControlIsActive,
				cabinRearShadeIsOpen = cabinRearShadeIsOpen,
				cabinSunroofShareIsOpen = cabinSunroofShareIsOpen,
				powertrainFuelSystemAbsoluteLevel = PropertyValue(
						definition = PropertyDefinition(
								id = 6,
								name = "POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL",
								type = Float::class,
								readPermission = "android.car.permission.oem.POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL_READ",
								writePermission = null,
								unitsOrEnum = "l"),
						value = 42.5f,
						hasError = false,
						canRead = true),
				speed = PropertyValue(
						definition = PropertyDefinition(
								id = 7,
								name = "SPEED",
								type = Float::class,
								readPermission = "android.car.permission.oem.SPEED_READ",
								writePermission = null,
								unitsOrEnum = "km/h"),
						value = 120f, hasError = false, canRead = true),
				traveledDistance = PropertyValue(
						definition = PropertyDefinition(
								id = 8,
								name = "TRAVELED_DISTANCE",
								type = Float::class,
								readPermission = "android.car.permission.oem.TRAVELED_DISTANCE_READ",
								writePermission = null,
								unitsOrEnum = "km"),
						value = 1234.5f,
						hasError = false,
						canRead = true),
				error = "This is a test message",
				onSwitch = { prop, value ->
					when (prop.definition.uid) {
						adasAbsIsEnabled.definition.uid -> adasAbsIsEnabled =
								adasAbsIsEnabled.copy(value = !value)

						adasCruiseControlIsActive.definition.uid -> adasCruiseControlIsActive =
							adasCruiseControlIsActive.copy(value = !value)

						cabinRearShadeIsOpen.definition.uid -> cabinRearShadeIsOpen =
							cabinRearShadeIsOpen.copy(value = !value)

						cabinSunroofShareIsOpen.definition.uid -> cabinSunroofShareIsOpen =
							cabinSunroofShareIsOpen.copy(value = !value)
					}
				},
				onChange = { prop, value ->
					when (prop.definition.uid) {
						cabinSeatRow1LeftPosition.definition.uid -> cabinSeatRow1LeftPosition =
							cabinSeatRow1LeftPosition.copy(value = value)

						cabinSeatRow1RightPosition.definition.uid -> cabinSeatRow1RightPosition =
							cabinSeatRow1RightPosition.copy(value = value)

						cabinSeatRow1LeftHeight.definition.uid -> cabinSeatRow1LeftHeight =
							cabinSeatRow1LeftHeight.copy(value = value)

						cabinSeatRow1RightHeight.definition.uid -> cabinSeatRow1RightHeight =
							cabinSeatRow1RightHeight.copy(value = value)
					}
				},
				onDismissError = {})
	}
}

@Preview(device = "id:automotive_1408p_landscape_with_google_apis")
@Composable
private fun MainViewNoPermissionsPreview() {
	AppTheme {
		var cabinSeatRow1LeftPosition by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_POSITION",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_LEFT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_POSITION_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_POSITION_WRITE",
							unitsOrEnum = "mm"),
					value = 100,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		var cabinSeatRow1RightPosition by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_POSITION",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_RIGHT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_POSITION_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_POSITION_WRITE",
							unitsOrEnum = "mm"),
					value = 200,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		var cabinSeatRow1LeftHeight by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_HEIGHT",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_LEFT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_WRITE",
							unitsOrEnum = "mm"),
					value = 300,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		var cabinSeatRow1RightHeight by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 1,
							name = "CABIN_SEAT_HEIGHT",
							type = Int::class,
							areaId = VehicleAreaSeat.SEAT_ROW_1_RIGHT,
							readPermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_READ",
							writePermission = "android.car.permission.oem.CABIN_SEAT_HEIGHT_WRITE",
							unitsOrEnum = "mm"),
					value = 400,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		var adasAbsIsEnabled by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 2,
							name = "ADAS_ABS_IS_ENABLED",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.ADAS_ABS_IS_ENABLED_READ",
							writePermission = null,
							unitsOrEnum = "ENABLED|DISABLED"),
					value = false,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		var adasCruiseControlIsActive by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 3,
							name = "ADAS_CRUISE_CONTROL_IS_ACTIVE",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.ADAS_CRUISE_CONTROL_IS_ACTIVE_READ",
							writePermission = "android.car.permission.oem.ADAS_CRUISE_CONTROL_IS_ACTIVE_WRITE",
							unitsOrEnum = "ACTIVE|OFF"),
					value = true,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		var cabinRearShadeIsOpen by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 4,
							name = "CABIN_REAR_SHADE_IS_OPEN",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.CABIN_REAR_SHADE_IS_OPEN_READ",
							writePermission = "android.car.permission.oem.CABIN_REAR_SHADE_IS_OPEN_WRITE",
							unitsOrEnum = "OPEN|CLOSED"),
					value = false,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		var cabinSunroofShareIsOpen by remember {
			mutableStateOf(PropertyValue(
					definition = PropertyDefinition(
							id = 5,
							name = "CABIN_SUNROOF_SHADE_IS_OPEN",
							type = Boolean::class,
							readPermission = "android.car.permission.oem.CABIN_SUNROOF_SHADE_IS_OPEN_READ",
							writePermission = null,
							unitsOrEnum = "OPEN|CLOSED"),
					value = true,
					hasError = false,
					canRead = false,
					canWrite = false))
		}
		MainView(
				cabinSeatRow1LeftHeight = cabinSeatRow1LeftHeight,
				cabinSeatRow1RightHeight = cabinSeatRow1RightHeight,
				cabinSeatRow1LeftPosition = cabinSeatRow1LeftPosition,
				cabinSeatRow1RightPosition = cabinSeatRow1RightPosition,
				adasAbsIsEnabled = adasAbsIsEnabled,
				adasCruiseControlIsActive = adasCruiseControlIsActive,
				cabinRearShadeIsOpen = cabinRearShadeIsOpen,
				cabinSunroofShareIsOpen = cabinSunroofShareIsOpen,
				powertrainFuelSystemAbsoluteLevel = PropertyValue(
						definition = PropertyDefinition(
								id = 6,
								name = "POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL",
								type = Float::class,
								readPermission = "android.car.permission.oem.POWERTRAIN_FUEL_SYSTEM_ABSOLUTE_LEVEL_READ",
								writePermission = null,
								unitsOrEnum = "l"),
						value = 42.5f,
						hasError = false,
						canRead = false),
				speed = PropertyValue(
						definition = PropertyDefinition(
								id = 7,
								name = "SPEED",
								type = Float::class,
								readPermission = "android.car.permission.oem.SPEED_READ",
								writePermission = null,
								unitsOrEnum = "km/h"),
						value = 120f,
						hasError = false,
						canRead = false),
				traveledDistance = PropertyValue(
						definition = PropertyDefinition(
								id = 8,
								name = "TRAVELED_DISTANCE",
								type = Float::class,
								readPermission = "android.car.permission.oem.TRAVELED_DISTANCE_READ",
								writePermission = null,
								unitsOrEnum = "km"),
						value = 1234.5f,
						hasError = false,
						canRead = false),
				error = "This is a test message",
				onSwitch = { prop, value ->
					when (prop.definition.uid) {
						adasAbsIsEnabled.definition.uid -> adasAbsIsEnabled = adasAbsIsEnabled
								.copy(value = !value)

						adasCruiseControlIsActive.definition.uid -> adasCruiseControlIsActive =
							adasCruiseControlIsActive.copy(value = !value)

						cabinRearShadeIsOpen.definition.uid -> cabinRearShadeIsOpen =
							cabinRearShadeIsOpen.copy(value = !value)

						cabinSunroofShareIsOpen.definition.uid -> cabinSunroofShareIsOpen =
							cabinSunroofShareIsOpen.copy(value = !value)
					}
				},
				onChange = { prop, value ->
					when (prop.definition.uid) {
						cabinSeatRow1LeftPosition.definition.uid -> cabinSeatRow1LeftPosition =
								cabinSeatRow1LeftPosition.copy(value = value)

						cabinSeatRow1RightPosition.definition.uid -> cabinSeatRow1RightPosition =
								cabinSeatRow1RightPosition.copy(value = value)

						cabinSeatRow1LeftHeight.definition.uid -> cabinSeatRow1LeftHeight =
								cabinSeatRow1LeftHeight.copy(value = value)

						cabinSeatRow1RightHeight.definition.uid -> cabinSeatRow1RightHeight =
								cabinSeatRow1RightHeight.copy(value = value)
					}
				},
				onDismissError = {})
	}
}

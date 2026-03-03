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

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import global.covesa.aosp.vhal.test.app.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	companion object {
		private val LOG_TAG = MainActivity::class.simpleName
		private const val PERMISSION_REQUEST_CODE: Int = 100

		private val CAR_PERMISSIONS: Array<String?> = arrayOf<String?>(
				"android.car.permission.CAR_MILEAGE",
				"android.car.permission.oem.CABIN_SUNROOF_SHADE_IS_OPEN_READ",
				"android.car.permission.oem.CABIN_SUNROOF_SHADE_IS_OPEN_WRITE",
				"android.car.permission.oem.CABIN_REAR_SHADE_IS_OPEN_READ",
				"android.car.permission.oem.CABIN_REAR_SHADE_IS_OPEN_WRITE",
				"android.car.permission.oem.ADAS_ABS_IS_ENABLED_READ",
				"android.car.permission.oem.ADAS_ABS_IS_ENABLED_WRITE",
				"android.car.permission.oem.ADAS_CRUISE_CONTROL_IS_ACTIVE_READ",
				"android.car.permission.oem.ADAS_CRUISE_CONTROL_IS_ACTIVE_WRITE"
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent { AppTheme { MainView() } }

		Log.d(LOG_TAG, "Checking permissions: START")
		val permissionsToRequest: MutableList<String?> = ArrayList()
		for (permission in CAR_PERMISSIONS) {
			if (checkSelfPermission(permission!!) != PackageManager.PERMISSION_GRANTED) {
				Log.d(LOG_TAG, "${permission} is not granted yet, requesting permission")
				permissionsToRequest.add(permission)
			}
		}
		if (!permissionsToRequest.isEmpty()) {
			requestPermissions(permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
		}
		Log.d(LOG_TAG, "Checking permissions: DONE")

	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
		if (requestCode == PERMISSION_REQUEST_CODE) {
			for (i in permissions.indices) {
				val granted = grantResults[i] == PackageManager.PERMISSION_GRANTED
				Log.d(LOG_TAG, "Permission ${permissions[i]}: ${if (granted) "GRANTED" else "DENIED"}")
			}
		}
	}
}

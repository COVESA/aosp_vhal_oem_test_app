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
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import global.covesa.aosp.vhal.test.app.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	companion object {
		private val LOG_TAG = MainActivity::class.simpleName
		private const val PERMISSION_REQUEST_CODE: Int = 100
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent { AppTheme { MainView() } }
		lifecycleScope.launch {
			while (isActive) {
				checkAndRequestPermissions()
				delay(5000)
			}
		}
	}

	private fun checkAndRequestPermissions() {
		Log.d(LOG_TAG, "Checking permissions: START")
		val permissionsToRequest: MutableList<String?> = ArrayList()
		for (permission in VEHICLE_PROPERTIES.flatMap { it.permissions }) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				Log.d(LOG_TAG, "${permission} is not granted yet, requesting permission")
				permissionsToRequest.add(permission)
			}
		}
		if (!permissionsToRequest.isEmpty()) {
			requestPermissions(permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == PERMISSION_REQUEST_CODE) {
			for (i in permissions.indices) {
				val granted = grantResults[i] == PackageManager.PERMISSION_GRANTED
				Log.d(LOG_TAG, "Permission ${permissions[i]}: ${if (granted) "GRANTED" else "DENIED"}")
			}
		}
	}
}

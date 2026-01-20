package com.kvrae.easykitchen.utils

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * Request location permission with proper UI handling
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {},
    onShowRationale: () -> Unit = {},
) {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            onPermissionGranted()
        }
    }

    LaunchedEffect(permissionsState.revokedPermissions) {
        val needsRationale =
            permissionsState.revokedPermissions.any { it.status.shouldShowRationale }
        if (needsRationale) {
            onShowRationale()
        }
    }

    // Automatically request permission on first composition
    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    if (permissionsState.revokedPermissions.isNotEmpty() && !permissionsState.allPermissionsGranted) {
        onPermissionDenied()
    }
}

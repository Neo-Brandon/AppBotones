package com.example.buttonsapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.buttonsapp.ui.theme.ButtonsAppTheme
import com.example.buttonsapp.components.*


// Extension de DataStore para Context
private val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    private val darkModeKey = booleanPreferencesKey("DARK_MODE_KEY")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkMode = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            // Obteniendo la preferencia inicial de modo oscuro
            LaunchedEffect(Unit) {
                getDarkModePreference().collect { isDarkMode ->
                    darkMode.value = isDarkMode
                }
            }

            ButtonsAppTheme(darkTheme = darkMode.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(
                        darkMode = darkMode,
                        onDarkModeChange = { isDarkMode ->
                            darkMode.value = isDarkMode
                            scope.launch {
                                setDarkModePreference(isDarkMode)
                            }
                        }
                    )
                }
            }
        }
    }

    // obtener el valor del modo oscuro desde DataStore
    private fun getDarkModePreference(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[darkModeKey] ?: false  // Default: modo claro
        }

    // guardar el valor del modo oscuro en DataStore
    private suspend fun setDarkModePreference(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[darkModeKey] = isDarkMode
        }
    }
}

@Composable
fun Content(darkMode: MutableState<Boolean>, onDarkModeChange: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BotonNormal()
        Space()
        BotonNormal2()
        Space()
        BotonTexto()
        Space()
        BotonOutline()
        Space()
        BotonIcono()
        Space()
        BotonSwitch()
        Space()
        DarkMode(darkMode = darkMode, onDarkModeChange = onDarkModeChange)
        Space()
        FloatingAction()
    }
}
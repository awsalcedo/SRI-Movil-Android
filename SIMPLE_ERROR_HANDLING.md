# Manejo Simple de Errores - SRI Móvil Android

## Enfoque Simple y Práctico

Esta implementación sigue el enfoque que un **Senior Android Developer** realmente usaría: **simple, directo y funcional**.

## Componentes Principales

### 1. Result Simple
```kotlin
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
```

### 2. ErrorHandler con strings.xml
```kotlin
@Singleton
class ErrorHandler @Inject constructor() {
    fun getErrorMessage(context: Context, throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> context.getString(R.string.error_no_internet)
            is IOException -> context.getString(R.string.error_network)
            is HttpException -> {
                when (throwable.code()) {
                    401 -> context.getString(R.string.error_unauthorized)
                    404 -> context.getString(R.string.error_not_found)
                    else -> context.getString(R.string.error_unknown)
                }
            }
            else -> context.getString(R.string.error_unknown)
        }
    }
}
```

### 3. BaseViewModel Simple
```kotlin
abstract class BaseViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    protected fun <T> executeOperation(
        operation: suspend () -> Result<T>,
        onSuccess: (T) -> Unit = {},
        onError: (String) -> Unit = { setError(it) }
    ) {
        // Implementation with automatic loading and error handling
    }
}
```

## Flujo de Datos

```
API Call → DataSource → Repository → UseCase → ViewModel → UI
    ↓           ↓           ↓          ↓         ↓        ↓
  Result    Result     Result    Result   StateFlow  Snackbar
```

## Uso en UI

### Con Snackbar
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            viewModel.clearError()
        }
    }
    
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        // Your UI content
    }
}
```

### Con Card de Error
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    Column {
        // Your UI content
        
        errorMessage?.let { message ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
```

## Strings.xml
```xml
<resources>
    <string name="error_no_internet">Sin conexión a internet</string>
    <string name="error_network">Error de red</string>
    <string name="error_unauthorized">No autorizado</string>
    <string name="error_forbidden">Acceso denegado</string>
    <string name="error_not_found">Recurso no encontrado</string>
    <string name="error_server">Error del servidor</string>
    <string name="error_unknown">Error desconocido</string>
</resources>
```

## Ventajas de este Enfoque

1. **Simple**: Fácil de entender y mantener
2. **Práctico**: Resuelve el problema real sin sobre-ingeniería
3. **Localizable**: Errores en strings.xml para múltiples idiomas
4. **Consistente**: Mismo patrón en toda la app
5. **Testeable**: Fácil de hacer unit tests
6. **Escalable**: Fácil agregar nuevos tipos de error

## Ejemplo Completo

```kotlin
// DataSource
class MyDataSource @Inject constructor(private val api: MyApi) {
    suspend fun getData(): Result<MyData> {
        return try {
            val response = api.getData()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

// ViewModel
@HiltViewModel
class MyViewModel @Inject constructor(
    private val useCase: MyUseCase,
    private val errorHandler: ErrorHandler
) : BaseViewModel() {
    
    fun loadData(context: Context) {
        executeOperation(
            operation = { useCase() },
            onSuccess = { data -> /* handle success */ },
            onError = { error -> 
                val message = errorHandler.getErrorMessage(context, Exception(error))
                setError(message)
            }
        )
    }
}
```

Este enfoque es **exactamente** lo que un Senior Android Developer implementaría: **simple, directo y que funciona**.

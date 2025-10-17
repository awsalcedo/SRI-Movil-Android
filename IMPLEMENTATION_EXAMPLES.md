# Ejemplos de Implementación - Sistema Unificado de Manejo de Errores

## 1. DataSource Implementation

### Remote DataSource
```kotlin
class ExampleRemoteDataSourceImpl @Inject constructor(
    private val apiService: ExampleService,
    private val errorHandler: ErrorHandler
) : ExampleRemoteDataSource {
    
    override suspend fun getData(id: String): DataResult<ExampleData, DataError.Network> {
        return safeDataResult(
            errorMapper = errorHandler::handleNetworkError
        ) {
            Timber.d("Fetching data for ID: $id")
            
            val response = apiService.getData(id)
            
            if (response.isSuccessful) {
                response.body()?.toDomain() 
                    ?: throw IllegalStateException("Response body is null")
            } else {
                throw IllegalStateException("API returned error: ${response.code()}")
            }
        }
    }
}
```

### Local DataSource
```kotlin
class ExampleLocalDataSourceImpl @Inject constructor(
    private val dao: ExampleDao,
    private val errorHandler: ErrorHandler
) : ExampleLocalDataSource {
    
    override suspend fun saveData(data: ExampleData): DataResult<Unit, DataError.Local> {
        return safeDataResult(
            errorMapper = errorHandler::handleLocalError
        ) {
            dao.insert(data.toEntity())
            Timber.d("Data saved successfully")
        }
    }
    
    override fun getDataFlow(): Flow<DataResult<List<ExampleData>, DataError.Local>> {
        return dao.getAll()
            .map { entities ->
                try {
                    DataResult.Success(entities.map { it.toDomain() })
                } catch (e: Exception) {
                    DataResult.Error(errorHandler.handleLocalError(e))
                }
            }
            .catch { e ->
                emit(DataResult.Error(errorHandler.handleLocalError(e)))
            }
    }
}
```

## 2. Repository Implementation

```kotlin
class ExampleRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExampleRemoteDataSource,
    private val localDataSource: ExampleLocalDataSource
) : ExampleRepository {
    
    override suspend fun getData(id: String): DataResult<ExampleData, DataError> {
        Timber.d("Repository: Getting data for ID: $id")
        
        // Try local first
        val localResult = localDataSource.getData(id)
        if (localResult.isSuccess) {
            Timber.d("Repository: Data found in local cache")
            return localResult
        }
        
        // Fallback to remote
        val remoteResult = remoteDataSource.getData(id)
        if (remoteResult.isSuccess) {
            // Cache the result
            localDataSource.saveData(remoteResult.data)
            Timber.d("Repository: Data fetched from remote and cached")
        }
        
        return remoteResult
    }
    
    override fun getDataFlow(): Flow<DataResult<List<ExampleData>, DataError>> {
        return localDataSource.getDataFlow()
            .map { result ->
                when (result) {
                    is DataResult.Success -> result
                    is DataResult.Error -> result.mapError { DataError.Local.Unknown() }
                }
            }
    }
}
```

## 3. UseCase Implementation

```kotlin
class GetExampleDataUseCase @Inject constructor(
    private val repository: ExampleRepository
) {
    suspend operator fun invoke(id: String): DataResult<ExampleData, DataError> {
        return repository.getData(id)
            .map { data ->
                // Business logic validation
                if (data.isValid()) {
                    data
                } else {
                    throw IllegalArgumentException("Invalid data format")
                }
            }
            .mapError { error ->
                when (error) {
                    is DataError.Network -> error
                    is DataError.Local -> error
                    else -> DataError.Business.ValidationFailed
                }
            }
    }
}
```

## 4. ViewModel Implementation

```kotlin
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getDataUseCase: GetExampleDataUseCase
) : BaseViewModel<ExampleUiState, ExampleUiEvent>() {
    
    private var lastRequestedId: String? = null
    
    init {
        // Observe data flow
        viewModelScope.launch {
            getDataUseCase.getDataFlow()
                .collect { result ->
                    when (result) {
                        is DataResult.Success -> setSuccess(result.data)
                        is DataResult.Error -> setError(result.error)
                    }
                }
        }
    }
    
    override fun handleFeatureEvent(event: ExampleUiEvent) {
        when (event) {
            is ExampleUiEvent.LoadData -> loadData(event.id)
            is ExampleUiEvent.RefreshData -> refreshData()
            is ExampleUiEvent.ClearCache -> clearCache()
        }
    }
    
    override fun retryLastOperation() {
        lastRequestedId?.let { id ->
            loadData(id)
        }
    }
    
    private fun loadData(id: String) {
        lastRequestedId = id
        executeOperation(
            operation = { getDataUseCase(id) },
            onSuccess = { data ->
                setSuccess(ExampleUiState.Success(data))
            },
            onError = { error, message ->
                setError(error, message)
            }
        )
    }
    
    private fun refreshData() {
        lastRequestedId?.let { id ->
            loadData(id)
        }
    }
    
    private fun clearCache() {
        executeOperation(
            operation = { 
                // Clear cache logic
                Unit 
            },
            onSuccess = {
                setSuccess(ExampleUiState.Idle)
            }
        )
    }
}
```

## 5. UI State Definition

```kotlin
sealed interface ExampleUiState : UiState<ExampleData> {
    data object Idle : ExampleUiState
    data object Loading : ExampleUiState
    data class Success(val data: ExampleData) : ExampleUiState
    data class Error(val error: DataError, val message: String?) : ExampleUiState
}
```

## 6. UI Events Definition

```kotlin
sealed interface ExampleUiEvent : UiEvent {
    data class LoadData(val id: String) : ExampleUiEvent
    data object RefreshData : ExampleUiEvent
    data object ClearCache : ExampleUiEvent
}
```

## 7. Compose UI Implementation

```kotlin
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.handleEvent(ExampleUiEvent.LoadData("example-id"))
    }
    
    when (uiState) {
        is ExampleUiState.Idle -> {
            // Initial state
        }
        
        is ExampleUiState.Loading -> {
            LoadingIndicator()
        }
        
        is ExampleUiState.Success -> {
            DataContent(
                data = uiState.data,
                onRefresh = {
                    viewModel.handleEvent(ExampleUiEvent.RefreshData)
                }
            )
        }
        
        is ExampleUiState.Error -> {
            ErrorContent(
                error = uiState.error,
                message = uiState.message,
                onRetry = {
                    viewModel.handleEvent(CommonUiEvent.Retry)
                },
                onDismiss = {
                    viewModel.handleEvent(CommonUiEvent.DismissError)
                }
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: DataError,
    message: String?,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val errorMessage = when (error) {
        is DataError.Network.NoInternet -> "Sin conexión a internet"
        is DataError.Network.Http.NotFound -> "Recurso no encontrado"
        is DataError.Network.Http.ServerError -> "Error del servidor"
        is DataError.Local.Database -> "Error de base de datos"
        is DataError.Business.ValidationFailed -> "Datos inválidos"
        else -> message ?: "Error desconocido"
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Reintentar")
                }
                
                OutlinedButton(
                    onClick = onDismiss
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}
```

## 8. Testing Examples

### Unit Test for DataSource
```kotlin
@ExtendWith(MockKExtension::class)
class ExampleRemoteDataSourceTest {
    
    @MockK
    private lateinit var apiService: ExampleService
    
    @MockK
    private lateinit var errorHandler: ErrorHandler
    
    private lateinit var dataSource: ExampleRemoteDataSourceImpl
    
    @BeforeEach
    fun setup() {
        dataSource = ExampleRemoteDataSourceImpl(apiService, errorHandler)
    }
    
    @Test
    fun `getData should return success when API call succeeds`() = runTest {
        // Given
        val expectedData = ExampleData("test-id", "test-name")
        val response = Response.success(expectedData.toDto())
        
        coEvery { apiService.getData("test-id") } returns response
        
        // When
        val result = dataSource.getData("test-id")
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedData, result.getOrNull())
    }
    
    @Test
    fun `getData should return error when API call fails`() = runTest {
        // Given
        val exception = HttpException(Response.error<ExampleDto>(404, "Not Found".toResponseBody()))
        val expectedError = DataError.Network.Http.NotFound()
        
        coEvery { apiService.getData("test-id") } throws exception
        every { errorHandler.handleNetworkError(exception) } returns expectedError
        
        // When
        val result = dataSource.getData("test-id")
        
        // Then
        assertTrue(result.isError)
        assertEquals(expectedError, result.getErrorOrNull())
    }
}
```

### Integration Test for Repository
```kotlin
@HiltAndroidTest
class ExampleRepositoryIntegrationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var repository: ExampleRepository
    
    @Before
    fun init() {
        hiltRule.inject()
    }
    
    @Test
    fun `repository should handle network and local errors gracefully`() = runTest {
        // Test network error handling
        val result = repository.getData("invalid-id")
        
        assertTrue(result.isError)
        assertTrue(result.getErrorOrNull() is DataError.Network)
    }
}
```

## 9. Error Handling Best Practices

### 1. Always Use safeDataResult
```kotlin
// ✅ Good
suspend fun operation(): DataResult<Data, DataError.Network> = 
    safeDataResult(errorHandler::handleNetworkError) {
        // risky operation
    }

// ❌ Bad
suspend fun operation(): DataResult<Data, DataError.Network> = 
    try {
        DataResult.Success(riskyOperation())
    } catch (e: Exception) {
        DataResult.Error(DataError.Network.Unknown())
    }
```

### 2. Provide Context in Logs
```kotlin
// ✅ Good
Timber.d("Repository: Getting data for ID: $id")
Timber.w("Repository: Failed to retrieve data: ${result.error}")

// ❌ Bad
Timber.d("Getting data")
Timber.w("Failed")
```

### 3. Use Specific Error Types
```kotlin
// ✅ Good
DataError.Network.Http.NotFound(body = responseBody)
DataError.Business.ValidationFailed

// ❌ Bad
DataError.Network.Unknown()
DataError.Business.Unknown()
```

### 4. Handle Common Events
```kotlin
// ✅ Good
override fun handleFeatureEvent(event: ExampleUiEvent) {
    when (event) {
        is CommonUiEvent.Retry -> retryLastOperation()
        is CommonUiEvent.DismissError -> dismissError()
        // ... feature events
    }
}
```

Este sistema proporciona una base sólida y consistente para el manejo de errores en toda la aplicación, facilitando el desarrollo, testing y mantenimiento.

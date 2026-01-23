# Core: Result + Error Handling + UI Events

Este módulo define un stack consistente para:
- Devolver resultados tipados desde Data/Domain (`DataResult`)
- Modelar errores de aplicación (`AppError`)
- Ejecutar llamadas Retrofit de forma segura (`safeCall`)
- Mapear errores a mensajes de UI localizables (`UiText`, `AppError.toUiText`)
- Consumir eventos one-shot (snackbar/navegación) respetando lifecycle (`ObserveAsEvents`)

El objetivo es evitar:
- Excepciones como control de flujo
- Acoplamiento de UI a Retrofit/OkHttp
- Duplicación de mapeos de errores por feature
- Bugs comunes por recolección de eventos fuera de lifecycle

---

## Componentes

### 1) `DataResult<T, E>`
Representa el resultado de una operación:
- `Success(data: T)`
- `Error(error: E)`

### 2) `AppError`
Modelo de errores tipado:
- `Network`: NoInternet, Timeout, Unavailable
- `Http`: Unauthorized, Forbidden, NotFound, TooManyRequests, Server, Client
- `Local`: errores de DB/almacenamiento
- `Auth`: errores de sesión/refresh
- `Serialization`
- `Unknown`

### 3) `safeCall`
Envuelve una llamada Retrofit y la convierte en `DataResult`:
- Mapea excepciones de red a `AppError.Network`
- Mapea parseo/serialización a `AppError.Serialization`
- Propaga cancelación correctamente
- Delegaa el mapeo de códigos HTTP a `HttpErrorMapper`

### 4) `HttpErrorMapper`
Contrato para mapear `(statusCode, errorBody)` → `AppError`.
Permite extender reglas sin tocar `safeCall`.

### 5) `UiText` + `AppError.toUiText()`
Convierte errores a texto UI localizable sin depender de `Resources`.
En Compose, `UiText.asString()` resuelve el texto.

### 6) `ObserveAsEvents`
Util para coleccionar eventos one-shot (`Flow<Event>`) respetando lifecycle.

---

## Uso recomendado por capas

### Data layer
- Implementar `RemoteDataSource` con Retrofit.
- Usar `safeCall { api.call() }`.
- Retornar `DataResult<Dto, AppError>` o mapear a domain si aplica.

### Domain layer
- UseCases operan sobre `DataResult` y retornan `DataResult<Domain, AppError>`.

### UI layer
- ViewModel expone:
    - `state: StateFlow<UiState>`
    - `events: Flow<UiEvent>` (Channel/SharedFlow)
- UI:
    - renderiza `state`
    - consume `events` con `ObserveAsEvents`
    - usa `error.toUiText().asString()` para snackbars/dialogs

---

## Ejemplo End-to-End

### 1) API (Retrofit)

```kotlin
interface EstadoApi {
    @GET("estado/{ruc}")
    suspend fun getEstadoTributario(@Path("ruc") ruc: String): Response<EstadoTributarioDto>
}

# Propuesta Arquitectónica: Metrolist Multiplatform (Linux/Android)

Para llevar Metrolist a Linux manteniendo una base de código única, se propone una arquitectura basada en **Kotlin Multiplatform (KMP)** con una clara separación de preocupaciones.

## 1. Estructura de Módulos Sugerida

- `:shared`: Contiene toda la lógica común.
    - `commonMain`: Modelos, Clientes de API (InnerTube, etc.), Lógica de ViewModels (si se usa `androidx-lifecycle-viewmodel` multiplataforma).
    - `androidMain`: Implementaciones específicas de Android (Media3, Room Android).
    - `desktopMain`: Implementaciones para Linux/Desktop (libmpv, Room SQLite JVM).
- `:app-android`: El módulo actual de Android (reducido a un "shell").
- `:app-desktop`: El nuevo punto de entrada para la aplicación de Linux.

## 2. Abstracción del Reproductor (Generic Player Interface)

Se debe definir una interfaz en `commonMain` que oculte las complejidades de Media3 y MPV:

```kotlin
interface MusicPlayer {
    val currentSong: StateFlow<Song?>
    val playbackState: StateFlow<PlaybackState>
    val isPlaying: StateFlow<Boolean>
    val volume: MutableStateFlow<Float>

    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Long)
    fun playQueue(queue: Queue)
    fun playNext(items: List<MediaItem>)
    fun addToQueue(items: List<MediaItem>)

    // Observadores de eventos
    fun addListener(listener: PlayerListener)
}
```

## 3. Migración de Inyección de Dependencias (Hilt -> Koin)

Hilt es una limitación importante ya que depende del runtime de Android. Koin es la alternativa recomendada por su ligereza y soporte multiplataforma.

### Plan de Migración:
1.  **Definir módulos comunes**: Crear archivos `Modules.kt` en `commonMain`.
2.  **Inyectar por constructor**: Asegurar que todas las clases reciban sus dependencias por constructor, eliminando `@Inject` de los campos.
3.  **Implementar `expect/actual` para módulos de plataforma**:
    - `expect val platformModule: Module`
    - En Android: Proveer `DatabaseProvider`, `Context`, etc.
    - En Linux: Proveer implementaciones de `LinuxPlayer` y rutas de archivos locales.

## 4. Estrategia para la Base de Datos (Room Multiplatform)

Metrolist ya usa Room. Para el port:
1.  Actualizar a la versión 2.7.0+ de Room que soporta KMP.
2.  Mover las entidades (`@Entity`) y DAOs a `commonMain`.
3.  Usar `RoomDatabase.Builder` con `setDriver` para diferenciar entre el driver de Android y el driver de SQLite nativo para Linux.

## 5. Integración con el Escritorio (MPRIS)

Para que Ubuntu "entienda" qué está sonando, se implementará un bridge MPRIS en el módulo `desktopMain`:
- Usar una librería de DBus para Kotlin.
- Mapear los estados de `MusicPlayer` a las propiedades de `org.mpris.MediaPlayer2.Player`.
- Escuchar comandos de DBus para ejecutar `player.pause()`, `player.next()`, etc.

## 6. Manejo de Imágenes y Cache

- **Coil**: Se mantendrá, pero usando la versión multiplataforma que soporta carga desde URLs en Desktop.
- **Cache de Audio**: Se debe reimplementar la lógica de `SimpleCache` (Media3) usando una estrategia de archivos propia en Linux o aprovechando las capacidades de cache de libmpv.

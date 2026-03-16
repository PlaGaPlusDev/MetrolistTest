# Reporte de Viabilidad Técnica: Port de Metrolist a Linux (Ubuntu)

Este documento detalla el análisis de viabilidad para portar la aplicación Metrolist (cliente de YouTube Music para Android) a plataformas Linux de forma nativa.

## 1. Resumen Ejecutivo
El port nativo a Linux es **altamente viable** gracias al uso actual de Kotlin y Jetpack Compose. La transición hacia **Compose Multiplatform (CMP)** permitiría reutilizar aproximadamente el 70-80% de la lógica de negocio actual. El mayor esfuerzo reside en la abstracción del motor de reproducción y la persistencia de datos.

## 2. Análisis de Dependencias

| Componente | Dependencia Android Actual | Alternativa Linux (Nativa) | Reutilización |
| :--- | :--- | :--- | :--- |
| **Lenguaje** | Kotlin | Kotlin JVM / Native | 100% |
| **Interfaz (UI)** | Jetpack Compose | Compose Multiplatform (Desktop) | ~90% |
| **Audio/Playback** | Media3 / ExoPlayer | libmpv / GStreamer | < 10% |
| **Inyección (DI)** | Dagger Hilt | Koin / Manual | 0% (Requiere migración) |
| **Base de Datos** | Room (Android) | Room (Multiplatform) | ~60% (Esquemas idénticos) |
| **Red** | Ktor (OkHttp) | Ktor (CIO / Libcurl) | ~95% |
| **Imágenes** | Coil | Coil (Multiplatform) | 100% |

## 3. Desafíos Críticos

### A. Abstracción del Reproductor
Metrolist depende profundamente de `androidx.media3`. Para Linux, se debe crear una interfaz de abstracción (`MusicPlayer`) que en Android use Media3 y en Linux use una librería como `mpv-kt` o bindings de VLC.

### B. Ciclo de Vida y Servicios
El concepto de `Service` de Android no existe igual en Linux. En Ubuntu, la aplicación debe integrarse con **MPRIS (Media Player Remote Interfacing Specification)** para permitir el control desde el panel de sonido del sistema y teclas multimedia.

### C. Persistencia
Aunque Room soporta multiplataforma, la configuración del driver de SQLite y las rutas de archivos deben adaptarse a las convenciones de Linux (ej. `~/.local/share/metrolist`).

## 4. Hoja de Ruta (Roadmap) Propuesta

### Fase 1: Modularización y Abstracción (1-2 meses)
1.  Crear módulo `shared` con lógica de InnerTube y modelos.
2.  Implementar abstracción para Inyección de Dependencias (migrar de Hilt a Koin).
3.  Definir interfaces para `Player`, `DownloadManager` y `FileSystem`.

### Fase 2: Implementación Desktop (1-2 meses)
1.  Crear módulo `desktop` usando Compose Multiplatform.
2.  Implementar `LinuxPlayer` usando `libmpv`.
3.  Adaptar la UI para pantallas de escritorio (panel lateral, atajos de teclado).

### Fase 3: Integración y Distribución (1 mes)
1.  Implementar protocolo MPRIS.
2.  Configurar notificaciones nativas de Linux.
3.  Generar instaladores `.deb` y `Flatpak`.

## 5. Estimación de Esfuerzo
- **Complejidad**: Media-Alta.
- **Tiempo estimado**: 3 a 5 meses para una versión estable (MVP).
- **Rendimiento esperado**: Superior a aplicaciones basadas en Electron por ser una solución nativa JVM.

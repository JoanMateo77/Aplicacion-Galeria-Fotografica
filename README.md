# Documentación de Arquitectura - PhotoGallery

## 1. Visión General

Este documento describe la arquitectura de la aplicación **PhotoGallery**. El objetivo del proyecto es establecer una base 
sólida, escalable y mantenible, aplicando los principios de la Arquitectura Limpia (Clean Architecture).

## 2. Objetivos Alcanzados

### Fase 1 y 2: Configuración Base
- Configurar un proyecto Android moderno con Jetpack Compose.
- Implementar la inyección de dependencias con Hilt.
- Configurar y probar una base de datos local con Room.
- Establecer una estructura de paquetes basada en capas (dominio, infraestructura, presentación).
- Declarar los permisos necesarios y los proveedores de archivos para la funcionalidad de la cámara.

### Fase 3: Mejoras de Arquitectura
- Implementar `PhotoSource` enum para tipado fuerte del origen de las fotos.
- Mover validación de límite de fotos al dominio (`AddPhotoUseCase`).
- Implementar `MaxPhotosException` para manejo de errores de negocio.
- Crear `PhotoUiState` para estado unificado de la UI.
- Implementar componentes UI reutilizables (CameraButton, GalleryButton, PhotoCard, PhotoGrid, EmptyState).

## 3. Patrón de Arquitectura

El proyecto sigue un diseño de **Arquitectura Limpia** con una clara separación de responsabilidades entre las siguientes capas:

- **`domain`**: Es el núcleo de la aplicación. No tiene dependencias de Android y contiene la lógica de negocio 
  y los modelos de datos principales.
  - `model/Photo.kt`: Representa el objeto de negocio con campo `source: PhotoSource`.
  - `model/PhotoSource.kt`: Enum que define el origen de la foto (CAMERA, GALLERY).
  - `model/exception/MaxPhotosException.kt`: Excepción de negocio para límite de fotos.
  - `repository/PhotoRepository.kt`: Interfaz que define el contrato para las operaciones de datos.
  - `usecase/`: Clases que contienen la lógica de negocio específica.
    - `AddPhotoUseCase`: Valida el límite de 3 fotos y lanza `MaxPhotosException` si se excede.

- **`infrastructure`**: Contiene las implementaciones concretas de las interfaces del dominio y gestiona las fuentes de datos.
  - `datasource/local`: Componentes de Room (`PhotoDatabase`, `PhotoDao`, `PhotoEntity`).
  - `repository/PhotoRepositoryImpl.kt`: Implementación del `PhotoRepository` que usa `PhotoDao`.
  - `mapper/PhotoMapper.kt`: Convierte entre el modelo de dominio (`Photo`) y la entidad de la base de datos (`PhotoEntity`).

- **`presentation`**: Es responsable de la interfaz de usuario (UI) y de la gestión del estado.
  - `state/PhotoUiState.kt`: Estado unificado de la UI (photos, isLoading, errorMessage, maxPhotosReached).
  - `viewmodel/PhotoViewModel.kt`: Gestiona el estado usando `PhotoUiState` y se comunica con la capa de dominio.
  - `ui/components/`: Componentes reutilizables (CameraButton, GalleryButton, PhotoCard, PhotoGrid, EmptyState).
  - `ui/screen/PhotoGalleryScreen.kt`: Pantalla principal que usa los componentes y establece `PhotoSource`.
  - `navigation/NavGraph.kt`: El grafo de navegación de la app.
  - `MainActivity.kt`: Punto de entrada de la UI que observa el `ViewModel`.

## 4. Componentes y Tecnologías Clave

- **Lenguaje:** 100% Kotlin.
- **UI:** Jetpack Compose.
- **Inyección de Dependencias (Hilt):** Gestiona las dependencias en toda la aplicación.
- **Base de Datos (Room):** Para la persistencia de datos local.
- **Asincronía:** Corrutinas de Kotlin.
- **Navegación:** Navigation-Compose.
- **Carga de Imágenes:** Coil.
- **Pruebas:** JUnit4 y Robolectric (ver `TESTING.md` para más detalles).

## 5. Estructura de Paquetes

```
com.proyecto.photogallery/
├── application/      # Clase Application de Android y DI.
│   └── di/          # Módulos de Hilt (DatabaseModule, RepositoryModule)
├── domain/
│   ├── model/
│   │   ├── Photo.kt
│   │   ├── PhotoSource.kt
│   │   └── exception/
│   │       └── MaxPhotosException.kt
│   ├── usecase/     # Casos de uso con lógica de negocio
│   └── repository/  # Interfaces de repositorios
├── infrastructure/
│   ├── datasource/
│   │   └── local/   # Room (PhotoDatabase, PhotoDao, PhotoEntity)
│   ├── mapper/       # PhotoMapper (conversión domain ↔ entity)
│   └── repository/   # PhotoRepositoryImpl
└── presentation/
    ├── state/        # PhotoUiState
    ├── viewmodel/    # PhotoViewModel
    ├── ui/
    │   ├── components/  # Componentes reutilizables
    │   └── screen/      # Pantallas
    └── navigation/    # NavGraph
```

## 6. Pruebas (Testing)

La calidad del código se asegura mediante tests unitarios que validan múltiples capas de la arquitectura.

- **Ubicación:** Los tests residen en `app/src/test/`.
- **Estrategia:** Se utilizan tests locales (ejecutados en la JVM) para una validación rápida y eficiente.
- **Cobertura:**
  - **Infraestructura:** Tests de Repository y DAO con Room en memoria.
  - **Mapper:** Tests de conversión entre modelos de dominio y entidades.
  - **Presentación:** Tests de ViewModel con repositorios mockeados.
- **Tecnologías:** JUnit4, Robolectric, Kotlin Coroutines Test, y Room en memoria.

Para una guía detallada sobre cómo configurar y ejecutar los tests, consulta el archivo **`TESTING.md`**.

## 7. Fase 3: Detalles Técnicos de la Capa de Presentación

Esta fase se centró en construir la interfaz de usuario y conectarla con la lógica de negocio. A continuación, se detallan los componentes técnicos clave implementados:

-   **Introducción de Casos de Uso (Use Cases):**
    -   Se ha creado una nueva capa en `domain/usecase` para una mejor adherencia a los principios de la Arquitectura Limpia.
    -   Cada caso de uso (ej. `AddPhotoUseCase`) tiene una única responsabilidad y se inyecta en el ViewModel usando el constructor y la anotación `@Inject`. Esto facilita las pruebas y la reutilización de la lógica de negocio.
    -   **`AddPhotoUseCase`** ahora contiene la validación del límite de 3 fotos, moviendo la lógica de negocio desde el ViewModel al dominio.

-   **Implementación del `PhotoViewModel`:**
    -   Se anota la clase con `@HiltViewModel` para permitir que Hilt gestione su ciclo de vida y le inyecte dependencias (los casos de uso).
    -   El estado de la UI se gestiona con **`PhotoUiState`**, un estado unificado que incluye: `photos`, `isLoading`, `errorMessage`, y `maxPhotosReached`.
    -   Se expone un único `StateFlow<PhotoUiState>` siguiendo el patrón de diseño **UDF (Unidirectional Data Flow)**.
    -   Maneja correctamente `MaxPhotosException` lanzada por `AddPhotoUseCase`.
    -   Todas las operaciones que interactúan con el dominio se ejecutan dentro de un `viewModelScope.launch`.

-   **Componentes UI Reutilizables:**
    -   **`CameraButton`**: Botón reutilizable para tomar fotos.
    -   **`GalleryButton`**: Botón reutilizable para seleccionar de galería.
    -   **`PhotoCard`**: Tarjeta individual de foto con Material3 Card.
    -   **`PhotoGrid`**: Grid reutilizable de fotos usando LazyVerticalGrid.
    -   **`EmptyState`**: Componente para mostrar estado vacío cuando no hay fotos.

-   **Construcción de la UI con Jetpack Compose:**
    -   `PhotoGalleryScreen` usa los componentes reutilizables y obtiene el `PhotoViewModel` a través de `hiltViewModel()`.
    -   El estado se consume de forma reactiva utilizando `collectAsState()`.
    -   Establece `PhotoSource` (CAMERA o GALLERY) cuando se crean fotos.
    -   Los botones se deshabilitan automáticamente cuando se alcanza el límite de 3 fotos.

-   **Navegación con Navigation-Compose:**
    -   Se ha creado un `NavGraph` centralizado que define las rutas de la aplicación.
    -   Se utiliza un `NavHost` para gestionar las transiciones entre los diferentes composables de pantalla.
    -   `MainActivity` se anota con `@AndroidEntryPoint` y se convierte en el contenedor del `NavGraph`.

## 8. Fase 4: Verificación de la Inyección de Dependencias

En esta fase se consolidó y verificó la configuración completa de Hilt en el proyecto.

-   **Verificación de Componentes:** Se confirmó que todos los puntos de entrada (`PhotoGalleryApplication`, `MainActivity`) y las clases inyectadas (`PhotoViewModel`, Casos de Uso) están correctamente anotados.
-   **Módulos de Hilt:** 
    -   `DatabaseModule`: Proporciona `PhotoDatabase` y `PhotoDao`.
    -   `RepositoryModule`: Vincula `PhotoRepository` con `PhotoRepositoryImpl`.
    -   Los módulos vacíos (`UseCaseModule`, `AppModule`) fueron eliminados ya que Hilt inyecta automáticamente los UseCases mediante `@Inject` en sus constructores.
-   **Actualización de Dependencias:** Se actualizaron las dependencias de Hilt a la última versión estable y se confirmó el uso del compilador KSP para un mejor rendimiento.
-   **Validación Final:** Se ejecutó una compilación limpia (`clean build`), demostrando que Hilt puede resolver y proveer correctamente todo el árbol de dependencias de la aplicación sin errores.

## 9. Fase 5: Implementación y Verificación de Funcionalidades

En esta fase se implementaron las funcionalidades interactivas y se solucionaron los problemas surgidos durante las pruebas.

-   **Implementación de Cámara y Galería:**
    -   Se utilizó `rememberLauncherForActivityResult` con los contratos `GetContent` y `TakePicture` para manejar la selección de imágenes y la captura con la cámara.
    -   Se implementó la solicitud de permisos de cámara en tiempo de ejecución.
    -   Se configuró el `FileProvider` para generar URIs seguros para la cámara, asegurando la compatibilidad con las versiones modernas de Android.
    -   Cada foto creada incluye `PhotoSource` (CAMERA o GALLERY) para trazabilidad.

-   **Mejoras de Arquitectura:**
    -   **Validación en el Dominio:** La validación del límite de 3 fotos se movió del ViewModel al `AddPhotoUseCase`, respetando Clean Architecture.
    -   **Manejo de Errores:** `MaxPhotosException` se lanza desde el UseCase y se maneja correctamente en el ViewModel.
    -   **Estado Unificado:** `PhotoUiState` unifica el estado de la UI, facilitando el manejo y las pruebas.
    -   **Componentes Reutilizables:** Los componentes UI están implementados y listos para reutilización.

-   **Depuración y Corrección de Errores:**
    -   **Build:** Se resolvió un error de compilación actualizando el **Android Gradle Plugin** y la **versión de Gradle**.
    -   **Layout de Compose:** Se solucionó un bug visual donde los botones de acción desaparecían.
    -   **Verificación Final:** Se confirmó que la aplicación es completamente funcional en un dispositivo físico.

## 10. Fase 6: Refinamiento y Mejoras

En esta fase se realizaron mejoras adicionales para fortalecer la arquitectura y la calidad del código.

-   **Modelo de Dominio Mejorado:**
    -   Implementación de `PhotoSource` enum para tipado fuerte.
    -   Campo `source` agregado al modelo `Photo`.
    -   `PhotoMapper` actualizado para manejar la conversión de `PhotoSource`.

-   **Limpieza de Código:**
    -   Eliminación de archivos vacíos y no utilizados.
    -   Eliminación de módulos Hilt vacíos.
    -   Organización mejorada de componentes UI.

-   **Tests Actualizados:**
    -   Tests de ViewModel actualizados para usar `PhotoSource`.
    -   Tests de Mapper actualizados para probar conversión de `PhotoSource`.
    -   Todos los tests pasan correctamente.

## 11. Conclusión Final

El proyecto ha alcanzado una etapa de madurez arquitectónica y funcional. Todas las capas están definidas, implementadas y conectadas a través de Hilt. La aplicación es completamente funcional, permitiendo al usuario añadir fotos desde la cámara o la galería, verlas en una cuadrícula y eliminarlas. La base de código, verificada por tests unitarios y validada en un dispositivo real, es robusta, escalable y está lista para futuras expansiones.

**Principios de Clean Architecture respetados:**
- ✅ Lógica de negocio en el dominio (UseCases).
- ✅ Domain sin dependencias de Android.
- ✅ Separación clara de responsabilidades.
- ✅ Componentes reutilizables y testeables.

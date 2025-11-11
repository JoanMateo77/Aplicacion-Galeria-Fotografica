# Documentación de Arquitectura - PhotoGallery

## 1. Visión General

Este documento describe la arquitectura de la aplicación **PhotoGallery**. El objetivo del proyecto es establecer una base 
sólida, escalable y mantenible, aplicando los principios de la Arquitectura Limpia (Clean Architecture).

## 2. Objetivos Alcanzados (Fase 1 y 2)

- Configurar un proyecto Android moderno con Jetpack Compose.
- Implementar la inyección de dependencias con Hilt.
- Configurar y probar una base de datos local con Room.
- Establecer una estructura de paquetes basada en capas (dominio, infraestructura, presentación).
- Declarar los permisos necesarios y los proveedores de archivos para la funcionalidad de la cámara.

## 3. Patrón de Arquitectura

El proyecto sigue un diseño de **Arquitectura Limpia** con una clara separación de responsabilidades entre las siguientes capas:

- **`domain`**: Es el núcleo de la aplicación. No tiene dependencias de Android y contiene la lógica de negocio 
  y los modelos de datos principales.
  - `model/Photo.kt`: Representa el objeto de negocio.
  - `repository/PhotoRepository.kt`: Interfaz que define el contrato para las operaciones de datos.
  - `usecase/`: Clases que contienen la lógica de negocio específica.

- **`infrastructure`**: Contiene las implementaciones concretas de las interfaces del dominio y gestiona las fuentes de datos.
  - `datasource/local`: Componentes de Room (`PhotoDatabase`, `PhotoDao`, `PhotoEntity`).
  - `repository/PhotoRepositoryImpl.kt`: Implementación del `PhotoRepository` que usa `PhotoDao`.
  - `mapper/PhotoMapper.kt`: Convierte entre el modelo de dominio (`Photo`) y la entidad de la base de datos (`PhotoEntity`).

- **`presentation`**: Es responsable de la interfaz de usuario (UI) y de la gestión del estado.
  - `viewmodel/PhotoViewModel.kt`: Gestiona la lógica de la UI y se comunica con la capa de dominio.
  - `MainActivity.kt`: Punto de entrada de la UI que observa el `ViewModel`.
  - `ui/screen/`: Componentes y pantallas de Compose.
  - `navigation/`: El grafo de navegación de la app.

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
├── domain/
│   ├── model/
│   ├── usecase/
│   └── repository/
├── infrastructure/
│   ├── datasource/
│   ├── mapper/
│   └── repository/
└── presentation/
    ├── viewmodel/
    ├── ui/
    └── navigation/
```

## 6. Pruebas (Testing)

La calidad del código se asegura mediante tests unitarios que validan la capa de infraestructura de forma aislada.

- **Ubicación:** Los tests residen en `app/src/test/`.
- **Estrategia:** Se utilizan tests locales (ejecutados en la JVM) para una validación rápida y eficiente.
- **Tecnologías:** Se usa una combinación de **JUnit4**, **Robolectric** (para simular el entorno Android) 
- y una base de datos **Room en memoria** para garantizar que los tests son rápidos, repetibles y no dejan artefactos.

Para una guía detallada sobre cómo configurar y ejecutar los tests, consulta el archivo **`TESTING.md`**.

## 7. Fase 3: Detalles Técnicos de la Capa de Presentación

Esta fase se centró en construir la interfaz de usuario y conectarla con la lógica de negocio. A continuación, se detallan los componentes técnicos clave implementados:

-   **Introducción de Casos de Uso (Use Cases):**
    -   Se ha creado una nueva capa en `domain/usecase` para una mejor adherencia a los principios de la Arquitectura Limpia.
    -   Cada caso de uso (ej. `AddPhotoUseCase`) tiene una única responsabilidad y se inyecta en el ViewModel usando el constructor y la anotación `@Inject`. Esto facilita las pruebas y la reutilización de la lógica de negocio.

-   **Implementación del `PhotoViewModel`:**
    -   Se anota la clase con `@HiltViewModel` para permitir que Hilt gestione su ciclo de vida y le inyecte dependencias (los casos de uso).
    -   El estado de la UI se gestiona con `MutableStateFlow` y se expone a la UI como un `StateFlow` inmutable, siguiendo el patrón de diseño **UDF (Unidirectional Data Flow)**.
    -   Todas las operaciones que interactúan con el dominio se ejecutan dentro de un `viewModelScope.launch`, asegurando que se realicen en un hilo secundario y se cancelen automáticamente si el ViewModel se destruye.

-   **Construcción de la UI con Jetpack Compose:**
    -   Se crea el composable `PhotoGalleryScreen`, que obtiene una instancia del `PhotoViewModel` a través de la función `hiltViewModel()`.
    -   El estado del `ViewModel` se consume en la UI de forma reactiva utilizando `collectAsState()`, lo que provoca recomposiciones automáticas cuando el `StateFlow` emite un nuevo valor.
    -   Para mostrar la lista de imágenes de forma eficiente, se utiliza una `LazyVerticalGrid`.
    -   Las imágenes se cargan de forma asíncrona desde su URI utilizando la función `rememberAsyncImagePainter` de la librería Coil.

-   **Navegación con Navigation-Compose:**
    -   Se ha creado un `NavGraph` centralizado que define las rutas de la aplicación.
    -   Se utiliza un `NavHost` para gestionar las transiciones entre los diferentes composables de pantalla.
    -   `MainActivity` se anota con `@AndroidEntryPoint` y se convierte en el contenedor del `NavGraph`, iniciando el flujo de la UI.

## 8. Fase 4: Verificación de la Inyección de Dependencias

En esta fase se consolidó y verificó la configuración completa de Hilt en el proyecto.

-   **Verificación de Componentes:** Se confirmó que todos los puntos de entrada (`PhotoGalleryApplication`, `MainActivity`) y las clases inyectadas (`PhotoViewModel`, Casos de Uso) están correctamente anotados.
-   **Actualización de Dependencias:** Se actualizaron las dependencias de Hilt a la última versión estable (`2.51.1`) y se confirmó el uso del compilador KSP para un mejor rendimiento.
-   **Validación Final:** Se ejecutó una compilación limpia (`clean build`), demostrando que Hilt puede resolver y proveer correctamente todo el árbol de dependencias de la aplicación sin errores.

## 9. Conclusión Final

El proyecto ha alcanzado una etapa de madurez arquitectónica. Todas las capas están definidas, implementadas y conectadas a través de Hilt. La base de código, verificada por tests unitarios, es robusta, escalable y está lista para la implementación de las funcionalidades finales, como la integración con la cámara y la galería.

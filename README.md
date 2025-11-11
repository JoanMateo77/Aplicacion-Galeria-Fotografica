# Documentación de Arquitectura - Fase 1: Configuración del Proyecto

## 1. Visión General

Este documento describe la arquitectura inicial de la aplicación **PhotoGallery**. El objetivo de esta primera fase ha sido establecer una base de proyecto sólida, escalable y mantenible, aplicando los principios de la Arquitectura Limpia (Clean Architecture).

## 2. Objetivos de la Fase 1

- Configurar un proyecto Android moderno con Jetpack Compose.
- Implementar la inyección de dependencias con Hilt.
- Configurar una base de datos local con Room.
- Establecer una estructura de paquetes basada en capas.
- Declarar los permisos necesarios y los proveedores de archivos para la funcionalidad de la cámara.

## 3. Patrón de Arquitectura

El proyecto sigue un diseño de **Arquitectura Limpia** con una clara separación de responsabilidades entre las siguientes capas:

- **`domain`**: Es el núcleo de la aplicación. No tiene dependencias de Android y contiene la lógica de negocio y los modelos de datos principales.
  - `model/Photo.kt`: Representa el objeto de negocio.
  - `repository/PhotoRepository.kt`: Interfaz que define el contrato para las operaciones de datos, independientemente de dónde vengan.

- **`infrastructure`**: Contiene las implementaciones concretas de las interfaces del dominio y gestiona las fuentes de datos.
  - `datasource/local`: Componentes de Room (`PhotoDatabase`, `PhotoDao`, `PhotoEntity`).
  - `repository/PhotoRepositoryImpl.kt`: Implementación del `PhotoRepository` que usa `PhotoDao` para interactuar con la base de datos.
  - `mapper/PhotoMapper.kt`: Funciones de extensión para convertir entre el modelo de dominio (`Photo`) y la entidad de la base de datos (`PhotoEntity`).

- **`presentation`**: Es responsable de la interfaz de usuario (UI) y de la gestión del estado.
  - `viewmodel/PhotoViewModel.kt`: Gestiona la lógica de la UI y se comunica con la capa de dominio a través del `PhotoRepository`.
  - `MainActivity.kt`: Punto de entrada de la UI que observa el `ViewModel`.

## 4. Componentes y Tecnologías Clave

- **Lenguaje:** 100% Kotlin.
- **UI:** Jetpack Compose.
- **Inyección de Dependencias (Hilt):**
  - `@HiltAndroidApp` en `PhotoGalleryApplication` para inicializar Hilt.
  - `@AndroidEntryPoint` en `MainActivity` para permitir la inyección.
  - `@HiltViewModel` en `PhotoViewModel` para inyectar dependencias en el ViewModel.
  - **Módulos de Hilt (`di`):**
    - `DatabaseModule`: Provee la instancia de la base de datos (`PhotoDatabase`) y el DAO (`PhotoDao`).
    - `RepositoryModule`: Vincula la interfaz `PhotoRepository` con su implementación `PhotoRepositoryImpl`.

- **Base de Datos (Room):**
  - Se utiliza para la persistencia de datos local. La configuración separa la entidad de la base de datos (`PhotoEntity`) del modelo de dominio (`Photo`), lo que permite que el dominio permanezca independiente de la fuente de datos.

- **Carga de Imágenes (Coil):**
  - Se ha integrado `io.coil-kt:coil-compose` para gestionar la carga y visualización asíncrona de imágenes. Es una librería moderna y eficiente, diseñada para Compose.

- **Gestión de Permisos en Tiempo de Ejecución (Accompanist):**
  - Se utiliza `com.google.accompanist:accompanist-permissions` para solicitar los permisos necesarios (cámara, almacenamiento) de una manera declarativa y compatible con Compose.
  - **Nota importante:** Esta librería está obsoleta (deprecated). Se deberá planificar su migración a las APIs de permisos nativas de Jetpack Compose en futuras fases para garantizar la compatibilidad y el mantenimiento a largo plazo.

- **Gestión de Configuración:**
  - **Gradle (Kotlin DSL):** Se utiliza `build.gradle.kts` para una gestión de dependencias y configuración de compilación segura y tipada.
  - **Permisos:** Se han declarado los permisos de `CAMERA` y `READ_MEDIA_IMAGES` en el `AndroidManifest.xml`.
  - **FileProvider:** Configurado para permitir el almacenamiento seguro de imágenes tomadas con la cámara.

## 5. Estructura de Paquetes

La estructura de paquetes refleja la separación de capas:

```
com.proyecto.photogallery/
├── application/      # Clase Application de Android.
├── di/               # Módulos de Hilt para inyección de dependencias.
├── domain/
│   ├── model/        # Modelos de datos del negocio.
│   └── repository/   # Interfaces de los repositorios.
├── infrastructure/
│   ├── datasource/   # Fuentes de datos (Room, API remota, etc.).
│   ├── mapper/       # Conversores entre modelos de datos.
│   └── repository/   # Implementaciones de los repositorios.
└── presentation/
    ├── viewmodel/    # ViewModels de la UI.
    └── (futuro: ui/) # Componentes de Compose, pantallas, etc.
```

## 6. Conclusión de la Fase 1

El proyecto cuenta ahora con una base arquitectónica robusta que facilita el desarrollo futuro. La separación de capas, junto con la inyección de dependencias, permite una alta cohesión, un bajo acoplamiento y una excelente capacidad de prueba.

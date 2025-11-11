# Documentación de Arquitectura - PhotoGallery

## 1. Visión General

Este documento describe la arquitectura de la aplicación **PhotoGallery**. El objetivo del proyecto es establecer una base sólida, escalable y mantenible, aplicando los principios de la Arquitectura Limpia (Clean Architecture).

## 2. Objetivos Alcanzados

- Configurar un proyecto Android moderno con Jetpack Compose.
- Implementar la inyección de dependencias con Hilt.
- Configurar y probar una base de datos local con Room.
- Establecer una estructura de paquetes basada en capas (dominio, infraestructura, presentación).
- Declarar los permisos necesarios y los proveedores de archivos para la funcionalidad de la cámara.

## 3. Patrón de Arquitectura

El proyecto sigue un diseño de **Arquitectura Limpia** con una clara separación de responsabilidades entre las siguientes capas:

- **`domain`**: Es el núcleo de la aplicación. No tiene dependencias de Android y contiene la lógica de negocio y los modelos de datos principales.
  - `model/Photo.kt`: Representa el objeto de negocio.
  - `repository/PhotoRepository.kt`: Interfaz que define el contrato para las operaciones de datos.

- **`infrastructure`**: Contiene las implementaciones concretas de las interfaces del dominio y gestiona las fuentes de datos.
  - `datasource/local`: Componentes de Room (`PhotoDatabase`, `PhotoDao`, `PhotoEntity`).
  - `repository/PhotoRepositoryImpl.kt`: Implementación del `PhotoRepository` que usa `PhotoDao`.
  - `mapper/PhotoMapper.kt`: Convierte entre el modelo de dominio (`Photo`) y la entidad de la base de datos (`PhotoEntity`).

- **`presentation`**: Es responsable de la interfaz de usuario (UI) y de la gestión del estado.
  - `viewmodel/PhotoViewModel.kt`: Gestiona la lógica de la UI y se comunica con la capa de dominio.
  - `MainActivity.kt`: Punto de entrada de la UI que observa el `ViewModel`.

## 4. Componentes y Tecnologías Clave

- **Lenguaje:** 100% Kotlin.
- **UI:** Jetpack Compose.
- **Inyección de Dependencias (Hilt):**
  - Módulos (`DatabaseModule`, `RepositoryModule`) para proveer y vincular dependencias.
- **Base de Datos (Room):**
  - Se utiliza para la persistencia de datos local, con una clara separación entre la entidad (`PhotoEntity`) y el modelo de dominio (`Photo`).
- **Carga de Imágenes (Coil):**
  - `io.coil-kt:coil-compose` para la carga y visualización asíncrona de imágenes.
- **Gestión de Permisos (Accompanist):**
  - Se utiliza `com.google.accompanist:accompanist-permissions`. **Nota:** Esta librería está obsoleta y se deberá migrar a las APIs nativas de Jetpack Compose.
- **Gestión de Configuración:**
  - **Gradle (Kotlin DSL):** Uso de `build.gradle.kts` para una gestión de dependencias tipada.
  - **FileProvider:** Configurado para el almacenamiento seguro de imágenes.

## 5. Estructura de Paquetes

```
com.proyecto.photogallery/
├── application/      # Clase Application de Android y DI.
├── domain/
│   ├── model/
│   └── repository/
├── infrastructure/
│   ├── datasource/
│   ├── mapper/
│   └── repository/
└── presentation/
    └── viewmodel/
```

## 6. Pruebas (Testing)

La calidad del código se asegura mediante tests unitarios que validan la capa de infraestructura de forma aislada.

- **Ubicación:** Los tests residen en `app/src/test/`.
- **Estrategia:** Se utilizan tests locales (ejecutados en la JVM) para una validación rápida y eficiente.
- **Tecnologías:** Se usa una combinación de **JUnit4**, **Robolectric** (para simular el entorno Android) y una base de datos **Room en memoria** para garantizar que los tests son rápidos, repetibles y no dejan artefactos.

Para una guía detallada sobre cómo configurar y ejecutar los tests, consulta el archivo **`TESTING.md`**.

## 7. Conclusión

El proyecto cuenta ahora con una base arquitectónica robusta y **verificada por tests**, que facilita el desarrollo futuro. La separación de capas, junto con la inyección de dependencias y una estrategia de pruebas sólida, permite una alta cohesión, un bajo acoplamiento y una excelente capacidad de prueba y mantenimiento.

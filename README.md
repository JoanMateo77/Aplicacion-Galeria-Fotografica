# PhotoGallery: galería de fotos para Android

Aplicación Android en Kotlin y Jetpack Compose que permite tomar fotos con la cámara o elegirlas de la galería del teléfono, guardarlas en una base de datos local y verlas en una cuadrícula. Es un proyecto de práctica de **Clean Architecture** con Hilt y Room, con pruebas unitarias en la JVM.

## Qué hace

- **Tomar foto**: pide el permiso de cámara en tiempo de ejecución, crea un archivo temporal y abre la cámara del sistema (`ActivityResultContracts.TakePicture`) con un URI seguro generado por `FileProvider`.
- **Seleccionar foto**: abre el selector de imágenes del sistema (`ActivityResultContracts.GetContent`).
- **Ver la galería**: muestra las fotos guardadas en una cuadrícula de 3 columnas (`LazyVerticalGrid`), cargadas con Coil.
- **Eliminar la última foto** de la cuadrícula.
- **Límite de 3 fotos**: al intentar agregar una cuarta, la pantalla muestra un mensaje de error.

Todo ocurre en una sola pantalla (`PhotoGalleryScreen`). Room guarda el URI de cada foto, no una copia de la imagen.

## Stack

| Área | Tecnología | Versión |
|---|---|---|
| Lenguaje | Kotlin (JVM 17) | 1.9.20 |
| Build | Android Gradle Plugin / Gradle | 8.4.1 / 8.7 |
| SDK | minSdk / targetSdk / compileSdk | 26 / 34 / 34 |
| UI | Jetpack Compose (BOM) + Material 3 | 2024.02.00 |
| Navegación | Navigation Compose | 2.7.7 |
| Inyección de dependencias | Hilt (con KSP) | 2.51.1 |
| Persistencia | Room | 2.6.1 |
| Imágenes | Coil | 2.6.0 |
| Permisos | Accompanist Permissions | 0.32.0 |
| Pruebas | JUnit 4 / Robolectric / kotlinx-coroutines-test | 4.13.2 / 4.12.1 / 1.7.3 |

Las versiones salen de `build.gradle.kts` y `app/build.gradle.kts`.

## Arquitectura

El código está en `app/src/main/java/com/proyecto/photogallery/`, separado en capas. El dominio no depende de Android ni de Room; la infraestructura implementa sus interfaces y Hilt las conecta.

```
com.proyecto.photogallery/
├── application/           Clase Application (@HiltAndroidApp) y módulos de Hilt
│   └── di/                DatabaseModule (Room), RepositoryModule (@Binds interfaz -> implementación)
├── domain/                Núcleo sin dependencias de Android
│   ├── model/             Photo (id, uri, fecha)
│   ├── repository/        PhotoRepository: contrato de acceso a datos
│   └── usecase/           AddPhoto, DeletePhoto, GetPhotos, GetPhotoCount
├── infrastructure/        Implementaciones concretas
│   ├── datasource/local/  PhotoDatabase, PhotoDao, PhotoEntity (Room)
│   ├── mapper/            PhotoMapper: Photo <-> PhotoEntity
│   └── repository/        PhotoRepositoryImpl: implementa PhotoRepository sobre el DAO
└── presentation/          UI y estado
    ├── viewmodel/         PhotoViewModel (@HiltViewModel), expone StateFlow
    ├── ui/screen/         PhotoGalleryScreen (Compose)
    ├── navigation/        NavGraph con NavHost
    └── utils/             Archivo temporal y URI de FileProvider para la cámara
```

Flujo de una foto nueva: `PhotoGalleryScreen` -> `PhotoViewModel.addPhoto` -> `GetPhotoCountUseCase` / `AddPhotoUseCase` -> `PhotoRepository` (interfaz) -> `PhotoRepositoryImpl` -> `PhotoMapper` -> `PhotoDao` (Room).

## Cómo ejecutarlo

Requisitos: Android Studio con JDK 17 y un dispositivo o emulador con Android 8.0 (API 26) o superior. Para probar la cámara conviene un dispositivo físico.

1. Clona el repositorio y ábrelo en Android Studio (**File > Open**). Gradle descarga las dependencias al sincronizar.
2. Ejecuta la configuración `app` (**Run**).

Desde la terminal:

```bash
./gradlew assembleDebug     # genera app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug      # instala en el dispositivo conectado
./gradlew test              # pruebas unitarias en la JVM
```

## Pruebas

Las pruebas están en `app/src/test/` y corren en la JVM, sin emulador:

- `PhotoRepositoryTest`: inserta, lee y borra con una base Room en memoria (Robolectric).
- `PhotoMapperTest`: conversión entre `Photo` y `PhotoEntity` en ambos sentidos.
- `PhotoViewModelTest`: agregar y eliminar fotos, y el error al superar el límite de 3, con un `FakePhotoRepository`.

La configuración del entorno de pruebas se explica en [TESTING.md](TESTING.md).

## Decisiones técnicas

- **Casos de uso de una sola responsabilidad** inyectados en el ViewModel: la UI no conoce el repositorio y cada operación se prueba por separado.
- **Interfaz de repositorio en el dominio y `@Binds` en Hilt**: cambiar Room por otra fuente de datos solo toca la capa de infraestructura.
- **Mapper explícito entre modelo de dominio y entidad de Room**: las anotaciones de persistencia no llegan al dominio.
- **Contratos de Activity Result y `FileProvider`** para cámara y galería, en lugar de una cámara propia: menos código y compatible con versiones recientes de Android.

## Estado

Proyecto académico funcional en su flujo principal (tomar, seleccionar, ver y eliminar fotos). Partes que siguen a medias:

- Hay clases creadas como estructura y aún vacías: `CameraDataSource`, `GalleryDataSource`, `LocalPhotoDataSource`, `CameraScreen`, los componentes de `ui/components/`, `PhotoSource`, `PhotoUiState`, `AppModule` y `UseCaseModule`.
- El límite de 3 fotos vive en el ViewModel; `MaxPhotosException` existe pero aún no se usa en el dominio.
- El campo `source` (cámara o galería) de `PhotoEntity` se guarda vacío porque el modelo de dominio todavía no lo tiene.
- Las fotos elegidas de la galería se guardan por URI sin pedir permiso persistente, así que pueden dejar de mostrarse al reiniciar la app.
- Las dependencias de CameraX están declaradas pero no se usan, y `gradle/libs.versions.toml` es un archivo de plantilla que el build actual no lee.

---

Hecho por [Joan Mateo Cardona](https://github.com/JoanMateo77).

# Guía de Pruebas (Testing) - PhotoGallery

Este documento detalla la estrategia, configuración y ejecución de los tests automatizados en el proyecto PhotoGallery.

## 1. Filosofía de Pruebas

El objetivo es asegurar la fiabilidad y robustez de la aplicación a través de **tests unitarios locales**. Estos tests se ejecutan en la JVM, lo que los hace extremadamente rápidos y perfectos para la integración continua (CI) y para ser ejecutados frecuentemente durante el desarrollo.

La estrategia se centra en probar cada capa de la arquitectura de forma aislada:
- **Capa de Infraestructura:** Se verifica que la base de datos (Room) y las implementaciones de los repositorios funcionan como se espera.
- **Capa de Dominio:** (Futuro) Se probará la lógica de negocio pura en los casos de uso.
- **Capa de Presentación:** (Futuro) Se probará el comportamiento de los ViewModels y su gestión del estado.

## 2. Configuración del Entorno de Pruebas

Para que los tests unitarios locales puedan funcionar con componentes del framework de Android (como `Context` o la base de datos Room), se ha configurado un entorno de pruebas específico.

### Tecnologías Utilizadas

- **JUnit4:** El framework estándar para escribir tests en Java/Kotlin.
- **Robolectric:** Una librería que simula el entorno de Android dentro de la JVM, permitiendo instanciar clases del SDK de Android y acceder a recursos sin necesidad de un emulador.
- **Room (en memoria):** Para cada ejecución de test, se crea una base de datos temporal en la memoria RAM. Esto asegura que cada test parte de un estado limpio y no deja datos residuales que puedan afectar a otros tests.
- **Kotlin Coroutines Test:** Utilidades para probar código que usa corrutinas.

### Dependencias de Gradle (`app/build.gradle.kts`)

Las siguientes dependencias son cruciales para el entorno de pruebas. Se encuentran bajo la configuración `testImplementation`:

```kotlin
// Testing - Unit
testImplementation("junit:junit:4.13.2")
testImplementation("androidx.test.ext:junit:1.1.5") // Para el AndroidJUnit4 test runner
testImplementation("androidx.test:core:1.5.0")      // Para ApplicationProvider
testImplementation("org.robolectric:robolectric:4.12.1") // Para simular el entorno Android
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
// ... otras dependencias de test ...
```

### Configuración de Gradle

Para que Robolectric pueda acceder a los recursos de la aplicación, se ha añadido la siguiente configuración en `app/build.gradle.kts`:

```kotlin
android {
    // ...
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
```

## 3. Estructura de los Tests

Actualmente, la suite de tests cubre las partes más críticas de la capa de infraestructura.

### A. Test del Repositorio y DAO (`PhotoRepositoryTest.kt`)

Este test es el más completo, ya que valida la interacción de varios componentes a la vez.

- **Ubicación:** `app/src/test/java/com/proyecto/photogallery/PhotoRepositoryTest.kt`
- **Runner:** Se anota con `@RunWith(AndroidJUnit4::class)` para que JUnit use el runner que permite a Robolectric funcionar.
- **Objetivo:** Probar de forma aislada que Room (`PhotoDao`) y `PhotoRepositoryImpl` pueden insertar, leer y borrar datos correctamente en un entorno controlado.

#### Ciclo de Vida del Test

- **`@Before` (`setup`):** Antes de cada test, se crea una nueva base de datos Room **en memoria**. Esto garantiza un entorno limpio y aislado para cada prueba.
- **`@Test`:** Contiene la lógica del test en sí. Se usa `runBlocking` para ejecutar las funciones `suspend` de Room/DAO de manera síncrona dentro del test.
- **`@After` (`teardown`):** Después de cada test, la base de datos en memoria se cierra para liberar los recursos.

### B. Test del Mapper (`PhotoMapperTest.kt`)

Este es un test unitario puro que no necesita el entorno de Android (ni Robolectric).

- **Ubicación:** `app/src/test/java/com/proyecto/photogallery/infrastructure/mapper/PhotoMapperTest.kt`
- **Objetivo:** Asegurar que la "traducción" de datos entre el modelo de dominio (`Photo`) y la entidad de la base de datos (`PhotoEntity`) es correcta y no pierde información.
- **Tests clave:**
  - `toDomain convierte correctamente PhotoEntity a Photo`: Verifica que los campos se copian correctamente de la entidad al modelo de dominio.
  - `toEntity convierte correctamente Photo a PhotoEntity`: Verifica la conversión en la dirección opuesta.

## 4. Cómo Ejecutar los Tests

Puedes ejecutar los tests de dos maneras:

### A) Desde la Línea de Comandos

Abre una terminal en la raíz del proyecto y ejecuta el siguiente comando de Gradle para correr **todos** los tests unitarios:

```bash
./gradlew test
```

Si quieres ejecutar solo un test específico (por ejemplo, el del Mapper):

```bash
./gradlew test --tests "*PhotoMapperTest*"
```

### B) Desde Android Studio

1.  Navega hasta el archivo de test deseado (ej. `PhotoMapperTest.kt`).
2.  Haz clic derecho sobre el nombre de la clase.
3.  Selecciona **Run 'PhotoMapperTest'**.

Android Studio ejecutará los tests y mostrará los resultados en una ventana dedicada.

## 5. Próximos Pasos

- Añadir tests para los `ViewModel` usando `ViewModel` real y repositorios "mockeados" (falsos).
- Crear tests de instrumentación (`androidTest`) para verificar flujos de UI completos en un emulador o dispositivo real.

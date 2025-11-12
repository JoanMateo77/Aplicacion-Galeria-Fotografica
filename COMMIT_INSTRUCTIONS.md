# Comandos para Commit - Fase 6: Refinamiento

## Crear Nueva Rama y Commit

```bash
# 1. Crear y cambiar a nueva rama
git checkout -b fase-6-refinamiento-arquitectura

# 2. Agregar todos los cambios
git add .

# 3. Hacer commit
git commit -m "feat: Fase 6 - Refinamiento de arquitectura y mejoras

- Implementar PhotoSource enum para tipado fuerte (CAMERA, GALLERY)
- Mover validación de límite de fotos al dominio (AddPhotoUseCase)
- Implementar MaxPhotosException para manejo de errores de negocio
- Crear PhotoUiState para estado unificado de UI
- Implementar componentes UI reutilizables (CameraButton, GalleryButton, PhotoCard, PhotoGrid, EmptyState)
- Refactorizar PhotoViewModel para usar PhotoUiState
- Actualizar PhotoMapper para manejar PhotoSource
- Actualizar PhotoGalleryScreen para usar componentes y establecer PhotoSource
- Actualizar tests para incluir PhotoSource
- Eliminar archivos no utilizados (CameraScreen, DataSources vacíos, módulos Hilt vacíos)
- Actualizar documentación (README, TESTING, ESTRUCTURA_PROYECTO)"
```

## Alternativa: Commit Más Corto

Si prefieres un mensaje más corto:

```bash
git checkout -b fase-6-refinamiento-arquitectura
git add .
git commit -m "feat: Fase 6 - Mejoras de arquitectura

- PhotoSource enum y validación en dominio
- PhotoUiState y componentes UI reutilizables
- Limpieza de código y actualización de documentación"
```


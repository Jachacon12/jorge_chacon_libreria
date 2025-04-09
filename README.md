# Jenkins Shared Library

Este repositorio contiene una **librería compartida para Jenkins** que sigue la estructura recomendada para la automatización DevOps en el entorno de CI/CD de Jenkins.

## Estructura del proyecto

\_libreria/ ├── vars/ │ └── .placeholder ├── src/ │ └── org/ │ └── threepoints/ │ └── .placeholder └── resources/ └── org/ └── threepoints/ └── .placeholder

### 📁 Descripción de Carpetas

- `vars/`: contiene scripts accesibles directamente como pasos dentro de Jenkinsfile (`miStep.groovy`)
- `src/`: contiene clases auxiliares organizadas por paquete (namespace)
- `resources/`: contiene archivos de configuración o plantillas (`.properties`, `.json`, `.groovy`, etc.)

## Autor

Creado por **Jorge Chacon** para la práctica de Fullstack DevOps - Threepoints.

---

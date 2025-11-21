# 🏪 Sistema de Gestión de Ventas MDV

Sistema integral de gestión comercial desarrollado con JavaFX y Hibernate para la administración de clientes, productos, categorías y ventas.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-25.0.1-blue?style=flat&logo=java)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4.4-green?style=flat)
![Maven](https://img.shields.io/badge/Maven-3.x-red?style=flat&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat)

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración de Base de Datos](#-configuración-de-base-de-datos)
- [Ejecución](#-ejecución)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Módulos del Sistema](#-módulos-del-sistema)
- [Arquitectura](#-arquitectura)
- [Capturas de Pantalla](#-capturas-de-pantalla)
- [Documentación Adicional](#-documentación-adicional)
- [Contribución](#-contribución)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

## ✨ Características

### Gestión de Clientes
- ✅ Alta, baja y modificación de clientes
- ✅ Validación de DNI con formato español
- ✅ Gestión de direcciones (habitual y envío)
- ✅ Búsqueda por DNI y nombre
- ✅ Historial de compras por cliente

### Gestión de Productos
- 📦 Catálogo completo de productos
- 📦 Control de existencias en tiempo real
- 📦 Precios recomendados y personalizados
- 📦 Organización por categorías
- 📦 Búsqueda por código o nombre

### Gestión de Categorías
- 🏷️ Clasificación jerárquica de productos
- 🏷️ CRUD completo de categorías
- 🏷️ Visualización de productos por categoría
- 🏷️ Vista previa en tiempo real

### Sistema de Ventas
- 💰 Registro de ventas completo
- 💰 Gestión de detalles de venta
- 💰 Cálculo automático de totales
- 💰 Historial de ventas
- 💰 Reportes y estadísticas

### Características Técnicas
- 🎨 Interfaz gráfica moderna con JavaFX
- 🎨 Ventanas sin decoración nativa (estilo macOS)
- 🔒 Validación de datos con Jakarta Validation
- 💾 Persistencia con Hibernate/JPA
- 🗄️ Soporte para MySQL/MariaDB
- 📊 Arquitectura en capas (DAO, Service, Controller)

## 🛠 Tecnologías

### Backend
- **Java 21** - Lenguaje de programación
- **Hibernate 6.4.4** - ORM para persistencia
- **Jakarta Persistence 3.2.0** - Especificación JPA
- **Jakarta Validation 3.1.1** - Validación de beans
- **Lombok 1.18.42** - Reducción de código boilerplate

### Frontend
- **JavaFX 25.0.1** - Framework de interfaz gráfica
- **FXML** - Diseño declarativo de interfaces

### Base de Datos
- **MySQL 8.0.33** / **MariaDB 3.5.6**
- **Connector/J 8.0.33** - Driver JDBC

### Build & Deploy
- **Maven 3.x** - Gestión de dependencias
- **javafx-maven-plugin 0.0.8** - Plugin de ejecución

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java Development Kit (JDK) 21** o superior
  ```bash
  java -version
  # Debería mostrar: java version "21.x.x"
  ```

- **Apache Maven 3.6+**
  ```bash
  mvn -version
  ```

- **MySQL 8.0+** o **MariaDB 10.5+**
  ```bash
  mysql --version
  ```

- **IDE recomendado**: IntelliJ IDEA, Eclipse o NetBeans

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/AC301.git
cd AC301
```

### 2. Instalar Dependencias

```bash
mvn clean install
```

### 3. Configurar Variables de Entorno (Opcional)

Puedes configurar las credenciales de la base de datos mediante variables de entorno:

```bash
export DB_URL="jdbc:mysql://localhost:3306/gestion-ventas"
export DB_USER="root"
export DB_PASS="tu_password"
```

## 🗄️ Configuración de Base de Datos

### Crear la Base de Datos

```sql
CREATE DATABASE `gestion-ventas` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### Configurar Conexión

Edita el archivo `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:mysql://localhost:3306/gestion-ventas"/>
<property name="jakarta.persistence.jdbc.user" 
          value="root"/>
<property name="jakarta.persistence.jdbc.password" 
          value="tu_password"/>
```

### Esquema de Base de Datos

El esquema se genera automáticamente gracias a Hibernate con la configuración:

```xml
<property name="hibernate.hbm2ddl.auto" value="update"/>
```

#### Tablas Principales:
- `clientes` - Información de clientes
- `productos` - Catálogo de productos
- `categorias` - Clasificación de productos
- `ventas` - Registro de ventas
- `ventas_detalles` - Líneas de detalle de cada venta

## ▶️ Ejecución

### Mediante Maven

```bash
mvn clean javafx:run
```

### Mediante IDE

1. Importa el proyecto como proyecto Maven
2. Configura JDK 21 en tu IDE
3. Ejecuta la clase principal: `org.mdv.Main`

### Crear Ejecutable (JAR)

```bash
mvn clean package
java -jar target/AC301-1.0-SNAPSHOT.jar
```

## 📁 Estructura del Proyecto

```
AC301/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/mdv/
│   │   │       ├── dao/              # Capa de acceso a datos
│   │   │       │   ├── CategoriaDAO.java
│   │   │       │   ├── ClienteDAO.java
│   │   │       │   ├── ProductoDAO.java
│   │   │       │   ├── VentaDAO.java
│   │   │       │   └── VentaDetalleDAO.java
│   │   │       │
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       │   ├── CategoriaRequest.java
│   │   │       │   ├── CategoriaResponse.java
│   │   │       │   ├── ClienteRequest.java
│   │   │       │   ├── ClienteResponse.java
│   │   │       │   ├── ProductoRequest.java
│   │   │       │   ├── ProductoResponse.java
│   │   │       │   ├── VentaRequest.java
│   │   │       │   └── VentaResponse.java
│   │   │       │
│   │   │       ├── model/            # Entidades JPA
│   │   │       │   ├── Categoria.java
│   │   │       │   ├── Cliente.java
│   │   │       │   ├── Producto.java
│   │   │       │   ├── Venta.java
│   │   │       │   └── VentaDetalle.java
│   │   │       │
│   │   │       ├── service/          # Lógica de negocio
│   │   │       │   ├── CategoriaService.java
│   │   │       │   ├── ClienteService.java
│   │   │       │   ├── ProductoService.java
│   │   │       │   ├── VentaService.java
│   │   │       │   └── VentaDetalleService.java
│   │   │       │
│   │   │       ├── SQL/              # Consultas JPQL
│   │   │       │   ├── CategoriaSQL.java
│   │   │       │   ├── ClienteSQL.java
│   │   │       │   ├── ProductoSQL.java
│   │   │       │   └── VentaSQL.java
│   │   │       │
│   │   │       ├── util/             # Utilidades
│   │   │       │   └── TransactionUtil.java
│   │   │       │
│   │   │       ├── view/             # Capa de presentación
│   │   │       │   ├── App.java
│   │   │       │   └── controller/   # Controladores JavaFX
│   │   │       │       ├── CategoriaEditarController.java
│   │   │       │       ├── CategoriaInsertController.java
│   │   │       │       ├── CategoriaListadoController.java
│   │   │       │       ├── ClienteEditarController.java
│   │   │       │       ├── ClienteInsertController.java
│   │   │       │       ├── ClienteListadoController.java
│   │   │       │       ├── MainWindowController.java
│   │   │       │       ├── ProductoEditarController.java
│   │   │       │       ├── ProductoInsertController.java
│   │   │       │       ├── ProductoListadoController.java
│   │   │       │       ├── VentaListadoController.java
│   │   │       │       └── WindowControllerBase.java
│   │   │       │
│   │   │       └── Main.java         # Clase principal
│   │   │
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── persistence.xml   # Configuración JPA
│   │       │
│   │       └── org/mdv/view/         # Archivos FXML
│   │           ├── categoria/
│   │           │   ├── categoria-editar.fxml
│   │           │   ├── categoria-insert.fxml
│   │           │   └── categoria-listado.fxml
│   │           │
│   │           ├── cliente/
│   │           │   ├── cliente-editar.fxml
│   │           │   ├── cliente-insert.fxml
│   │           │   └── cliente-listado.fxml
│   │           │
│   │           ├── producto/
│   │           │   ├── producto-editar.fxml
│   │           │   ├── producto-insert.fxml
│   │           │   └── producto-listado.fxml
│   │           │
│   │           ├── venta/
│   │           │   └── venta-listado.fxml
│   │           │
│   │           └── main-window.fxml  # Ventana principal
│   │
│   └── test/
│       └── java/                     # Tests unitarios
│
├── pom.xml                           # Configuración Maven
├── README.md                         # Documentación
└── .gitignore                        # Archivos ignorados por Git
```

## 🎯 Módulos del Sistema

### 1. Gestión de Clientes

**Funcionalidades:**
- Registro de nuevos clientes con validación de DNI
- Edición de datos personales y direcciones
- Búsqueda avanzada (por DNI o nombre)
- Eliminación con confirmación
- Copiar dirección habitual a dirección de envío

**Validaciones:**
- DNI formato español: `[0-9]{8}[A-Za-z]`
- Campos obligatorios: DNI, nombre, apellidos, dirección de envío

### 2. Gestión de Productos

**Funcionalidades:**
- Alta de productos con código único
- Control de stock y precios
- Asignación de categorías
- Búsqueda por código o descripción
- Edición de información y existencias

**Validaciones:**
- Código único obligatorio
- Precio ≥ 0
- Existencias ≥ 0
- Categoría obligatoria

### 3. Gestión de Categorías

**Funcionalidades:**
- CRUD completo de categorías
- Visualización de productos asociados
- Vista previa en tiempo real
- Búsqueda por ID o nombre
- Protección contra eliminación con productos asociados

### 4. Sistema de Ventas

**Funcionalidades:**
- Registro de ventas con múltiples líneas
- Cálculo automático de totales
- Asociación con clientes
- Gestión de detalles de venta
- Visualización de historial

## 🏗️ Arquitectura

### Patrón de Diseño: Arquitectura en Capas

```
┌─────────────────────────────────────┐
│         Capa de Presentación        │
│    (JavaFX Controllers + FXML)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Capa de Servicios            │
│     (Lógica de Negocio + DTOs)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Capa de Acceso a Datos          │
│           (DAO + SQL)               │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Capa de Persistencia         │
│      (JPA/Hibernate + Entities)     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Base de Datos              │
│         (MySQL/MariaDB)             │
└─────────────────────────────────────┘
```

### Características Arquitectónicas

- **Separación de Responsabilidades**: Cada capa tiene una función específica
- **DTOs (Data Transfer Objects)**: Desacoplamiento entre capas
- **Transacciones Gestionadas**: Mediante `TransactionUtil`
- **Inyección de Dependencias Manual**: Servicios instanciados en controladores
- **Validación en Múltiples Capas**: Request DTOs + Bean Validation

### Gestión de Transacciones

```java
// Operaciones con transacción
TransactionUtil.doInTransaction(emf, em -> {
    // Código que modifica la BD
});

// Operaciones de solo lectura
TransactionUtil.doInSession(emf, em -> {
    // Código que consulta la BD
});
```

## 📸 Capturas de Pantalla

### Ventana Principal
Dashboard central con acceso a todos los módulos del sistema.

### Gestión de Clientes
Listado completo con filtros y operaciones CRUD.

### Gestión de Productos
Catálogo con búsqueda avanzada y control de stock.

### Sistema de Ventas
Registro y consulta de ventas con detalles.

## 📚 Documentación Adicional

### Modelos de Datos

#### Cliente
```java
@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String dirHabitual;
    private String dirEnvio;
    
    @OneToMany(mappedBy = "cliente")
    private List<Venta> ventas;
}
```

#### Producto
```java
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    private String codigo;
    private String descripcion;
    private BigDecimal precioRecomendado;
    private int existencias;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
```

#### Venta
```java
@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    private LocalDate fechaVenta;
    private BigDecimal importeTotal;
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<VentaDetalle> ventaDetalles;
}
```

### Consultas JPQL Comunes

```java
// Buscar producto con categoría (evita N+1)
SELECT p FROM Producto p 
JOIN FETCH p.categoria 
WHERE LOWER(p.codigo) = LOWER(:codigo)

// Buscar ventas con detalles
SELECT DISTINCT v FROM Venta v 
JOIN FETCH v.cliente 
LEFT JOIN FETCH v.ventaDetalles d 
LEFT JOIN FETCH d.producto p 
LEFT JOIN FETCH p.categoria

// Buscar clientes por nombre (búsqueda parcial)
SELECT c FROM Cliente c 
WHERE LOWER(c.nombre) LIKE :nombre
```

## 🤝 Contribución

¡Las contribuciones son bienvenidas! Si deseas colaborar:

1. **Fork** el proyecto
2. Crea una **rama** para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. Abre un **Pull Request**

### Guía de Estilo

- Seguir convenciones de código Java estándar
- Usar Lombok para reducir boilerplate
- Documentar clases y métodos públicos
- Mantener la arquitectura en capas
- Validar datos en múltiples niveles

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

```
MIT License

Copyright (c) 2024 MDV Systems

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 👥 Autores

- **Equipo MDV** - *Desarrollo inicial* - [GitHub](https://github.com/tu-usuario)

## 🙏 Agradecimientos

- Comunidad JavaFX por la documentación
- Hibernate team por el excelente ORM
- Todos los contribuidores del proyecto

## 📞 Contacto

- **Email**: contacto@mdvsystems.com
- **Website**: [www.mdvsystems.com](https://www.mdvsystems.com)
- **Issues**: [GitHub Issues](https://github.com/tu-usuario/AC301/issues)

---

**Nota**: Este README se actualiza regularmente. Última actualización: Noviembre 2025

⭐ Si este proyecto te ha sido útil, considera darle una estrella en GitHub

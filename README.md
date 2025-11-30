# pe.validator.doi

## 🌐 Idiomas

[🇪🇸 Español](README.md) · [🇬🇧 English](README.en.md)

---

## ⚡ TL;DR

`pe.validator.doi` es una librería Java mínima que valida **tipos y números de Documento Oficial de Identidad** usados
en el Perú.  
Soporta: **DNI, RUC, CE y Pasaporte**.  
Su trabajo es simple: **te dice si un valor “parece válido” según su tipo**. Nada más.

---

## 📌 ¿Qué es este módulo?

`pe.validator.doi` es una librería Java que valida tipos y números de Documento Oficial de Identidad usados en el Perú (
**DOI**).  
Su propósito es asegurar que un valor tiene un formato válido antes de ser usado en cualquier flujo de negocio.

Está diseñado para ser:

- **Pequeño**
- **Predecible**
- **Sin dependencias externas**
- **Apto para cualquier proyecto Java (21, 23, 25+)**
- **Orientado al ecosistema de software peruano**

No intenta validar la existencia del documento, ni consultar entidades del Estado.  
Solo valida formato. Punto.

---

## ⚠️ Advertencia importante

La validación de **RUC** que realiza esta librería es **100% local**.  
No consulta servicios de SUNAT ni verifica la existencia real del contribuyente.

Esto significa que un RUC puede ser considerado “válido” si su **estructura** es correcta,  
aunque dicho RUC **no exista** en el padrón de SUNAT.

Esta librería valida formato, no existencia.

---

## 🏛️ ¿Qué es la SUNAT?

La **SUNAT** es la *Superintendencia Nacional de Aduanas y de Administración Tributaria* del Perú.  
Es la entidad del Estado responsable de:

- administrar los impuestos,
- gestionar el Registro Único de Contribuyentes (RUC),
- y supervisar el cumplimiento de obligaciones tributarias y aduaneras.

En otras palabras: es la autoridad tributaria (fisco) del Perú.  
Si se necesita verificar si un RUC **existe** y está **activo**, esa verificación debe hacerse a través de los servicios
oficiales de SUNAT, no mediante este módulo.

Este proyecto **no** consulta a SUNAT ni interactúa con sus sistemas.

---

## 🇵🇪 ¿Por qué existe este proyecto?

En el Perú usamos varios tipos de Documento Oficial de Identidad: DNI, RUC, Carné de Extranjería, Pasaporte, entre
otros.  
Cada entidad pública y cada sistema privado implementa validaciones distintas, a veces incompletas o inconsistentes.

Esta librería busca ofrecer:

- un estándar simple,
- un comportamiento uniforme,
- y una validación sintáctica confiable,

para poder limpiar datos **antes** de cualquier lógica adicional.

---

## 🚀 Instalación

Disponible en Maven Central:

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.infoyupay:pe.validator.doi:<versión>")
}
```

### Maven

```xml

<dependency>
    <groupId>com.infoyupay</groupId>
    <artifactId>pe.validator.doi</artifactId>
    <version><!-- versión --></version>
</dependency>
```

---

## 🧪 Uso básico

```java
boolean ok = DoiValidator.isValid(DoiType.DNI, "12345678");

if(ok){
        // listo para usar en tu flujo
        }
```

---

## 📘 Tipos soportados

| Tipo         | Descripción                          | Código PLAME | Código PLE | Código AFPNET | Código FV3800 | Regex           |
|--------------|--------------------------------------|--------------|------------|---------------|---------------|-----------------|
| `OTHERS`     | Otros                                | --           | 0          | --            | -             | \p{Alnum}{1,15} |
| `DNI`        | Documento Nacional de Identidad      | 01           | 1          | 0             | 01            | \d{8}           |
| `PNP`        | Carné Militar y Policial             | 02           | 2          | 2             | -             | \p{Alnum}{1,15} |
| `CE`         | Carné de Extranjería                 | 04           | 4          | 1             | 04            | \p{Alnum}{1,12} |
| `RUC`        | Registro Único de Contribuyente      | 06           | 6          | -             | 06            | \d{11}          |
| `PASSPORT`   | Pasaporte peruano o extranjero       | 07           | 7          | 4             | 07            | \p{Alnum}{1,12} |               
| `REFUGEE`    | Carné de refugiado                   | 09           | 0          | 9             | -             | \p{Alnum}{1,15} |
| `DIPLOMATIC` | Cédula de identidad diplomática      | 22           | 0          | 7             | -             | \p{Alnum}{1,15} |
| `PTP`        | Permiso Temporal de Permanencia      | 23           | 0          | 6             | -             | \p{Alnum}{1,15} |
| `ID`         | Identificación Extranjera            | 24           | 0          | 8             | 02            | \p{Alnum}{1,15} |
| `ID_PTP`     | Carné del PTP                        | 26           | 0          | 10            | -             | \p{Alnum}{1,15} |
| `TIN`        | Identificación Tributaria Extranjera | --           | 0          | -             | 01            | \p{Alnum}{1,15} |

### Caso especial: RUC

El RUC es un caso de uso especial, ya que SUNAT ha establecido un modelo de verificación basado en 2 parámetros: los dos
caracteres iniciales, y un dígito de verificación basado en el módulo 11. Así, por ejemplo, el RUC de Infoyupay es
**20607854247**, lo podemos descomponer en tres partes:

| Prefijo | Número de Identidad | Dígito de Verificación |
|---------|---------------------|------------------------|
| 20      | 60785424            | 7                      |

Las reglase del prefijo:

| Prefijo | Significado                                                                                      |
|---------|--------------------------------------------------------------------------------------------------|
| 10      | Persona Natural Peruana                                                                          |
| 15      | Sucesiones indivisas, sociedades conyugales, personal policial, militar, y personas extranjeras. |
| 16      | Se menciona como válido en la especificación SUNAT; pero está reservado (no está documentado).   |
| 17      | Personas naturales inscritas entre los años 1993 y 2000.                                         |
| 20      | Personas Jurídicas (incluyendo sociedades irregulares, joint ventures, contratos de consorcio)   |

Las reglas del dígito de verificación:

Es módulo 11, basado en el algoritmo de verificación de RUC en Perú. Este algoritmo consiste en utilizar el valor
numérico de los 10 primeros dígitos, multiplicarlo por un peso según su posición, y sumar los resultados.
El peso posicional es el siguiente:

| Char index:    | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
|----------------|---|---|---|---|---|---|---|---|---|---|
| Peso (factor): | 5 | 4 | 3 | 2 | 7 | 6 | 5 | 4 | 3 | 2 |

Luego, se suma el resultado de cada multiplicación y se calcula el resto de la división por 11 (módulo, u operador
`%` en Java). Al residuo se le calcula el complemento a 11, y el resultado es el dígito de verificación. Si nuestro
resultado es 10 u 11, el dígito verificador es 0 o 1 respectivamente.

Ejemplo práctico:

| RUC:           | 2  | 0 | 6  | 0 | 7  | 8  | 5  | 4  | 2 | 4 | 7 |
|----------------|----|---|----|---|----|----|----|----|---|---|---|
| Peso (factor): | 5  | 4 | 3  | 2 | 7  | 6  | 5  | 4  | 3 | 2 | - |
| Parcial :      | 10 | 0 | 18 | 0 | 49 | 48 | 25 | 16 | 6 | 8 | - |

| Operación           | Resultado    |
|---------------------|--------------|
| Suma parcial:       | 180          |
| Resto:              | 180 % 11 = 4 |
| Complemento a 11:   | 11 - 4 = 7   |
| Dígito verificador: | 7            |

Como nuestro RUC comienza con un prefijo válido, contiene 11 dígitos, termina en 7, y el dígito verificador calculado es
7,
entonces es un número de RUC válido.
**Esto no quiere decir que el RUC exista, pero sí que sea válido según el algoritmo de verificación.**

#### Optimizaciones

En InfoYupay amamos la optimizaciones elegantes, así que, en lugar de tener un array para guardar los pesos por cada
posición, utilizamos una optimización en el bucle de recorrido de la cadena de caracteres que nos permite calcular el
peso de cada dígito de forma eficiente, mediante cómputo aritmético (el favorito de cualquier CPU digno hijo de Turing).
Así lo hacemos:

Nuestro bucle for contiene dos iteraciones:

```java
for (int i = 0, x = 6; i < 10; i++)
```

¿Qué hacemos con x? Al momento de hacer la multiplicación, lo disminuimos en 1 unidad antes de usar:

```java
sum += (c - '0') * --x;
```

esto, lo que hace, es disminuir el valor de x en 1 unidad antes de usarlo en la
multiplicación,
lo que nos permite calcular el peso de cada dígito de forma eficiente. Así, en la posición 0, multiplicamos por 5, en la
posición 1 por 4, en la posición 2 por 3, y así sucesivamente.

¿Y qué pasa cuando llegamos a la posición 4, donde el peso se "reinicia" a 7? Pues, al inicio del cuerpo del bucle,
hacemos
una pequeña validación `if( i == 4) x = 8;` y con eso nos aseguramos de que si estamos revisando la posición 4,
multiplicaremos
por 7, y en las sucesivas iteraciones seguirá disminuyendo hasta llegar al factor 2 en la posición 9. De esta forma nos
ahorramos la creación de un array para el peso, lo que nos permite optimizar el uso de memoria y dejar este método
con un footprint negligible incluso en entornos de alta concurrencia.

Esta optimización no busca micro-mejoras prematuras, sino evitar almacenamiento innecesario cuando el patrón
es determinístico y puede representarse aritméticamente.

---

## Value Sanitization

Esta biblioteca también contiene utilidades para "sanitizar" los valores antes de pasarlos por la validación. No es
nuestra intención decirte a ti cómo hacer tu trabajo, o limitarte la forma en que quieras usar nuestra biblioteca. No obstante,
sí nos gustaría compartir contigo nuestra visión sobre dónde debemos aplicar la limpieza de datos, y dónde debemos dejar
al dato simplemente ser.

En InfoYupay, creemos en la libertad del usuario y en la ergonomía del usuario, cada decisión que tomamos está orientada
a mejorar la vida del usuario, y no a cuadricularla para que se adapte a nuestros estándares. Por ello, en el nivel del
modelo físico (aka: Base de Datos), nosotros no aplicamos limpieza de datos, el usuario es completamente libre de escribir
el dato tal como lo encuentra en un documento original, así tal cual, el usuario lo ingresa en la GUI, y así queda registrado
en las bases de datos. Por ejemplo, si ingresa un número de pasaporte que incluía guiones, se le permite ingresar los guiones,
siempre y cuando... la versión sanitizada de ese número cumpla con los requisitos de validación.

```java
if(validate(doiType, sanitized(doiNumber)){
    allowPersistence();
}else{
    showUserMessage("El número ingresado no es compatible con los requisitos de validación.")
}
```

Y entonces, si el usuario ingresa un número de identificación con puntos, guiones, y otros caracteres que SUNAT no 
permite ingresar en sus sistemas (PLE, FV3800, etc), ¿cómo vamos a enviarlo si no tenemos el dato limpio?

Pues, justamente de esta forma:

```java
exportStream.write(sanitized(doiNumber));
```

De esa forma garantizamos:
- **Trazabilidad**: el valor queda registrado tal como está en el documento original, permitiendo trazar mejor cualquier error.
- **Ergonomía**: el usuario es menos propenso a cometer errores si solamente tiene que copiar el dato tal como está, en
lugar de tener que hacer él la limpieza manual.
- **Seguridad**: garantizamos que el dato enviado sea compatible con los sistemas de SUNAT, evitando errores de validación.
Nuestra experiencia nos ha demostrado que dejarle esa responsabilidad al usuario, inevitablemente termina en errores no
deseados, muchos de los cuáles revientan en las auditorías, con terribles costos para los stakeholders.
- **Separación de Responsabilidades**: cualquier sistema o aplicación robusto, con una arquitectura madura, tiene que 
separar adecuadamente las responabilidades de los distintos elementos. Y la responsabilidad de sanitizar no debe recaer
en un actor humano, sino en un comportamiento del sistema.

Si tú deseas utilizar nuestra biblioteca de otra forma, estás en todo tu derecho de hacerlo, no tomes esto como un 
aleccionamiento, solamente como una anécdota de parte de alguien que ya ha pasado por esto antes.

---

## 📄 Licencia

Este proyecto está licenciado bajo **GPLv3 or (at your option) any later version**.  
Consulta el archivo [LICENSE.md](LICENSE.md) para más detalles.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas.  
Este módulo es parte del ecosistema **InfoYupay**, pero está pensado para servir a toda la comunidad de desarrolladores
peruanos.

---

Hecho con ☕ y respeto por el software peruano.

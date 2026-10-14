# Lab 3 - Herencia: Sistema de mensajería EnvíaGT

Laboratorio 3 de Programación Orientada a Objetos (CC2008), Universidad del Valle de Guatemala, Ciclo 2 2026.

Programa de consola en Java para la empresa de mensajería EnvíaGT. Permite cotizar y registrar envíos de distintas categorías, administrar la flotilla de vehículos, armar despachos, registrar los resultados de entrega y consultar reportes de envíos e ingresos. Los datos se guardan solo en memoria mientras el programa está abierto.

## Integrantes

- Henry Emanuel Vicente Alvarado (26275)
- Joel Alejandro Chávez Pérez (26888)

## Estructura del repositorio

```
cc2008-Lab3/
├── src/     Código fuente (.java)
├── bin/     Archivos compilados (.class)
├── docs/    Análisis del laboratorio (POO-AnalisisLab3.xlsx)
└── README.md
```

## Cómo compilar y ejecutar

Se necesita Java 17 o superior. Desde la carpeta del proyecto:

```
javac -d bin src/*.java
java -cp bin Main
```

En la consola de Windows (cmd) las tildes pueden verse raras. En IntelliJ, VS Code o PowerShell con UTF-8 se ven bien.

## Diseño

El programa usa el patrón MVC.

| Capa | Clases |
|---|---|
| Modelo | `Envio` (abstracta), `Sobre`, `Paquete`, `PaqueteFragil`, `CargaRefrigerada`, `Vehiculo`, `Despacho`, `EnviaGT`, `TipoDestino`, `EstadoEnvio`, `EstadoDespacho` |
| Vista | `VistaEnviaGT` |
| Controlador | `ControladorEnviaGT` |
| Driver | `Main` |

### Jerarquía de herencia

```
Envio (abstracta)
├── Sobre
├── Paquete
│   └── PaqueteFragil
└── CargaRefrigerada
```

- `Envio` guarda los datos comunes, controla los estados, los intentos y los ingresos, y declara los métodos abstractos `calcularCosto()`, `calcularDiasEntrega()`, `getIntentosPermitidos()` y `getCategoria()`.
- Cada subclase implementa sus propias reglas de costo, tiempo de entrega e intentos.
- `PaqueteFragil` hereda de `Paquete` porque es un paquete con embalaje y seguro. Reutiliza el cálculo del paquete con `super.calcularCosto()` y `super.calcularDiasEntrega()`.
- `CargaRefrigerada` sobrescribe `esCompatibleCon(Vehiculo)` para exigir un vehículo refrigerado. `Despacho` solo le pregunta al envío y no revisa su tipo.
- `EnviaGT` es el gestor: guarda las listas de envíos, vehículos y despachos, y entrega los datos de los reportes.

## Menú

| Opción | Acción |
|---|---|
| 1 | Cotizar y registrar envío |
| 2 | Registrar vehículo |
| 3 | Crear despacho |
| 4 | Asignar envío a despacho |
| 5 | Retirar envío de despacho |
| 6 | Hacer salir despacho |
| 7 | Registrar resultado de entrega |
| 8 | Consultar envíos |
| 9 | Consultar vehículos |
| 10 | Reportes (envíos por categoría y estado, ingresos y despachos abiertos) |
| 0 | Salir |

## Reglas de cobro

| Categoría | Costo | Entrega | Intentos |
|---|---|---|---|
| Sobre | Q20 urbano, Q35 departamental. Peso máximo 0.5 kg | 1 día urbano, 2 departamental | 3 |
| Paquete | Tarifa base (Q25 urbano, Q40 departamental) más Q8 por kilogramo facturable o fracción | 2 días urbano, 4 departamental | 3 |
| Paquete frágil | Costo de paquete más Q15 de embalaje más 3% del valor declarado | Un día más que el paquete | 2 |
| Carga refrigerada | Q25 más Q12 por kilogramo de peso real más Q60 de cadena de frío. Solo destino urbano y temperatura entre -25 y 8 grados Celsius | 1 día | 1 |

- Peso volumétrico = largo × ancho × alto / 5000. El peso facturable es el mayor entre el peso real y el volumétrico.
- Cada intento adicional al primero cuesta Q15. Se cobra cuando el despacho sale.
- Ingreso total = ingreso por registro + ingreso por reintentos. Las cotizaciones no aceptadas, los retiros de envíos y las operaciones rechazadas no cambian los ingresos.

Ejemplo del enunciado: un paquete de 3.2 kg y 40 × 30 × 20 cm con destino urbano tiene un peso volumétrico de 4.8 kg y se facturan 5 kg. Cuesta Q25 + Q40 = Q65. Como paquete frágil con valor declarado de Q1,200 cuesta Q65 + Q15 + Q36 = Q116.

## Estados

- **Envío:** En bodega, Asignado, En ruta, Entregado, Devuelto al remitente. Si una entrega falla y quedan intentos, el envío vuelve a bodega. Si ya no quedan intentos, queda devuelto al remitente y no puede asignarse de nuevo.
- **Despacho:** En preparación, En ruta, Cerrado. Se cierra cuando todos sus envíos tienen resultado, y el vehículo queda libre.

## Validaciones

- Cada dato se revisa apenas se escribe. Si no es válido, se muestra el error y se vuelve a pedir sin perder lo que ya se escribió.
- Una entrada con letras donde va un número no termina el programa.
- No se aceptan códigos ni placas vacíos o repetidos, pesos o medidas en 0 o negativos, sobres de más de 0.5 kg, cargas refrigeradas con destino departamental o fuera del rango de temperatura, ni envíos que superen la capacidad del vehículo o que no sean compatibles con él.
- Las operaciones sobre despachos muestran primero las opciones válidas: despachos en preparación o en ruta, envíos en bodega y envíos pendientes de resultado.

## Datos iniciales

Al iniciar, el sistema carga 8 envíos en bodega, sin ingresos, y 3 vehículos.

| Código | Categoría | Datos |
|---|---|---|
| S001, S002 | Sobre | 0.3 kg urbano, 0.5 kg departamental |
| P001, P002 | Paquete | P001: 3.2 kg y 40 × 30 × 20 cm (su peso volumétrico supera al real). P002: 8 kg y 30 × 30 × 30 cm |
| F001, F002 | Paquete frágil | Valor declarado de Q1,200 y Q3,500 |
| R001, R002 | Carga refrigerada | -18 y 4 grados Celsius, urbanos |

| Placa | Capacidad | Refrigerado |
|---|---|---|
| P123ABC | 800 kg | No |
| C456DEF | 600 kg | Sí |
| M789GHI | 10 kg | No |

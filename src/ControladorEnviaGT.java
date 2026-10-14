import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ControladorEnviaGT {
    private EnviaGT gestor;
    private VistaEnviaGT vista;

    public ControladorEnviaGT(EnviaGT gestor, VistaEnviaGT vista) {
        this.gestor = gestor;
        this.vista = vista;
    }

    public void iniciar() {
        cargarDatosIniciales();
        vista.mostrarMensaje("Bienvenido al sistema de EnvíaGT.");
        try {
            while (true) {
                vista.mostrarMenu();
                int opcion = vista.leerEntero("Opción: ");
                if (opcion == 0) {
                    vista.mostrarMensaje("Saliendo del sistema.");
                    return;
                }
                ejecutarOpcion(opcion);
            }
        } catch (NoSuchElementException e) {
            vista.mostrarMensaje("");
            vista.mostrarMensaje("No hay más datos de entrada. Saliendo del sistema.");
        }
    }

    private void cargarDatosIniciales() {
        gestor.cargarEnvioInicial(new Sobre("S001", "Ana López", "Carlos Pérez", 0.3, TipoDestino.URBANO));
        gestor.cargarEnvioInicial(new Sobre("S002", "Luis Gómez", "María Ruiz", 0.5, TipoDestino.DEPARTAMENTAL));
        gestor.cargarEnvioInicial(new Paquete("P001", "Jorge Díaz", "Sofía Castro", 3.2, TipoDestino.URBANO,
                40, 30, 20));
        gestor.cargarEnvioInicial(new Paquete("P002", "Elena Morales", "Pedro Sánchez", 8.0,
                TipoDestino.DEPARTAMENTAL, 30, 30, 30));
        gestor.cargarEnvioInicial(new PaqueteFragil("F001", "Andrea Flores", "Diego Ramírez", 2.0,
                TipoDestino.URBANO, 30, 20, 15, 1200));
        gestor.cargarEnvioInicial(new PaqueteFragil("F002", "Roberto Juárez", "Lucía Herrera", 5.5,
                TipoDestino.DEPARTAMENTAL, 50, 40, 30, 3500));
        gestor.cargarEnvioInicial(new CargaRefrigerada("R001", "Farmacia Central", "Hospital Roosevelt", 4.0,
                TipoDestino.URBANO, -18));
        gestor.cargarEnvioInicial(new CargaRefrigerada("R002", "Lácteos del Valle", "Supermercado La Torre", 2.5,
                TipoDestino.URBANO, 4));
        gestor.registrarVehiculo(new Vehiculo("P123ABC", 800, false));
        gestor.registrarVehiculo(new Vehiculo("C456DEF", 600, true));
        gestor.registrarVehiculo(new Vehiculo("M789GHI", 10, false));
    }

    private void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    cotizarYRegistrarEnvio();
                    break;
                case 2:
                    registrarVehiculo();
                    break;
                case 3:
                    crearDespacho();
                    break;
                case 4:
                    asignarEnvio();
                    break;
                case 5:
                    retirarEnvio();
                    break;
                case 6:
                    sacarDespacho();
                    break;
                case 7:
                    registrarResultado();
                    break;
                case 8:
                    consultarEnvios();
                    break;
                case 9:
                    consultarVehiculos();
                    break;
                case 10:
                    mostrarReportes();
                    break;
                default:
                    vista.mostrarError("Opción inválida.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void cotizarYRegistrarEnvio() {
        vista.mostrarMensaje("Categorías: 1. Sobre  2. Paquete  3. Paquete frágil  4. Carga refrigerada");
        int categoria = vista.leerEntero("Elija la categoría: ");
        while (categoria < 1 || categoria > 4) {
            vista.mostrarError("La categoría debe ser un número del 1 al 4.");
            categoria = vista.leerEntero("Elija la categoría: ");
        }
        Envio envio = crearEnvioSegunCategoria(categoria);
        vista.mostrarCotizacion(envio.getCategoria(), envio.calcularCosto(), envio.calcularDiasEntrega());
        if (vista.leerSiNo("¿El cliente acepta la cotización? (s/n): ")) {
            gestor.registrarEnvio(envio);
            vista.mostrarMensaje("Envío " + envio.getCodigo() + " registrado y en bodega. Monto cobrado: Q"
                    + vista.formatearMonto(envio.getIngresoRegistro()));
        } else {
            vista.mostrarMensaje("Cotización no aceptada. No se registró nada.");
        }
    }

    private Envio crearEnvioSegunCategoria(int categoria) {
        if (categoria < 1 || categoria > 4) {
            throw new IllegalArgumentException("Categoría inválida.");
        }
        String codigo = vista.leerTexto("Código: ");
        while (codigo.isEmpty() || gestor.buscarEnvio(codigo) != null) {
            if (codigo.isEmpty()) {
                vista.mostrarError("El código del envío no puede estar vacío.");
            } else {
                vista.mostrarError("Ya existe un envío con el código " + codigo + ".");
            }
            codigo = vista.leerTexto("Código: ");
        }
        String remitente = vista.leerTexto("Nombre del remitente: ");
        while (remitente.isEmpty()) {
            vista.mostrarError("El nombre del remitente no puede estar vacío.");
            remitente = vista.leerTexto("Nombre del remitente: ");
        }
        String destinatario = vista.leerTexto("Nombre del destinatario: ");
        while (destinatario.isEmpty()) {
            vista.mostrarError("El nombre del destinatario no puede estar vacío.");
            destinatario = vista.leerTexto("Nombre del destinatario: ");
        }
        double pesoReal = vista.leerDecimal("Peso real (kg): ");
        while (pesoReal <= 0 || (categoria == 1 && pesoReal > Sobre.PESO_MAXIMO)) {
            if (pesoReal <= 0) {
                vista.mostrarError("El peso real debe ser mayor que 0.");
            } else {
                vista.mostrarError("Un sobre no puede pesar más de " + Sobre.PESO_MAXIMO + " kg.");
            }
            pesoReal = vista.leerDecimal("Peso real (kg): ");
        }
        int opcionDestino = vista.leerEntero("Destino (1. Urbano, 2. Departamental): ");
        while ((opcionDestino != 1 && opcionDestino != 2) || (categoria == 4 && opcionDestino == 2)) {
            if (opcionDestino != 1 && opcionDestino != 2) {
                vista.mostrarError("El destino debe ser 1 (urbano) o 2 (departamental).");
            } else {
                vista.mostrarError("La carga refrigerada solo acepta destino urbano.");
            }
            opcionDestino = vista.leerEntero("Destino (1. Urbano, 2. Departamental): ");
        }
        TipoDestino destino;
        if (opcionDestino == 1) {
            destino = TipoDestino.URBANO;
        } else {
            destino = TipoDestino.DEPARTAMENTAL;
        }
        if (categoria == 1) {
            return new Sobre(codigo, remitente, destinatario, pesoReal, destino);
        }
        if (categoria == 4) {
            double temperatura = vista.leerDecimal("Temperatura requerida (grados Celsius): ");
            while (temperatura < CargaRefrigerada.TEMPERATURA_MINIMA
                    || temperatura > CargaRefrigerada.TEMPERATURA_MAXIMA) {
                vista.mostrarError("La temperatura debe estar entre " + CargaRefrigerada.TEMPERATURA_MINIMA + " y "
                        + CargaRefrigerada.TEMPERATURA_MAXIMA + " grados Celsius.");
                temperatura = vista.leerDecimal("Temperatura requerida (grados Celsius): ");
            }
            return new CargaRefrigerada(codigo, remitente, destinatario, pesoReal, destino, temperatura);
        }
        double largo = vista.leerDecimal("Largo (cm): ");
        while (largo <= 0) {
            vista.mostrarError("El largo debe ser mayor que 0.");
            largo = vista.leerDecimal("Largo (cm): ");
        }
        double ancho = vista.leerDecimal("Ancho (cm): ");
        while (ancho <= 0) {
            vista.mostrarError("El ancho debe ser mayor que 0.");
            ancho = vista.leerDecimal("Ancho (cm): ");
        }
        double alto = vista.leerDecimal("Alto (cm): ");
        while (alto <= 0) {
            vista.mostrarError("El alto debe ser mayor que 0.");
            alto = vista.leerDecimal("Alto (cm): ");
        }
        if (categoria == 2) {
            return new Paquete(codigo, remitente, destinatario, pesoReal, destino, largo, ancho, alto);
        }
        double valorDeclarado = vista.leerDecimal("Valor declarado (Q): ");
        while (valorDeclarado <= 0) {
            vista.mostrarError("El valor declarado debe ser mayor que 0.");
            valorDeclarado = vista.leerDecimal("Valor declarado (Q): ");
        }
        return new PaqueteFragil(codigo, remitente, destinatario, pesoReal, destino, largo, ancho, alto,
                valorDeclarado);
    }

    private void registrarVehiculo() {
        String placa = vista.leerTexto("Placa: ");
        while (placa.isEmpty() || gestor.buscarVehiculo(placa) != null) {
            if (placa.isEmpty()) {
                vista.mostrarError("La placa no puede estar vacía.");
            } else {
                vista.mostrarError("Ya existe un vehículo con la placa " + placa + ".");
            }
            placa = vista.leerTexto("Placa: ");
        }
        double capacidad = vista.leerDecimal("Capacidad máxima (kg): ");
        while (capacidad <= 0) {
            vista.mostrarError("La capacidad máxima debe ser mayor que 0.");
            capacidad = vista.leerDecimal("Capacidad máxima (kg): ");
        }
        boolean refrigerado = vista.leerSiNo("¿Tiene refrigeración? (s/n): ");
        gestor.registrarVehiculo(new Vehiculo(placa, capacidad, refrigerado));
        vista.mostrarMensaje("Vehículo registrado.");
    }

    private void crearDespacho() {
        ArrayList<Vehiculo> libres = new ArrayList<>();
        for (Vehiculo vehiculo : gestor.listarVehiculos()) {
            if (vehiculo.isLibre()) {
                libres.add(vehiculo);
            }
        }
        if (libres.isEmpty()) {
            vista.mostrarError("No hay vehículos libres para crear un despacho.");
            return;
        }
        vista.mostrarMensaje("Vehículos libres:");
        vista.mostrarVehiculos(libres);
        String placa = vista.leerTexto("Placa del vehículo: ");
        Vehiculo vehiculo = gestor.buscarVehiculo(placa);
        while (vehiculo == null || !vehiculo.isLibre()) {
            if (placa.isEmpty()) {
                vista.mostrarError("La placa no puede estar vacía.");
            } else if (vehiculo == null) {
                vista.mostrarError("No existe un vehículo con la placa " + placa + ".");
            } else {
                vista.mostrarError("El vehículo " + vehiculo.getPlaca() + " ya tiene un despacho sin cerrar.");
            }
            placa = vista.leerTexto("Placa del vehículo: ");
            vehiculo = gestor.buscarVehiculo(placa);
        }
        Despacho despacho = gestor.crearDespacho(placa);
        vista.mostrarMensaje("Despacho " + despacho.getNumero() + " creado con el vehículo "
                + despacho.getVehiculo().getPlaca() + ".");
    }

    private void asignarEnvio() {
        ArrayList<Despacho> enPreparacion = new ArrayList<>();
        for (Despacho despacho : gestor.listarDespachosAbiertos()) {
            if (despacho.getEstado() == EstadoDespacho.EN_PREPARACION) {
                enPreparacion.add(despacho);
            }
        }
        if (enPreparacion.isEmpty()) {
            vista.mostrarError("No hay despachos en preparación. Primero cree un despacho.");
            return;
        }
        ArrayList<Envio> enBodega = new ArrayList<>();
        for (Envio envio : gestor.listarEnvios()) {
            if (envio.getEstado() == EstadoEnvio.EN_BODEGA) {
                enBodega.add(envio);
            }
        }
        if (enBodega.isEmpty()) {
            vista.mostrarError("No hay envíos en bodega para asignar.");
            return;
        }
        vista.mostrarMensaje("Despachos en preparación:");
        vista.mostrarDespachosAbiertos(enPreparacion);
        int numero = vista.leerEntero("Número de despacho: ");
        Despacho despacho = gestor.buscarDespacho(numero);
        while (despacho == null || despacho.getEstado() != EstadoDespacho.EN_PREPARACION) {
            if (despacho == null) {
                vista.mostrarError("No existe el despacho " + numero + ".");
            } else {
                vista.mostrarError("El despacho " + numero + " no está en preparación.");
            }
            numero = vista.leerEntero("Número de despacho: ");
            despacho = gestor.buscarDespacho(numero);
        }
        vista.mostrarMensaje("Envíos en bodega:");
        vista.mostrarEnvios(enBodega);
        String codigo = vista.leerTexto("Código del envío: ");
        Envio envio = gestor.buscarEnvio(codigo);
        while (envio == null || envio.getEstado() != EstadoEnvio.EN_BODEGA) {
            if (codigo.isEmpty()) {
                vista.mostrarError("El código del envío no puede estar vacío.");
            } else if (envio == null) {
                vista.mostrarError("No existe un envío con el código " + codigo + ".");
            } else {
                vista.mostrarError("El envío " + envio.getCodigo() + " no está en bodega. Estado actual: "
                        + envio.getEstado() + ".");
            }
            codigo = vista.leerTexto("Código del envío: ");
            envio = gestor.buscarEnvio(codigo);
        }
        gestor.asignarEnvio(numero, codigo);
        vista.mostrarMensaje("Envío asignado. Capacidad disponible: "
                + String.format("%.2f", despacho.calcularCapacidadDisponible()) + " kg");
    }

    private void retirarEnvio() {
        ArrayList<Despacho> conEnvios = new ArrayList<>();
        for (Despacho despacho : gestor.listarDespachosAbiertos()) {
            if (despacho.getEstado() == EstadoDespacho.EN_PREPARACION && !despacho.getEnvios().isEmpty()) {
                conEnvios.add(despacho);
            }
        }
        if (conEnvios.isEmpty()) {
            vista.mostrarError("No hay despachos en preparación con envíos asignados.");
            return;
        }
        vista.mostrarMensaje("Despachos en preparación con envíos:");
        vista.mostrarDespachosAbiertos(conEnvios);
        int numero = vista.leerEntero("Número de despacho: ");
        Despacho despacho = gestor.buscarDespacho(numero);
        while (despacho == null || despacho.getEstado() != EstadoDespacho.EN_PREPARACION
                || despacho.getEnvios().isEmpty()) {
            if (despacho == null) {
                vista.mostrarError("No existe el despacho " + numero + ".");
            } else if (despacho.getEstado() != EstadoDespacho.EN_PREPARACION) {
                vista.mostrarError("El despacho " + numero + " no está en preparación.");
            } else {
                vista.mostrarError("El despacho " + numero + " no tiene envíos asignados.");
            }
            numero = vista.leerEntero("Número de despacho: ");
            despacho = gestor.buscarDespacho(numero);
        }
        vista.mostrarMensaje("Envíos del despacho " + numero + ":");
        vista.mostrarEnvios(despacho.getEnvios());
        String codigo = vista.leerTexto("Código del envío: ");
        Envio envio = gestor.buscarEnvio(codigo);
        while (envio == null || !despacho.getEnvios().contains(envio)) {
            if (codigo.isEmpty()) {
                vista.mostrarError("El código del envío no puede estar vacío.");
            } else if (envio == null) {
                vista.mostrarError("No existe un envío con el código " + codigo + ".");
            } else {
                vista.mostrarError("El envío " + envio.getCodigo() + " no está asignado al despacho " + numero + ".");
            }
            codigo = vista.leerTexto("Código del envío: ");
            envio = gestor.buscarEnvio(codigo);
        }
        gestor.retirarEnvio(numero, codigo);
        vista.mostrarMensaje("Envío retirado del despacho y devuelto a bodega.");
    }

    private void sacarDespacho() {
        int numero = vista.leerEntero("Número de despacho: ");
        double cargos = gestor.sacarDespacho(numero);
        vista.mostrarMensaje("El despacho " + numero + " está en ruta. Cargos por reintento registrados: Q"
                + vista.formatearMonto(cargos));
    }

    private void registrarResultado() {
        ArrayList<Despacho> enRuta = new ArrayList<>();
        for (Despacho despacho : gestor.listarDespachosAbiertos()) {
            if (despacho.getEstado() == EstadoDespacho.EN_RUTA) {
                enRuta.add(despacho);
            }
        }
        if (enRuta.isEmpty()) {
            vista.mostrarError("No hay despachos en ruta.");
            return;
        }
        vista.mostrarMensaje("Despachos en ruta:");
        vista.mostrarDespachosAbiertos(enRuta);
        int numero = vista.leerEntero("Número de despacho: ");
        Despacho despacho = gestor.buscarDespacho(numero);
        while (despacho == null || despacho.getEstado() != EstadoDespacho.EN_RUTA) {
            if (despacho == null) {
                vista.mostrarError("No existe el despacho " + numero + ".");
            } else {
                vista.mostrarError("El despacho " + numero + " no está en ruta.");
            }
            numero = vista.leerEntero("Número de despacho: ");
            despacho = gestor.buscarDespacho(numero);
        }
        ArrayList<Envio> pendientes = new ArrayList<>();
        for (Envio envio : despacho.getEnvios()) {
            if (envio.getEstado() == EstadoEnvio.EN_RUTA) {
                pendientes.add(envio);
            }
        }
        vista.mostrarMensaje("Envíos pendientes de resultado:");
        vista.mostrarEnvios(pendientes);
        String codigo = vista.leerTexto("Código del envío: ");
        Envio envio = gestor.buscarEnvio(codigo);
        while (envio == null || !pendientes.contains(envio)) {
            if (codigo.isEmpty()) {
                vista.mostrarError("El código del envío no puede estar vacío.");
            } else if (envio == null) {
                vista.mostrarError("No existe un envío con el código " + codigo + ".");
            } else {
                vista.mostrarError("El envío " + envio.getCodigo() + " no está pendiente de resultado en el despacho "
                        + numero + ".");
            }
            codigo = vista.leerTexto("Código del envío: ");
            envio = gestor.buscarEnvio(codigo);
        }
        boolean entregado = vista.leerSiNo("¿Se entregó? (s/n): ");
        gestor.registrarResultado(numero, codigo, entregado);
        vista.mostrarMensaje("Resultado registrado. Estado del envío: " + envio.getEstado());
        if (despacho.getEstado() == EstadoDespacho.CERRADO) {
            vista.mostrarMensaje("El despacho " + numero + " quedó cerrado y el vehículo está libre.");
        }
    }

    private void consultarEnvios() {
        vista.mostrarEnvios(gestor.listarEnvios());
    }

    private void consultarVehiculos() {
        vista.mostrarVehiculos(gestor.listarVehiculos());
    }

    private void mostrarReportes() {
        vista.mostrarMensaje("----- Reporte de envíos -----");
        for (String categoria : gestor.obtenerCategorias()) {
            for (EstadoEnvio estado : EstadoEnvio.values()) {
                vista.mostrarConteo(categoria, estado, gestor.contarEnvios(categoria, estado));
            }
        }
        vista.mostrarMensaje("----- Reporte de ingresos -----");
        vista.mostrarIngresos(gestor.calcularIngresoPorRegistro(), gestor.calcularIngresoPorReintentos(),
                gestor.calcularIngresoTotal());
        for (String categoria : gestor.obtenerCategorias()) {
            vista.mostrarIngresoCategoria(categoria, gestor.calcularIngresoPorCategoria(categoria));
        }
        vista.mostrarMensaje("----- Despachos abiertos -----");
        vista.mostrarDespachosAbiertos(gestor.listarDespachosAbiertos());
    }
}

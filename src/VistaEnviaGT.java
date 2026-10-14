import java.util.ArrayList;
import java.util.Scanner;

public class VistaEnviaGT {
    private Scanner scanner;

    public VistaEnviaGT() {
        scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println();
        System.out.println("========== EnvíaGT ==========");
        System.out.println("1. Cotizar y registrar envío");
        System.out.println("2. Registrar vehículo");
        System.out.println("3. Crear despacho");
        System.out.println("4. Asignar envío a despacho");
        System.out.println("5. Retirar envío de despacho");
        System.out.println("6. Hacer salir despacho");
        System.out.println("7. Registrar resultado de entrega");
        System.out.println("8. Consultar envíos");
        System.out.println("9. Consultar vehículos");
        System.out.println("10. Reportes");
        System.out.println("0. Salir");
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número entero.");
            }
        }
    }

    public double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            if (entrada.matches("[-+]?(\\d+(\\.\\d*)?|\\.\\d+)")) {
                double valor = Double.parseDouble(entrada);
                if (!Double.isInfinite(valor)) {
                    return valor;
                }
            }
            mostrarError("Debe ingresar un número. Use punto para los decimales.");
        }
    }

    public boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim().toLowerCase();
            if (entrada.equals("s") || entrada.equals("si") || entrada.equals("sí")) {
                return true;
            }
            if (entrada.equals("n") || entrada.equals("no")) {
                return false;
            }
            mostrarError("Responda s o n.");
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("Error: " + mensaje);
    }

    public String formatearMonto(double monto) {
        return String.format("%.2f", monto);
    }

    public void mostrarCotizacion(String categoria, double costo, int dias) {
        System.out.println("----- Cotización -----");
        System.out.println("Categoría: " + categoria);
        System.out.println("Costo: Q" + formatearMonto(costo));
        System.out.println("Tiempo estimado de entrega: " + dias + " día(s) hábil(es)");
    }

    public void mostrarEnvios(ArrayList<Envio> envios) {
        if (envios.isEmpty()) {
            System.out.println("No hay envíos.");
            return;
        }
        for (Envio envio : envios) {
            System.out.println(envio);
        }
    }

    public void mostrarVehiculos(ArrayList<Vehiculo> vehiculos) {
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehículos.");
            return;
        }
        for (Vehiculo vehiculo : vehiculos) {
            System.out.println(vehiculo);
        }
    }

    public void mostrarDespachosAbiertos(ArrayList<Despacho> despachos) {
        if (despachos.isEmpty()) {
            System.out.println("No hay despachos abiertos.");
            return;
        }
        for (Despacho despacho : despachos) {
            System.out.println(despacho + " | peso cargado: " + String.format("%.2f", despacho.calcularPesoCargado())
                    + " kg | capacidad disponible: " + String.format("%.2f", despacho.calcularCapacidadDisponible())
                    + " kg");
        }
    }

    public void mostrarConteo(String categoria, EstadoEnvio estado, int cantidad) {
        System.out.println(categoria + " | " + estado + ": " + cantidad);
    }

    public void mostrarIngresos(double registro, double reintentos, double total) {
        System.out.println("Ingreso por registro: Q" + formatearMonto(registro));
        System.out.println("Ingreso por reintentos: Q" + formatearMonto(reintentos));
        System.out.println("Ingreso total: Q" + formatearMonto(total));
    }

    public void mostrarIngresoCategoria(String categoria, double ingreso) {
        System.out.println("Ingreso de " + categoria + ": Q" + formatearMonto(ingreso));
    }
}

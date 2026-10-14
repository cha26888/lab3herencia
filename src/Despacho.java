import java.util.ArrayList;

public class Despacho {
    private static final double MARGEN = 0.000001;

    private int numero;
    private Vehiculo vehiculo;
    private ArrayList<Envio> envios;
    private ArrayList<Envio> pendientes;
    private EstadoDespacho estado;

    public Despacho(int numero, Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El despacho necesita un vehículo.");
        }
        vehiculo.ocupar();
        this.numero = numero;
        this.vehiculo = vehiculo;
        this.envios = new ArrayList<>();
        this.pendientes = new ArrayList<>();
        this.estado = EstadoDespacho.EN_PREPARACION;
    }

    public void asignarEnvio(Envio envio) {
        if (estado != EstadoDespacho.EN_PREPARACION) {
            throw new IllegalStateException("El despacho " + numero + " no está en preparación.");
        }
        if (envio == null) {
            throw new IllegalArgumentException("El envío no existe.");
        }
        if (envio.getEstado() != EstadoEnvio.EN_BODEGA) {
            throw new IllegalStateException("El envío " + envio.getCodigo() + " no está en bodega. Estado actual: "
                    + envio.getEstado() + ".");
        }
        if (!envio.esCompatibleCon(vehiculo)) {
            throw new IllegalStateException("El envío " + envio.getCodigo() + " (" + envio.getCategoria()
                    + ") no puede ir en el vehículo " + vehiculo.getPlaca() + ".");
        }
        if (calcularPesoCargado() + envio.getPesoReal() > vehiculo.getCapacidadMaxima() + MARGEN) {
            throw new IllegalStateException("El envío " + envio.getCodigo() + " supera la capacidad disponible del vehículo "
                    + vehiculo.getPlaca() + " (" + String.format("%.2f", calcularCapacidadDisponible()) + " kg).");
        }
        envio.asignar();
        envios.add(envio);
    }

    public void retirarEnvio(Envio envio) {
        if (estado != EstadoDespacho.EN_PREPARACION) {
            throw new IllegalStateException("El despacho " + numero + " no está en preparación.");
        }
        if (envio == null || !envios.contains(envio)) {
            throw new IllegalArgumentException("El envío no está asignado al despacho " + numero + ".");
        }
        envio.retirarDeDespacho();
        envios.remove(envio);
    }

    public double salir() {
        if (estado != EstadoDespacho.EN_PREPARACION) {
            throw new IllegalStateException("El despacho " + numero + " no está en preparación.");
        }
        if (envios.isEmpty()) {
            throw new IllegalStateException("El despacho " + numero + " no tiene envíos asignados.");
        }
        double cargos = 0;
        for (Envio envio : envios) {
            cargos += envio.registrarSalida();
        }
        pendientes.addAll(envios);
        estado = EstadoDespacho.EN_RUTA;
        return cargos;
    }

    public void registrarResultado(Envio envio, boolean entregado) {
        if (estado != EstadoDespacho.EN_RUTA) {
            throw new IllegalStateException("El despacho " + numero + " no está en ruta.");
        }
        if (envio == null || !pendientes.contains(envio)) {
            throw new IllegalArgumentException("El envío no está pendiente de resultado en el despacho " + numero + ".");
        }
        envio.registrarResultado(entregado);
        pendientes.remove(envio);
        if (pendientes.isEmpty()) {
            cerrar();
        }
    }

    private void cerrar() {
        estado = EstadoDespacho.CERRADO;
        vehiculo.liberar();
    }

    public double calcularPesoCargado() {
        double total = 0;
        for (Envio envio : envios) {
            total += envio.getPesoReal();
        }
        return total;
    }

    public double calcularCapacidadDisponible() {
        return vehiculo.getCapacidadMaxima() - calcularPesoCargado();
    }

    @Override
    public String toString() {
        return "Despacho " + numero + " | " + estado + " | vehículo " + vehiculo.getPlaca();
    }

    public int getNumero() {
        return numero;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public ArrayList<Envio> getEnvios() {
        return new ArrayList<>(envios);
    }

    public EstadoDespacho getEstado() {
        return estado;
    }
}

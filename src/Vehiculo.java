public class Vehiculo {
    private String placa;
    private double capacidadMaxima;
    private boolean refrigerado;
    private boolean libre;

    public Vehiculo(String placa, double capacidadMaxima, boolean refrigerado) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía.");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor que 0.");
        }
        this.placa = placa.trim();
        this.capacidadMaxima = capacidadMaxima;
        this.refrigerado = refrigerado;
        this.libre = true;
    }

    public void ocupar() {
        if (!libre) {
            throw new IllegalStateException("El vehículo " + placa + " ya tiene un despacho sin cerrar.");
        }
        libre = false;
    }

    public void liberar() {
        libre = true;
    }

    @Override
    public String toString() {
        String tipo = refrigerado ? "refrigerado" : "no refrigerado";
        String disponibilidad = libre ? "libre" : "ocupado";
        return placa + " | capacidad: " + capacidadMaxima + " kg | " + tipo + " | " + disponibilidad;
    }

    public String getPlaca() {
        return placa;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean isRefrigerado() {
        return refrigerado;
    }

    public boolean isLibre() {
        return libre;
    }
}

public class Paquete extends Envio {
    private static final double TARIFA_POR_KG = 8;
    private static final double DIVISOR_VOLUMETRICO = 5000;
    private static final int DIAS_URBANO = 2;
    private static final int DIAS_DEPARTAMENTAL = 4;

    private double largo;
    private double ancho;
    private double alto;

    public Paquete(String codigo, String remitente, String destinatario, double pesoReal, TipoDestino destino,
            double largo, double ancho, double alto) {
        super(codigo, remitente, destinatario, pesoReal, destino);
        if (largo <= 0 || ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("El largo, el ancho y el alto deben ser mayores que 0.");
        }
        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
    }

    public double calcularPesoVolumetrico() {
        return largo * ancho * alto / DIVISOR_VOLUMETRICO;
    }

    public double calcularPesoFacturable() {
        return Math.max(getPesoReal(), calcularPesoVolumetrico());
    }

    @Override
    public double calcularCosto() {
        double kilos = Math.ceil(Math.round(calcularPesoFacturable() * 1000000.0) / 1000000.0);
        return getTarifaBase() + TARIFA_POR_KG * kilos;
    }

    @Override
    public int calcularDiasEntrega() {
        if (getDestino() == TipoDestino.URBANO) {
            return DIAS_URBANO;
        }
        return DIAS_DEPARTAMENTAL;
    }

    @Override
    public int getIntentosPermitidos() {
        return INTENTOS_ESTANDAR;
    }

    @Override
    public String getCategoria() {
        return "Paquete";
    }

    @Override
    public String toString() {
        return super.toString() + " | " + largo + " x " + ancho + " x " + alto + " cm";
    }

    public double getLargo() {
        return largo;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }
}

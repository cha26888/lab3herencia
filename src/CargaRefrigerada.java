public class CargaRefrigerada extends Envio {
    public static final double TEMPERATURA_MINIMA = -25;
    public static final double TEMPERATURA_MAXIMA = 8;
    private static final double COSTO_POR_KG = 12;
    private static final double CARGO_CADENA_FRIO = 60;
    private static final int DIAS_ENTREGA = 1;
    private static final int INTENTOS_PERMITIDOS = 1;

    private double temperatura;

    public CargaRefrigerada(String codigo, String remitente, String destinatario, double pesoReal,
            TipoDestino destino, double temperatura) {
        super(codigo, remitente, destinatario, pesoReal, destino);
        if (destino != TipoDestino.URBANO) {
            throw new IllegalArgumentException("La carga refrigerada solo acepta destino urbano.");
        }
        if (temperatura < TEMPERATURA_MINIMA || temperatura > TEMPERATURA_MAXIMA) {
            throw new IllegalArgumentException("La temperatura debe estar entre " + TEMPERATURA_MINIMA + " y "
                    + TEMPERATURA_MAXIMA + " grados Celsius.");
        }
        this.temperatura = temperatura;
    }

    @Override
    public double calcularCosto() {
        return getTarifaBase() + COSTO_POR_KG * getPesoReal() + CARGO_CADENA_FRIO;
    }

    @Override
    public int calcularDiasEntrega() {
        return DIAS_ENTREGA;
    }

    @Override
    public int getIntentosPermitidos() {
        return INTENTOS_PERMITIDOS;
    }

    @Override
    public String getCategoria() {
        return "Carga refrigerada";
    }

    @Override
    public boolean esCompatibleCon(Vehiculo vehiculo) {
        return vehiculo.isRefrigerado();
    }

    @Override
    public String toString() {
        return super.toString() + " | temperatura: " + temperatura + " grados C";
    }

    public double getTemperatura() {
        return temperatura;
    }
}

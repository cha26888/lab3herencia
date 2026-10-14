public class Sobre extends Envio {
    public static final double PESO_MAXIMO = 0.5;
    private static final double COSTO_URBANO = 20;
    private static final double COSTO_DEPARTAMENTAL = 35;
    private static final int DIAS_URBANO = 1;
    private static final int DIAS_DEPARTAMENTAL = 2;

    public Sobre(String codigo, String remitente, String destinatario, double pesoReal, TipoDestino destino) {
        super(codigo, remitente, destinatario, pesoReal, destino);
        if (pesoReal > PESO_MAXIMO) {
            throw new IllegalArgumentException("Un sobre no puede pesar más de " + PESO_MAXIMO + " kg.");
        }
    }

    @Override
    public double calcularCosto() {
        if (getDestino() == TipoDestino.URBANO) {
            return COSTO_URBANO;
        }
        return COSTO_DEPARTAMENTAL;
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
        return "Sobre";
    }
}

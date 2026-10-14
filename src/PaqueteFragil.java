public class PaqueteFragil extends Paquete {
    private static final double EMBALAJE = 15;
    private static final double PORCENTAJE_SEGURO = 0.03;

    private double valorDeclarado;

    public PaqueteFragil(String codigo, String remitente, String destinatario, double pesoReal, TipoDestino destino,
            double largo, double ancho, double alto, double valorDeclarado) {
        super(codigo, remitente, destinatario, pesoReal, destino, largo, ancho, alto);
        if (valorDeclarado <= 0) {
            throw new IllegalArgumentException("El valor declarado debe ser mayor que 0.");
        }
        this.valorDeclarado = valorDeclarado;
    }

    public double calcularSeguro() {
        return valorDeclarado * PORCENTAJE_SEGURO;
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + EMBALAJE + calcularSeguro();
    }

    @Override
    public int calcularDiasEntrega() {
        return super.calcularDiasEntrega() + 1;
    }

    @Override
    public int getIntentosPermitidos() {
        return 2;
    }

    @Override
    public String getCategoria() {
        return "Paquete frágil";
    }

    @Override
    public String toString() {
        return super.toString() + " | valor declarado: Q" + String.format("%.2f", valorDeclarado);
    }

    public double getValorDeclarado() {
        return valorDeclarado;
    }
}

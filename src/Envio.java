public abstract class Envio {
    private static final double CARGO_REINTENTO = 15;
    protected static final int INTENTOS_ESTANDAR = 3;

    private String codigo;
    private String remitente;
    private String destinatario;
    private double pesoReal;
    private TipoDestino destino;
    private EstadoEnvio estado;
    private int intentosRealizados;
    private double ingresoRegistro;
    private double ingresoReintentos;

    protected Envio(String codigo, String remitente, String destinatario, double pesoReal, TipoDestino destino) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del envío no puede estar vacío.");
        }
        if (remitente == null || remitente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del remitente no puede estar vacío.");
        }
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del destinatario no puede estar vacío.");
        }
        if (pesoReal <= 0) {
            throw new IllegalArgumentException("El peso real debe ser mayor que 0.");
        }
        if (destino == null) {
            throw new IllegalArgumentException("El destino debe ser urbano o departamental.");
        }
        this.codigo = codigo.trim();
        this.remitente = remitente.trim();
        this.destinatario = destinatario.trim();
        this.pesoReal = pesoReal;
        this.destino = destino;
        this.estado = EstadoEnvio.EN_BODEGA;
        this.intentosRealizados = 0;
        this.ingresoRegistro = 0;
        this.ingresoReintentos = 0;
    }

    public abstract double calcularCosto();

    public abstract int calcularDiasEntrega();

    public abstract int getIntentosPermitidos();

    public abstract String getCategoria();

    public boolean esCompatibleCon(Vehiculo vehiculo) {
        return true;
    }

    protected double getTarifaBase() {
        if (destino == TipoDestino.URBANO) {
            return 25;
        }
        return 40;
    }

    protected double calcularCargoReintento() {
        return CARGO_REINTENTO;
    }

    public void cobrarRegistro() {
        if (ingresoRegistro > 0) {
            throw new IllegalStateException("El envío " + codigo + " ya fue cobrado.");
        }
        ingresoRegistro = calcularCosto();
    }

    public void asignar() {
        if (estado != EstadoEnvio.EN_BODEGA) {
            throw new IllegalStateException("El envío " + codigo + " no está en bodega. Estado actual: " + estado + ".");
        }
        estado = EstadoEnvio.ASIGNADO;
    }

    public void retirarDeDespacho() {
        if (estado != EstadoEnvio.ASIGNADO) {
            throw new IllegalStateException("El envío " + codigo + " no está asignado a un despacho en preparación.");
        }
        estado = EstadoEnvio.EN_BODEGA;
    }

    public double registrarSalida() {
        if (estado != EstadoEnvio.ASIGNADO) {
            throw new IllegalStateException("El envío " + codigo + " no está asignado a un despacho.");
        }
        estado = EstadoEnvio.EN_RUTA;
        intentosRealizados++;
        double cargo = 0;
        if (intentosRealizados > 1) {
            cargo = calcularCargoReintento();
            ingresoReintentos += cargo;
        }
        return cargo;
    }

    public void registrarResultado(boolean entregado) {
        if (estado != EstadoEnvio.EN_RUTA) {
            throw new IllegalStateException("El envío " + codigo + " no está en ruta.");
        }
        if (entregado) {
            estado = EstadoEnvio.ENTREGADO;
        } else if (intentosRealizados >= getIntentosPermitidos()) {
            estado = EstadoEnvio.DEVUELTO_AL_REMITENTE;
        } else {
            estado = EstadoEnvio.EN_BODEGA;
        }
    }

    public double getIngresoTotal() {
        return ingresoRegistro + ingresoReintentos;
    }

    @Override
    public String toString() {
        return codigo + " | " + getCategoria() + " | " + remitente + " -> " + destinatario + " | " + destino
                + " | " + pesoReal + " kg | " + estado + " | intentos: " + intentosRealizados + " de "
                + getIntentosPermitidos();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public double getPesoReal() {
        return pesoReal;
    }

    public TipoDestino getDestino() {
        return destino;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public int getIntentosRealizados() {
        return intentosRealizados;
    }

    public double getIngresoRegistro() {
        return ingresoRegistro;
    }

    public double getIngresoReintentos() {
        return ingresoReintentos;
    }
}

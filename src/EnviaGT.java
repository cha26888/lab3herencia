import java.util.ArrayList;

public class EnviaGT {
    private ArrayList<Envio> envios;
    private ArrayList<Vehiculo> vehiculos;
    private ArrayList<Despacho> despachos;
    private int siguienteNumeroDespacho;

    public EnviaGT() {
        envios = new ArrayList<>();
        vehiculos = new ArrayList<>();
        despachos = new ArrayList<>();
        siguienteNumeroDespacho = 1;
    }

    public void registrarEnvio(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no existe.");
        }
        if (buscarEnvio(envio.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un envío con el código " + envio.getCodigo() + ".");
        }
        envio.cobrarRegistro();
        envios.add(envio);
    }

    public void cargarEnvioInicial(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envío no existe.");
        }
        if (buscarEnvio(envio.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un envío con el código " + envio.getCodigo() + ".");
        }
        envios.add(envio);
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no existe.");
        }
        if (buscarVehiculo(vehiculo.getPlaca()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con la placa " + vehiculo.getPlaca() + ".");
        }
        vehiculos.add(vehiculo);
    }

    public Despacho crearDespacho(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehículo con la placa " + placa + ".");
        }
        Despacho despacho = new Despacho(siguienteNumeroDespacho, vehiculo);
        despachos.add(despacho);
        siguienteNumeroDespacho++;
        return despacho;
    }

    public void asignarEnvio(int numeroDespacho, String codigo) {
        Despacho despacho = buscarDespacho(numeroDespacho);
        if (despacho == null) {
            throw new IllegalArgumentException("No existe el despacho " + numeroDespacho + ".");
        }
        Envio envio = buscarEnvio(codigo);
        if (envio == null) {
            throw new IllegalArgumentException("No existe un envío con el código " + codigo + ".");
        }
        despacho.asignarEnvio(envio);
    }

    public void retirarEnvio(int numeroDespacho, String codigo) {
        Despacho despacho = buscarDespacho(numeroDespacho);
        if (despacho == null) {
            throw new IllegalArgumentException("No existe el despacho " + numeroDespacho + ".");
        }
        Envio envio = buscarEnvio(codigo);
        if (envio == null) {
            throw new IllegalArgumentException("No existe un envío con el código " + codigo + ".");
        }
        despacho.retirarEnvio(envio);
    }

    public double sacarDespacho(int numeroDespacho) {
        Despacho despacho = buscarDespacho(numeroDespacho);
        if (despacho == null) {
            throw new IllegalArgumentException("No existe el despacho " + numeroDespacho + ".");
        }
        return despacho.salir();
    }

    public void registrarResultado(int numeroDespacho, String codigo, boolean entregado) {
        Despacho despacho = buscarDespacho(numeroDespacho);
        if (despacho == null) {
            throw new IllegalArgumentException("No existe el despacho " + numeroDespacho + ".");
        }
        Envio envio = buscarEnvio(codigo);
        if (envio == null) {
            throw new IllegalArgumentException("No existe un envío con el código " + codigo + ".");
        }
        despacho.registrarResultado(envio, entregado);
    }

    public Envio buscarEnvio(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Envio envio : envios) {
            if (envio.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return envio;
            }
        }
        return null;
    }

    public Vehiculo buscarVehiculo(String placa) {
        if (placa == null) {
            return null;
        }
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                return vehiculo;
            }
        }
        return null;
    }

    public Despacho buscarDespacho(int numero) {
        for (Despacho despacho : despachos) {
            if (despacho.getNumero() == numero) {
                return despacho;
            }
        }
        return null;
    }

    public ArrayList<Envio> listarEnvios() {
        return new ArrayList<>(envios);
    }

    public ArrayList<Vehiculo> listarVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public ArrayList<Despacho> listarDespachosAbiertos() {
        ArrayList<Despacho> abiertos = new ArrayList<>();
        for (Despacho despacho : despachos) {
            if (despacho.getEstado() != EstadoDespacho.CERRADO) {
                abiertos.add(despacho);
            }
        }
        return abiertos;
    }

    public ArrayList<String> obtenerCategorias() {
        ArrayList<String> categorias = new ArrayList<>();
        for (Envio envio : envios) {
            if (!categorias.contains(envio.getCategoria())) {
                categorias.add(envio.getCategoria());
            }
        }
        return categorias;
    }

    public int contarEnvios(String categoria, EstadoEnvio estado) {
        int cantidad = 0;
        for (Envio envio : envios) {
            if (envio.getCategoria().equals(categoria) && envio.getEstado() == estado) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public double calcularIngresoPorRegistro() {
        double total = 0;
        for (Envio envio : envios) {
            total += envio.getIngresoRegistro();
        }
        return total;
    }

    public double calcularIngresoPorReintentos() {
        double total = 0;
        for (Envio envio : envios) {
            total += envio.getIngresoReintentos();
        }
        return total;
    }

    public double calcularIngresoTotal() {
        return calcularIngresoPorRegistro() + calcularIngresoPorReintentos();
    }

    public double calcularIngresoPorCategoria(String categoria) {
        double total = 0;
        for (Envio envio : envios) {
            if (envio.getCategoria().equals(categoria)) {
                total += envio.getIngresoTotal();
            }
        }
        return total;
    }
}

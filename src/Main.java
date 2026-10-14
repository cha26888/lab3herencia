public class Main {
    public static void main(String[] args) {
        EnviaGT gestor = new EnviaGT();
        VistaEnviaGT vista = new VistaEnviaGT();
        ControladorEnviaGT controlador = new ControladorEnviaGT(gestor, vista);
        controlador.iniciar();
    }
}

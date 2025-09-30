import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class ImpresoraSemaforo {
    private static final Semaphore impresora = new Semaphore(1, true); // semáforo binario justo

    public static void usarImpresora(int documentoId) {
        try {
            int delay = ThreadLocalRandom.current().nextInt(100, 501);
            System.out.println("Documento #" + documentoId + " espera " + delay + "ms...");
            Thread.sleep(delay);

            impresora.acquire(); // P()
            try {
                System.out.println("Imprimiendo documento #" + documentoId + "...");
                Thread.sleep(500); // simular impresión
                System.out.println("Documento #" + documentoId + " completado.");
            } finally {
                impresora.release(); // V()
                System.out.println("Documento #" + documentoId + " liberó la impresora.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int numDocumentos = 5;
        Thread[] hilos = new Thread[numDocumentos];

        System.out.println("=== Simulación de impresión con SEMÁFORO ===\n");

        for (int i = 0; i < numDocumentos; i++) {
            final int id = i + 1;
            hilos[i] = new Thread(() -> usarImpresora(id));
            hilos[i].start();
        }
        for (Thread t : hilos) t.join();

        System.out.println("\nTodos los documentos han sido impresos.");
    }
}

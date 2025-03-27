package pcd.ass01;

public class Administrator {
    private final int numThreads;
    private int waitingThreads;

    public Administrator(int numThreads) {
        this.numThreads = numThreads;
        this.waitingThreads = 0;
    }

    public synchronized void threadDone() {
        waitingThreads++;
        if (waitingThreads == numThreads) {
            notifyAll(); // Sblocca tutti i thread in attesa
        }
    }

    public synchronized void waitThreads() {
        while (waitingThreads < numThreads) { // Attendi che tutti i thread abbiano terminato
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
            }
        }
    }

    public synchronized void signalDone() {
        waitingThreads = 0;
        notifyAll(); // Sblocca eventuali thread in attesa di un nuovo ciclo
    }
}

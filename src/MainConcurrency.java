public class MainConcurrency {
    Object o1 = new Object();
    Object o2 = new Object();

    public static void main(String[] args) {
        new MainConcurrency().runDeadlock();
    }

    void runDeadlock() {
        getThread(o1, o2).start();
        getThread(o2, o1).start();
    }

    private Thread getThread(Object first, Object second) {
        return new Thread(() -> {
            synchronized (first) {
                System.out.println("printing first: " + first + ", from: " + Thread.currentThread().getName());
                Thread.yield();
                synchronized (second) {
                    System.out.println("never gets here: " + second + ", from: " + Thread.currentThread().getName());
                }
            }
        });
    }
}

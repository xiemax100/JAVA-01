import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeWork04 {
    public static void main(String[] args) throws Exception {
        HomeWork04 homeWork = new HomeWork04();
        homeWork.runAndGetResultWithFuture();
        homeWork.runAndGetResultWithFutureTask();
        homeWork.runAndGetResultWithCompletableFuture();
        homeWork.runAndGetResultWithCompletionService();
        homeWork.runAndGetResultWithCountDownLatch();
        homeWork.runAndGetResultWithJoin();

        // 输出结果：
        // ===== 使用 Future
        // 异步计算结果为：24157817
        // 使用时间：125 ms
        // ===== 使用 FutureTask
        // 异步计算结果为：24157817
        // 使用时间：63 ms
        // ===== 使用 CompletableFuture
        // 异步计算结果为：24157817
        // 使用时间：62 ms
        // ===== 使用 CompletionService
        // 异步计算结果为：24157817
        // 使用时间：78 ms
        // ===== 使用 CountDownLatch
        // 异步计算结果为：24157817
        // 使用时间：47 ms
        // ===== 使用 Join
        // 异步计算结果为：24157817
        // 使用时间：63 ms
    }

    private static final int FIBONACCI_CONST = 36;

    private static int fibonacci(int a) {
        if (a < 2)
            return 1;
        return fibonacci(a - 1) + fibonacci(a - 2);
    }

    void runAndGetResultWithFuture() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("===== 使用 Future");

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(() -> fibonacci(FIBONACCI_CONST));

        System.out.println("异步计算结果为：" + future.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        executorService.shutdown();
    }

    void runAndGetResultWithFutureTask() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("===== 使用 FutureTask");

        FutureTask<Integer> futureTask = new FutureTask<>(() -> fibonacci(FIBONACCI_CONST));
        new Thread(futureTask).start();

        System.out.println("异步计算结果为：" + futureTask.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    void runAndGetResultWithCompletableFuture() {
        long start = System.currentTimeMillis();
        System.out.println("===== 使用 CompletableFuture");

        Integer result = CompletableFuture.supplyAsync(() -> fibonacci(FIBONACCI_CONST)).join();

        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    void runAndGetResultWithCompletionService() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("===== 使用 CompletionService");

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);
        completionService.submit(() -> fibonacci(FIBONACCI_CONST));

        System.out.println("异步计算结果为：" + completionService.take().get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        executorService.shutdown();
    }

    void runAndGetResultWithCountDownLatch() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("===== 使用 CountDownLatch");

        AtomicInteger result = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            result.set(fibonacci(FIBONACCI_CONST));
            latch.countDown();
        }).start();
        latch.await();

        System.out.println("异步计算结果为：" + result.get());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    void runAndGetResultWithJoin() throws Exception {

        class Internal implements Runnable {

            private int value;

            @Override
            public void run() {
                value = fibonacci(FIBONACCI_CONST);
            }

            public int getValue() {
                return value;
            }
        }

        long start = System.currentTimeMillis();
        System.out.println("===== 使用 Join");

        Internal internal = new Internal();
        Thread t = new Thread(internal);
        t.start();
        t.join();

        System.out.println("异步计算结果为：" + internal.getValue());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

}

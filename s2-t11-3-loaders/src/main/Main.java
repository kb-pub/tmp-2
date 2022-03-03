package main;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws Exception {
        var loaderFactory = new LoaderFactory("/home/kb/test/v1");

        ManagementFactory.getPlatformMBeanServer().registerMBean(
                loaderFactory,
                new ObjectName("LoaderFactoryAgent:name=factory")
        );

//        while (true) {
//            System.out.println("[" + LocalDateTime.now() + "] =>");
//
//            var loader = loaderFactory.getLoader();
//            var clazz = loader.loadClass("animals.Main");
//            clazz.getMethod("main", String[].class).invoke(null, (Object) args);
//
//            Thread.sleep(2000);
//        }

        while (true) {
            var thread = new Thread(() -> {
                System.out.println("[" + LocalDateTime.now() + "] =>");
                try {
                    var clazz = Thread.currentThread().getContextClassLoader()
                            .loadClass("animals.Main");
                    clazz.getMethod("main", String[].class).invoke(null, (Object) args);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.setContextClassLoader(loaderFactory.getLoader());
            thread.start();

            Thread.sleep(2000);
        }
    }
}

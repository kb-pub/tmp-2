package main;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) throws Exception {
        var loaderFactory = new LoaderFactory("/home/kb/test/v1");
        ManagementFactory.getPlatformMBeanServer().registerMBean(
                loaderFactory,
                new ObjectName("LoaderFactoryAgent:name=factory")
        );

        while (true) {
            var loader = loaderFactory.getLoader();
            var clazz = loader.loadClass("animals.Main");
            clazz.getMethod("main", String[].class).invoke(null, (Object) args);

            Thread.sleep(2000);
        }
    }
}

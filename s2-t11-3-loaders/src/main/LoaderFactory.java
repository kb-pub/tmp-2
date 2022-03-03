package main;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoaderFactory implements LoaderFactoryMBean {
    private ClassLoader loader;

    public LoaderFactory(String dir) throws Exception {
        loader = newLoader(dir);
    }

    public void reload(String dir) throws Exception {
        loader = newLoader(dir);
    }

    private ClassLoader newLoader(String dir) throws Exception {
        var path = Path.of(dir);
        if (!Files.isDirectory(path))
            throw new RuntimeException("incorrect directory path");
        return new URLClassLoader(new URL[] { path.toUri().toURL() });
    }

    public ClassLoader getLoader() {
        return loader;
    }
}

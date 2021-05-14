package main;

public interface LoaderFactoryMBean {
    void reload(String dir) throws Exception;
    String giveMeString(String value);
}

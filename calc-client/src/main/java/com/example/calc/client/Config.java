package com.example.calc.client;

import ru.qatools.properties.Property;
import ru.qatools.properties.PropertyLoader;
import ru.qatools.properties.Resource;
import ru.qatools.properties.providers.MapPropPathReplacerProvider;

@Resource.Classpath("client.properties")
enum Config {

    INSTANCE;

    @Property("base.url")
    private String baseUrl;

    @Property("connection.timeout")
    private Long connectionTimeout;

    @Property("read.timeout")
    private Long readTimeout;

    Config(){
        PropertyLoader
                .newInstance()
                .withPropertyProvider(new MapPropPathReplacerProvider(System.getProperties()))
                .populate(this);
    }

    public String baseUrl(){
        return baseUrl;
    }

    public Long connectionTimeout(){
        return connectionTimeout;
    }

    public Long readTimeout(){
        return readTimeout;
    }
}

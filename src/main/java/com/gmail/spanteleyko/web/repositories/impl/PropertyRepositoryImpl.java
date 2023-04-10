package com.gmail.spanteleyko.web.repositories.impl;

import com.gmail.spanteleyko.web.repositories.PropertyRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class PropertyRepositoryImpl implements PropertyRepository {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final String CONFIG_PROPERTY_FILE_LOCATION = "config.properties";
    private static Properties properties;
    private static PropertyRepository propertyRepository;

    public static PropertyRepository getInstance() {
        if (propertyRepository == null) {
            propertyRepository = new PropertyRepositoryImpl();
            properties = new Properties();
            try {
                InputStream propertiesStream = PropertyRepositoryImpl.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTY_FILE_LOCATION);
                properties.load(propertiesStream);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new IllegalArgumentException("Config property file is not found");
            }
        }

        return propertyRepository;
    }

    @Override
    public String getProperty(String name) {
        return properties.getProperty(name);
    }
}

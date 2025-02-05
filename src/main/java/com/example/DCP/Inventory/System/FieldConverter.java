package com.example.DCP.Inventory.System;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FieldConverter implements AttributeConverter<String, String> {

    private static final String ENCRYPTION_PASSWORD = "DCP";

    private static final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    static {
        encryptor.setPassword(ENCRYPTION_PASSWORD);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptor.decrypt(dbData);
    }
}
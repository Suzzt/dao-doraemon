package org.dao.doraemon.example.sensitive;

import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.handler.PasswordHandler;

/**
 * @author sucf
 * @since 1.0
 */
@SensitiveMapping(fieldName = "password", handler = PasswordHandler.class)
public class AppleClassVO extends AppleVO {
}
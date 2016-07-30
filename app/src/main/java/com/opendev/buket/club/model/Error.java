package com.opendev.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class Error {

    private static final int CODE_11 = 11;
    private static final int CODE_12 = 12;
    private static final int CODE_21 = 21;
    private static final int CODE_22 = 22;
    private static final int CODE_23 = 23;

    public static final int CODE_200 = 200;
    public static final int CODE_201 = 201;
    private static final int CODE_204 = 204;
    private static final int CODE_400 = 400;
    private static final int CODE_401 = 401;
    private static final int CODE_404 = 404;
    private static final int CODE_422 = 422;


    private static final String CODE_11_DESK = "ошибка при валидации модели";
    private static final String CODE_12_DESK = "запись не найдена";
    private static final String CODE_21_DESK = "ивалидный токен аутентификации";
    private static final String CODE_22_DESK = "инвалидные данные аутентификации";
    private static final String CODE_23_DESK = "нет прав для выполнения действия";

    private static final String CODE_200_DESK = "the request was successful";
    private static final String CODE_201_DESK = "the request was successful and a resource was created";
    private static final String CODE_204_DESK = "the request was successful but there is no representation to return";
    private static final String CODE_400_DESK = "the request could not be understood or was missing required parameters";
    private static final String CODE_401_DESK = "authentication failed or user doesn't have permissions for requested operation";
    private static final String CODE_404_DESK = "resource was not found";
    private static final String CODE_422_DESK = "requested data contain invalid values";

    private static final String CODE_UNKNOW_DESK = "Неизвестная ошибка";

    @JsonProperty(Fields.CODE)
    private int code;

    @JsonProperty(Fields.TYPE)
    private String type;

    @JsonProperty(Fields.MESSAGE)
    private String message;

    @JsonProperty(Fields.ERRORS)
    private String errors;

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getGetCodeDesc() {
        switch (code) {
            case CODE_11:
                return CODE_11_DESK;
            case CODE_12:
                return CODE_12_DESK;
            case CODE_21:
                return CODE_21_DESK;
            case CODE_22:
                return CODE_22_DESK;
            case CODE_23:
                return CODE_23_DESK;
            case CODE_200:
                return CODE_200_DESK;
            case CODE_201:
                return CODE_201_DESK;
            case CODE_204:
                return CODE_204_DESK;
            case CODE_400:
                return CODE_400_DESK;
            case CODE_401:
                return CODE_401_DESK;
            case CODE_404:
                return CODE_404_DESK;
            case CODE_422:
                return CODE_422_DESK;
            default:
                return CODE_UNKNOW_DESK + " : " + code;
        }
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatusDone() {
        return code == 0 || code == Error.CODE_200 || code == Error.CODE_201;
    }
}

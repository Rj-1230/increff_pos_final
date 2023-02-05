package com.increff.pos.util;

import javax.validation.*;
import java.util.Set;

public class ValidationUtil {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    public static <T> void validateForm(T form){
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
//
//    private static <T> Set<ConstraintViolation<T>> validate(T t) {
//        return factory.getValidator().validate(t);
//    }
//
//    public static <T> void checkValidList(List<T> collection) throws ApiException {
//        for(T obj : collection)
//            checkValid(obj);
//    }
//
//    public static void checkNull(Object obj, String message) throws ApiException {
//        if (obj != null) {
//            throw new ApiException(ApiStatus.BAD_DATA, message);
//        }
//    }
//
//    public static <T> void checkValid(T obj) throws ApiException {
//        Set<ConstraintViolation<T>> violations = ValidationUtil.validate(obj);
//        if (violations.isEmpty()) {
//            return;
//        }
//        List<FieldErrorData> errorList = new ArrayList<FieldErrorData>(violations.size());
//        for (ConstraintViolation<T> violation : violations) {
//            FieldErrorData error = new FieldErrorData();
//            error.setCode("");
//            error.setField(violation.getPropertyPath().toString());
//            error.setMessage(violation.getMessage());
//            errorList.add(error);
//        }
//        throw new ApiException("Input validation failed", errorList);
//    }
}
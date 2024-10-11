package fib.br10.utility;


public final class Messages {

    public static final String USER_EXISTS_SAME_PHONE_NUMBER = "user.exists-same-phoneNumber";
    public static final String USER_EXISTS_SAME_USERNAME ="user.exists-same-username" ;

    private Messages() {
    }

    //Success
    public static final String SUCCESS = "default.success";

    //Error
    public static final String ERROR = "default.error";

    //User
    public static final String USER_ID_REQUIRED = "user.id-required";
    public static final String USER_NOT_FOUND = "user.not-found";
    public static final String USER_NOT_ACTIVE = "user.not-active";
    public static final String PHONE_NUMBER_OR_USERNAME_NOT_FOUND = "phoneNumberOrUsername.not-found";
    public static final String CONFIRM_PASSWORD_NOT_MATCH = "confirm-password.not-match";
    public static final String USERNAME_REQUIRED = "user.username-required";
    public static final String EMAIL_REQUIRED = "user.email-required";
    public static final String PASSWORD_REQUIRED = "user.password-required";
    public static final String PHONE_NUMBER_REQUIRED = "user.phoneNumber-required";
    public static final String USER_TYPE_REQUIRED = "user.userType-required";


    //Auth
    //token
    public static final String TOKEN_NOT_VALID = "auth.token.not-valid";
    public static final String TOKEN_REQUIRED = "auth.token.required";
    public static final String JWT_REQUIRED = "auth.jwt.required";
    public static final String JWT_INVALID_CLAIM = "auth.jwt.invalid-claim";
    public static final String JWT_EXPIRED = "auth.jwt.expired";
    public static final String REFRESH_TOKEN_REQUIRED = "auth.jwt.refresh-token-required";
    public static final String ACCESS_TOKEN_REQUIRED = "auth.jwt.accesss-token-required";
    public static final String JWT_AT_BLACKLIST = "auth.jwt.at-black-list";
    public static final String DEVICE_INFO_REQUIRED = "auth.device-info-required";

    public static final String REGISTER_TYPE_REQUIRED = "auth.register-type-required";


    //otp
    public static final String OTP_REQUIRED = "auth.otp.required";
    public static final String OTP_EXPIRED = "auth.otp.expired";
    public static final String OTP_NOT_FOUND = "auth.otp.not-found";
    public static final String OTP_NOT_VALID = "auth.otp.not-valid";
    public static final String PHONE_NUMBER_OR_USERNAME_REQUIRED = "auth.otp.phoneNumberOrUsername-required";

    // Specialist-Profile
    public static final String SPECIALITY_REQUIRED = "specialist-profile.speciality-required";
    public static final String SPECIALIST_PROFILE_NOT_FOUND = "specialist-profile.not-found";
    public static final String SPECIALIST_PROFILE_ALREADY_EXISTS = "specialist-profile.already-exists";
    public static final String BLOCKED_CUSTOMER_NOT_FOUND = "specialist-profile.blocked-customer-not-found";



    // Specialist-Service
    public static final String SPECIALIST_SERVICE_NOT_FOUND = "specialistService.not-found";
    public static final String SPECIALIST_SERVICE_ALREADY_EXISTS = "specialistService.already-exists";
    public static final String SPECIALIST_SERVICE_NAME_REQUIRED = "specialistService.name-required";
    public static final String SPECIALIST_SERVICE_PRICE_REQUIRED = "specialistService.price-required";
    public static final String SERVICE_ALREADY_USED_ON_ANY_RESERVATION = "specialistService.already-used-on-any-reservation";

    //general

    //    --------------------------------------------------------
    //Reservation
    public static final String RESERVATION_DATE_REQUIRED = "reservation.date-required";
    public static final String SPECIALIST_USER_ID_REQUIRED = "reservation.specialistUserId-required";
    public static final String CUSTOMER_USER_ID_REQUIRED = "reservation.customerUserId-required";
    public static final String SPECIALIST_SERVICE_ID_REQUIRED = "reservation.specialistServiceId-required";
    public static final String RESERVATION_SOURCE_REQUIRED = "reservation.source-required";
    public static final String RESERVATION_CUSTOMER_USER_ID_NOT_MATCH = "reservation.customerUserId-not-match";
    public static final String RESERVATION_SPECIALIST_USER_ID_NOT_MATCH = "reservation.specialistUserId-not-match";
    public static final String RESERVATION_CONFLICT = "reservation.conflict";
    public static final String RESERVATION_NOT_FOUND = "reservation.not-found";
    public static final String RESERVATION_ID_REQUIRED = "reservation.id-required";
    //    --------------------------------------------------------
    //appointment
    public static final String APPOINTMENT_NOT_FOUND = "appointment.not-found";
    public static final String APPOINTMENT_NOT_AVAILABLE = "appointment.not-available";
    public static final String APPOINTMENT_DATE_REQUIRED = "appointment.date-required";
    //    --------------------------------------------------------

    //Block customer user
    public static final String CUSTOMER_ALREADY_BLOCKED = "customer.already-blocked";
    public static final String CUSTOMER_BLOCKED_BY_SPECIALIST = "customer.blocked-by-specialist";

    //Websocket
    public static final String WEBSOCKET_REQUEST_CANNOT_BE_NULL = "web-socket.request-can-not-be-null";
    public static final String WEBSOCKET_QUEUE_NAME_CANNOT_BE_NULL = "web-socket.queue-name-can-not-be-null";
    public static final String WEBSOCKET_PUBLISH_ERROR = "web-socket.publish-error";

    //Category
    public static final String CATEGORY_EXISTS_SAME_NAME = "category.exists-same-name";
    public static final String CATEGORY_NOT_FOUND = "category.not-found";
    public static final String CATEGORY_NOT_BELONG_TO_USER= "category.not-belong-to-user";
    public static final String CATEGORY_HAVE_PRODUCT= "category.have-product";
    public static final String CATEGORY_NAME_REQUIRED= "category.name-required";


    //file upload

    public static final String IMAGE_SAVE_EXCEPTION  = "file-upload.image-save-exception";
    public static final String GALLERY_IMAGE_NOT_FOUND = "gallery-image.not-found";

    //
    //Product
    public static final String PRODUCT_NOT_FOUND = "product.not-found";
    public static final String PRODUCT_EXISTS_SAME_NAME = "product.exists-same-name";

    //Notification
    public static final String ERROR_OCCURRED_WHILE_SEND_NOTIFICATION = "notification.error-occurred";


    //    --------------------------------------------------------
    //    //general
    public static final String ID_REQUIRED = "error.general.id-required";
    public static final String TIME_ZONE_REQUIRED = "error.general.timeZone.required";
    public static final String ENCRYPT_ERROR = "error.general.encrypt-error";
    public static final String DECRYPT_ERROR = "error.general.decrypt-error";
    public static final String RECORD_NOT_FOUND = "error.general.record-not-found";
}

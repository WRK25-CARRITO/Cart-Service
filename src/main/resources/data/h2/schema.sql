CREATE TABLE IF NOT EXISTS PAYMENT_METHODS(
    ID                  UUID                        ,
    NAME                VARCHAR(100)        NOT NULL,
    CHARGE              DOUBLE              NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS COUNTRY_TAXES(
    ID                  UUID                        ,
    COUNTRY             VARCHAR(100)        NOT NULL,
    CHARGE              DOUBLE              NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS CARTS(
    ID                  UUID                        ,
    ID_USER             UUID                NOT NULL,
    TOTAL_PRICE         DECIMAL             NOT NULL,
    COUNTRY_TAX_ID      UUID                        ,
    PAYMENT_METHOD_ID   UUID                        ,
    PROMOTIONS_ID       UUID                        ,
    CREATED_AT          TIMESTAMP                   ,
    UPDATED_AT          TIMESTAMP                   ,
    PRIMARY KEY (ID),
    FOREIGN KEY (COUNTRY_TAX_ID) REFERENCES COUNTRY_TAXES(ID),
    FOREIGN KEY (PAYMENT_METHOD_ID) REFERENCES PAYMENT_METHODS(ID)
);

CREATE TABLE IF NOT EXISTS CART_DETAILS(
    ID                  UUID                        ,
    CART_ID             UUID                NOT NULL,
    PRODUCT_ID          UUID                NOT NULL,
    QUANTITY            INTEGER             NOT NULL,
    TOTAL_WEIGHT        DOUBLE              NOT NULL,
    TOTAL_ITEM_PRICE    DOUBLE              NOT NULL,
    PAYMENT_METHOD_ID   UUID                        ,

    FOREIGN KEY (CART_ID) REFERENCES CARTS(ID),
    FOREIGN KEY (PAYMENT_METHOD_ID) REFERENCES PAYMENT_METHODS(ID)
);

CREATE TABLE IF NOT EXISTS PROMOTIONS(
    ID                  UUID                        ,
    CART_ID             UUID                NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (CART_ID) REFERENCES CARTS(ID)
);

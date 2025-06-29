USE CARRITOBD;

INSERT INTO PAYMENT_METHODS (ID, name, charge) VALUES
    (uuid_to_bin('9de13f9c-af57-4e86-9dd4-22dc2eba6934'), 'Credit Card', 0.25),
    (uuid_to_bin('41e05b0c-2340-4cdb-9d89-1524a8349f1c'), 'PayPal', 0.30),
    (uuid_to_bin('e2058881-416e-4d8b-9fa7-06b0a398844e'), 'Bank Transfer', 0.1);

INSERT INTO COUNTRY_TAXES (ID, country, TAX) VALUES
    (uuid_to_bin('026de96e-0744-4a57-ae88-c8bf3f1c1e62'), 'Spain', 0.21),
    (uuid_to_bin('6d948074-3749-4329-9cab-0c87e3c1a7c5'), 'Germany', 0.19),
    (uuid_to_bin('6d948074-3749-4329-9cab-0c87e3c1a7c4'), 'Belgium', 0.22),
    (uuid_to_bin('e95543c1-24a2-4d56-8daa-491d6fbf82b6'), 'France', 0.2);

INSERT INTO CARTS (ID, ID_USER, COUNTRY_TAX_ID, PAYMENT_METHOD_ID, updated_at, created_at, state) VALUES
    (uuid_to_bin('4d82b684-7131-4ba4-864d-465fc290708b'), uuid_to_bin('2f05a6f9-87dc-4ea5-a23c-b05265055334'),  null, null, NOW(), NOW(), 'ACTIVE'),
    (uuid_to_bin('bdbfb686-3fc4-4a3b-9f70-df76cdff0791'), uuid_to_bin('b96124a9-69a6-4859-acc7-5708ab07cd80'),  uuid_to_bin('6d948074-3749-4329-9cab-0c87e3c1a7c5'), uuid_to_bin('41e05b0c-2340-4cdb-9d89-1524a8349f1c'),  NOW(), NOW(), 'PENDING'),
    (uuid_to_bin('3d82b684-7131-4ba4-864d-465fc290708b'), uuid_to_bin('b96124a9-69a6-4859-acc7-5708ab07cd80'),  uuid_to_bin('6d948074-3749-4329-9cab-0c87e3c1a7c4'), uuid_to_bin('41e05b0c-2340-4cdb-9d89-1524a8349f1c'),  NOW(), NOW(), 'PENDING'),
    (uuid_to_bin('31f3f1fb-7d72-4116-a987-dc43f54c73ec'), uuid_to_bin('2f05a6f9-87dc-4ea5-a23c-b05265055334'),  null, null, NOW(), NOW(), 'ACTIVE');

INSERT INTO CART_DETAILS (cart_ID, Product_ID, quantity) VALUES
    (uuid_to_bin('4d82b684-7131-4ba4-864d-465fc290708b'),1, 2);

INSERT INTO CART_DETAILS (cart_ID, Product_ID, quantity) VALUES
    ( uuid_to_bin('bdbfb686-3fc4-4a3b-9f70-df76cdff0791'),3, 1);

INSERT INTO PROMOTIONS_CARTS (ID_PROMOTION, CART_ID) VALUES
    (0000000000004, uuid_to_bin('4d82b684-7131-4ba4-864d-465fc290708b')),
    (0000000000005, uuid_to_bin('bdbfb686-3fc4-4a3b-9f70-df76cdff0791'));

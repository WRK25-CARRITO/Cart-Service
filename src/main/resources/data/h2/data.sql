INSERT INTO PAYMENT_METHODS (ID, name, charge) VALUES
    ('9de13f9c-af57-4e86-9dd4-22dc2eba6934', 'Credit Card', 0.25),
    ('41e05b0c-2340-4cdb-9d89-1524a8349f1c', 'PayPal', 0.30),
    ('e2058881-416e-4d8b-9fa7-06b0a398844e', 'Bank Transfer', 0.1);

INSERT INTO COUNTRY_TAXES (ID, country, TAX) VALUES
    ('026de96e-0744-4a57-ae88-c8bf3f1c1e62', 'Spain', 0.21),
    ('6d948074-3749-4329-9cab-0c87e3c1a7c5', 'Germany', 0.19),
    ('6d948074-3749-4329-9cab-0c87e3c1a7c4', 'Belgium', 0.22),
    ('e95543c1-24a2-4d56-8daa-491d6fbf82b6', 'France', 0.2);

INSERT INTO CARTS (ID, ID_USER, total_price,total_weight, COUNTRY_TAX_ID, PAYMENT_METHOD_ID, updated_at, created_at, state) VALUES
    ('4d82b684-7131-4ba4-864d-465fc290708b', '2f05a6f9-87dc-4ea5-a23c-b05265055334', 150.75, 3.0, '026de96e-0744-4a57-ae88-c8bf3f1c1e62', '9de13f9c-af57-4e86-9dd4-22dc2eba6934', NOW(), NOW(), 'CLOSED'),
    ('bdbfb686-3fc4-4a3b-9f70-df76cdff0791', 'b96124a9-69a6-4859-acc7-5708ab07cd80', 89.99, 3.0, '6d948074-3749-4329-9cab-0c87e3c1a7c5', '41e05b0c-2340-4cdb-9d89-1524a8349f1c',  NOW(), NOW(), 'PENDING'),
    ('3d82b684-7131-4ba4-864d-465fc290708b', 'b96124a9-69a6-4859-acc7-5708ab07cd80', 45.99, 4.0, '6d948074-3749-4329-9cab-0c87e3c1a7c4', '41e05b0c-2340-4cdb-9d89-1524a8349f1c',  NOW(), NOW(), 'PENDING'),
    ('31f3f1fb-7d72-4116-a987-dc43f54c73ec', '2f05a6f9-87dc-4ea5-a23c-b05265055334', 150.75, 3.0, null, null, NOW(), NOW(), 'ACTIVE');


INSERT INTO CART_DETAILS (cart_ID, Product_ID, quantity, total_weight, total_item_price) VALUES
    ('4d82b684-7131-4ba4-864d-465fc290708b',0000000000001, 2, 1.5, 100.50);

INSERT INTO CART_DETAILS (cart_ID, Product_ID, quantity, total_weight, total_item_price) VALUES
    ( 'bdbfb686-3fc4-4a3b-9f70-df76cdff0791',0000000000003, 1, 0.8, 89.99);

INSERT INTO PROMOTIONS_CARTS (ID_PROMOTION, CART_ID) VALUES
    (0000000000004, '4d82b684-7131-4ba4-864d-465fc290708b'),
    (0000000000005, 'bdbfb686-3fc4-4a3b-9f70-df76cdff0791');

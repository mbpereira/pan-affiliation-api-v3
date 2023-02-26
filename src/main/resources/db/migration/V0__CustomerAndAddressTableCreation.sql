CREATE TABLE customers (
    id UUID NOT NULL PRIMARY KEY,
    document_number VARCHAR(14) NOT NULL,
    name VARCHAR(300) NOT NULL
);

CREATE TABLE addresses (
    id UUID NOT NULL PRIMARY KEY,
    customer_id UUID NOT NULL,
    postal_code VARCHAR(8) NOT NULL,
    street VARCHAR(300) NOT NULL,
    number INT NULL,
    state VARCHAR(2) NOT NULL,
    country VARCHAR(50) NOT NULL,
    complement VARCHAR(100) NULL,
    neighborhood VARCHAR(150) NOT NULL,
    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customers(id)
);
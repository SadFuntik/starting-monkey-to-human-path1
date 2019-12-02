CREATE TABLE IF NOT EXISTS officiants
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    first_name  VARCHAR(50) UNIQUE,
    second_name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS orders
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    date         Date,
    officiant_id INTEGER NOT NULL FOREIGN KEY REFERENCES officiants (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    name        VARCHAR(50) UNIQUE,
    description varchar(255) NOT NULL,
    cost        numeric(10, 3)
);

CREATE TABLE IF NOT EXISTS items_order
(
    id                  INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    orders_id           INTEGER FOREIGN KEY REFERENCES orders (id),
    items_dictionary_id INTEGER FOREIGN KEY REFERENCES items (id),
    quantity            INTEGER NOT NULL
);
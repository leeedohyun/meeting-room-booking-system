CREATE TABLE meeting_room
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    capacity    INT          NOT NULL,
    hourly_rate INT          NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);

CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);

CREATE TABLE payment
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_type       ENUM('CARD', 'SIMPLE', 'VIRTUAL_ACCOUNT') NOT NULL,
    amount              INT          NOT NULL,
    status              ENUM('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED') NOT NULL,
    external_payment_id VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);

CREATE TABLE reservation
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT    NOT NULL,
    meeting_room_id BIGINT    NOT NULL,
    start_time      DATETIME  NOT NULL,
    end_time        DATETIME  NOT NULL,
    total_amount    INT       NOT NULL,
    status          enum('PAYMENT_PENDING', 'CONFIRMED', 'CANCELED') NOT NULL,
    payment_id      BIGINT,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (meeting_room_id) REFERENCES meeting_room (id)
);

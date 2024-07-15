CREATE TABLE IF NOT EXISTS tbeventroom (
    eventroomid uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    roomname VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    isfull BOOLEAN NOT NULL DEFAULT TRUE,
    dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT
);
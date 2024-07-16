CREATE TABLE IF NOT EXISTS tbperson (
    personid uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    isactive BOOLEAN NOT NULL DEFAULT TRUE,
    eventroomid uuid REFERENCES tbeventroom(eventroomid),
    cofeespaceid uuid REFERENCES tbcofeespace(cofeespaceid),
    dthreg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dthalt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT,

    CONSTRAINT fk_eventroomid FOREIGN KEY (eventroomid) REFERENCES tbeventroom(eventroomid),
    CONSTRAINT fk_cofeespaceid FOREIGN KEY (cofeespaceid) REFERENCES tbcofeespace(cofeespaceid)
);
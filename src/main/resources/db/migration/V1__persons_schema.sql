
CREATE TABLE public.persons(
    id                      uuid NOT NULL,
    names                   VARCHAR NOT NULL,
    last_names              VARCHAR NOT NULL,
    curp                    VARCHAR NOT NULL UNIQUE,
    rfc                     VARCHAR NOT NULL UNIQUE,
    email                   VARCHAR NOT NULL UNIQUE,
    birth_date              BIGINT NOT NULL,
    cell_phone              VARCHAR NOT NULL,
    address                 JSONB,
    employment_information  JSONB,
    bank_information        JSONB,
    status                  VARCHAR NOT NULL,
    created_at              BIGINT  NOT NULL,
    CONSTRAINT persons_pkey PRIMARY KEY (id)
);

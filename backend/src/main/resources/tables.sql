create type result as enum ('white','black','draw');

create table users (
                       uuid UUID PRIMARY KEY,
                       name VARCHAR UNIQUE NOT NULL,
                       email VARCHAR UNIQUE NOT NULL,
                       password VARCHAR NOT NULL
);

create table results
(
    uuid        uuid primary key,
    game_id     uuid unique not null,
    result      result      not null,
    finish_time timestamp   not null,
    constraint game_fkey foreign key (game_id)
        references games (uuid)
);

create table game_history
(
    uuid    uuid primary key,
    game_id uuid unique not null,
    history jsonb[],
    constraint game_fkey foreign key (game_id)
        references games (uuid)
);

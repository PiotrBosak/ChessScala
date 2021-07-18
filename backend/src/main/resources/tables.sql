create table users
(
    uuid     uuid primary key,
    username varchar unique not null,
    password varchar        not null check ( length(password) > 3 ),
    score    int            not null
);

create table games
(
    uuid          uuid primary key,
    white_id      uuid      not null,
    black_id      uuid      not null,
    starting_time timestamp not null,
    constraint white_fkey foreign key (white_id)
        references users (uuid),
    constraint black_fkey foreign key (black_id)
        references users (uuid)
);

create type result as enum ('white','black','draw');

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

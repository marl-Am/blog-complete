# Users
CREATE TABLE IF NOT EXISTS public.user
(
    id       SERIAL PRIMARY KEY,
    username TEXT UNIQUE,
    email    TEXT UNIQUE,
    password TEXT,
    role     TEXT
);

CREATE TABLE IF NOT EXISTS public.post
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES public.user (id)
);

CREATE TABLE IF NOT EXISTS public.authority
(
    name TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS public.user_authority
(
    user_id        INTEGER REFERENCES public.user (id),
    authority_name TEXT REFERENCES public.authority (name)
);

# Authority
CREATE TABLE IF NOT EXISTS public.authority
(
    name TEXT PRIMARY KEY
);
#

# Posts
CREATE TABLE IF NOT EXISTS public.post
(
    id        SERIAL PRIMARY KEY,
    title     TEXT UNIQUE                         NOT NULL,
    content   TEXT                                NOT NULL,
    createdOn TEXT                                NOT NULL,
    updatedOn TEXT                                NOT NULL,
    user_id   INTEGER REFERENCES public.user (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.post_tags
(
    post_id INTEGER REFERENCES public.post (id),
    tag     TEXT
);
#
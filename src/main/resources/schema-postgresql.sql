CREATE TABLE IF NOT EXISTS usuario(
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(320) NOT NULL,
    senha VARCHAR(16) NOT NULL
);

CREATE TABLE IF NOT EXISTS musica(
    id UUID PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    album VARCHAR(200),
    artista VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS playlist(
    id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL,
    nome VARCHAR(200) NOT NULL,
    CONSTRAINT fk_playlist_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS playlist_musica(
    playlist_id UUID NOT NULL,
    musica_id UUID NOT NULL,
    PRIMARY KEY (playlist_id, musica_id),
    CONSTRAINT fk_playlist_musica_playlist FOREIGN KEY (playlist_id) REFERENCES playlist(id) ON DELETE CASCADE,
    CONSTRAINT fk_playlist_musica_musica FOREIGN KEY (musica_id) REFERENCES musica(id) ON DELETE CASCADE
);
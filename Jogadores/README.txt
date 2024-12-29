Utilizei a base de dados MySql no Xampp.
Criar base de dados players.
Criar tabela players.
CREATE TABLE players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    idade INT NOT NULL,
    clube_atual VARCHAR(255) NOT NULL,
    formacao VARCHAR(255) NOT NULL,
    melhor_pe VARCHAR(10) NOT NULL
);
A minha aplicação não tem front-end para testar utilizar o postman e aqui estão as routes para fazer o Crud na base de dados 
route("/players") {
	post
        get("/") 
        get("/{id}") 
        put("/{id}") 
        delete("/{id}") 
        get("/club/{clubeAtual}") 
        get("/foot/{melhorPe}") 
        get("/position/{formacao}")
}
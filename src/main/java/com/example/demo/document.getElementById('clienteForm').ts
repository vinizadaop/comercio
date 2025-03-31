document.addEventListener('DOMContentLoaded', function () {
    // Variáveis para os formulários
    const formCliente = document.getElementById('formCliente');
    const formContato = document.getElementById('formContato');

    const clientesTable = document.getElementById('clientesBody');
    const contatosTable = document.getElementById('contatosBody');

    // Função para cadastrar cliente
    formCliente.addEventListener('submit', function (event) {
        event.preventDefault();

        const cliente = {
            nome: document.getElementById('nomeCliente').value,
            cpf: document.getElementById('cpfCliente').value,
            dataNascimento: document.getElementById('dataNascimentoCliente').value,
            endereco: document.getElementById('enderecoCliente').value
        };

        fetch('/clientes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cliente)
        })
        .then(response => response.json())
        .then(data => {
            alert('Cliente cadastrado com sucesso!');
            carregarClientes(); // Atualiza a lista de clientes
            formCliente.reset(); // Limpa os campos do formulário
        })
        .catch(error => {
            console.error('Erro ao cadastrar cliente:', error);
        });
    });

    // Função para cadastrar contato
    formContato.addEventListener('submit', function (event) {
        event.preventDefault();

        const contato = {
            tipoContato: document.getElementById('tipoContato').value,
            valorContato: document.getElementById('valorContato').value,
            observacao: document.getElementById('observacaoContato').value,
            clienteId: 1 // Você pode adaptar para pegar o cliente corretamente
        };

        fetch('/contatos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contato)
        })
        .then(response => response.json())
        .then(data => {
            alert('Contato cadastrado com sucesso!');
            carregarContatos(); // Atualiza a lista de contatos
            formContato.reset(); // Limpa os campos do formulário
        })
        .catch(error => {
            console.error('Erro ao cadastrar contato:', error);
        });
    });

    // Função para carregar clientes na tabela
    function carregarClientes() {
        fetch('/clientes')
            .then(response => response.json())
            .then(data => {
                clientesTable.innerHTML = '';
                data.forEach(cliente => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${cliente.nome}</td>
                        <td>${cliente.cpf}</td>
                        <td>${cliente.dataNascimento}</td>
                        <td>${cliente.endereco}</td>
                        <td><button onclick="excluirCliente(${cliente.id})">Excluir</button></td>
                    `;
                    clientesTable.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Erro ao carregar clientes:', error);
            });
    }

    // Função para excluir cliente
    function excluirCliente(id) {
        fetch(`/clientes/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            alert('Cliente excluído com sucesso!');
            carregarClientes(); // Atualiza a lista
        })
        .catch(error => {
            console.error('Erro ao excluir cliente:', error);
        });
    }

    // Função para carregar contatos na tabela
    function carregarContatos() {
        fetch('/contatos')
            .then(response => response.json())
            .then(data => {
                contatosTable.innerHTML = '';
                data.forEach(contato => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${contato.tipoContato}</td>
                        <td>${contato.valorContato}</td>
                        <td>${contato.observacao}</td>
                        <td><button onclick="excluirContato(${contato.id})">Excluir</button></td>
                    `;
                    contatosTable.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Erro ao carregar contatos:', error);
            });
    }

    // Carrega os dados iniciais
    carregarClientes();
    carregarContatos();
});

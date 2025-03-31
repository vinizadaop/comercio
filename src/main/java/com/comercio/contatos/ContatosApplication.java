package com.comercio.contatos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class ContatosApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContatosApplication.class, args);
    }
}

@Entity
@Table(name = "clientes")
class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;
    
    @Column(name = "data_nascimento")
    private String dataNascimento;
    
    @Column(length = 255)
    private String endereco;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos;

    public void SetId(Long id2) {
    
        throw new UnsupportedOperationException("Unimplemented method 'SetId'");
    }
}

@Entity
@Table(name = "contatos")
class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(nullable = false, length = 50)
    private String tipo;
    
    @Column(nullable = false, length = 100)
    private String valor;
    
    @Column(length = 255)
    private String observacao;
}

@Repository
interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    Cliente findByCpf(String cpf);
}

@Repository
interface ContatoRepository extends JpaRepository<Contato, Long> {
    List<Contato> findByClienteId(Long clienteId);
}

@Service
@Transactional
class ClienteService {
    private final ClienteRepository clienteRepository;
    
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Cliente atualizarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public void excluirCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}

@Service
@Transactional
class ContatoService {
    private final ContatoRepository contatoRepository;
    
    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }
    
    public Contato salvarContato(Contato contato) {
        return contatoRepository.save(contato);
    }
    
    public List<Contato> listarContatosPorCliente(Long clienteId) {
        return contatoRepository.findByClienteId(clienteId);
    }
    
    public void excluirContato(Long id) {
        contatoRepository.deleteById(id);
    }
}

@RestController
@RequestMapping("/clientes")
class ClienteController {
    private final ClienteService clienteService;
    
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente) {
        return clienteService.salvarCliente(cliente);
    }
    
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }
    
    @GetMapping("/{id}")
    public Optional<Cliente> buscarCliente(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }
    
    @PutMapping("/{id}")
    public Cliente atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.SetId(id);
        return clienteService.atualizarCliente(cliente);
    }
    
    @DeleteMapping("/{id}")
    public void excluirCliente(@PathVariable Long id) {
        clienteService.excluirCliente(id);
    }
}

@RestController
@RequestMapping("/contatos")
class ContatoController {
    private final ContatoService contatoService;
    
    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }
    
    @PostMapping
    public Contato criarContato(@RequestBody Contato contato) {
        return contatoService.salvarContato(contato);
    }
    
    @GetMapping("/cliente/{clienteId}")
    public List<Contato> listarContatos(@PathVariable Long clienteId) {
        return contatoService.listarContatosPorCliente(clienteId);
    }
    
    @DeleteMapping("/{id}")
    public void excluirContato(@PathVariable Long id) {
        contatoService.excluirContato(id);
    }
}

package com.petshop.api.config;

import com.petshop.api.model.*;
import com.petshop.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            log.info("Dados já existentes, seed ignorado.");
            return;
        }

        log.info("Populando banco de dados...");

        Category racoes       = categoryRepository.save(Category.builder().name("Rações").description("Alimentos para cães e gatos").build());
        Category brinquedos   = categoryRepository.save(Category.builder().name("Brinquedos").description("Entretenimento e estímulo para pets").build());
        Category higiene      = categoryRepository.save(Category.builder().name("Higiene").description("Produtos de banho e tosa").build());
        Category medicamentos = categoryRepository.save(Category.builder().name("Medicamentos").description("Vermífugos, antipulgas e suplementos").build());
        Category acessorios   = categoryRepository.save(Category.builder().name("Acessórios").description("Coleiras, camas e casinhas").build());

        Product p1 = saveProduct("Ração Premium Golden Adulto 15kg", "Ração super premium para cães adultos de médio e grande porte.", new BigDecimal("149.90"), 50, racoes);
        Product p4 = saveProduct("Bola de Borracha Kong Classic M",  "Brinquedo resistente ideal para cães de médio porte.",            new BigDecimal("49.90"),  25, brinquedos);

        saveProduct("Ração Royal Canin Gato Adulto 4kg",           "Nutrição específica para gatos adultos castrados.",               new BigDecimal("89.90"),  35, racoes);
        saveProduct("Ração Pedigree Filhote 10kg",                 "Fórmula especial para o desenvolvimento de filhotes.",            new BigDecimal("79.90"),  40, racoes);
        saveProduct("Corda de Sisal para Gatos",                   "Arranhador e brinquedo em corda de sisal natural.",              new BigDecimal("29.90"),  30, brinquedos);
        saveProduct("Shampoo Sanol Dog Neutro 500ml",              "Shampoo neutro para uso frequente em cães.",                     new BigDecimal("19.90"),  60, higiene);
        saveProduct("Escova de Dentes Pet com Pasta",              "Kit higiene dental com escova e pasta sabor frango.",            new BigDecimal("24.90"),  45, higiene);
        saveProduct("Antipulgas Frontline Plus para Cães 10-20kg", "Proteção contra pulgas e carrapatos por 30 dias.",              new BigDecimal("54.90"),  20, medicamentos);
        saveProduct("Coleira Antipulga Seresto Cão M",             "Proteção por até 8 meses contra pulgas e carrapatos.",          new BigDecimal("199.90"), 15, acessorios);
        saveProduct("Cama Pet Confort Tamanho M",                  "Cama macia e lavável para cães de médio porte.",                new BigDecimal("119.90"), 20, acessorios);

        Address addr1 = Address.builder().street("Rua das Flores").number("123").neighborhood("Centro").city("São Paulo").state("SP").zipCode("01001-000").build();
        Customer c1 = customerRepository.save(Customer.builder().name("João da Silva").email("joao.silva@email.com").cpf("123.456.789-00").phone("(11) 99999-1111").address(addr1).build());

        Address addr2 = Address.builder().street("Av. Paulista").number("1000").neighborhood("Bela Vista").city("São Paulo").state("SP").zipCode("01310-100").build();
        customerRepository.save(Customer.builder().name("Maria Oliveira").email("maria.oliveira@email.com").cpf("987.654.321-00").phone("(11) 98888-2222").address(addr2).build());

        userRepository.save(User.builder()
                .name("Admin")
                .email("admin@petshop.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .build());

        OrderItem item1 = OrderItem.builder().product(p1).quantity(2).unitPrice(p1.getPrice()).build();
        OrderItem item2 = OrderItem.builder().product(p4).quantity(1).unitPrice(p4.getPrice()).build();

        BigDecimal total = p1.getPrice().multiply(BigDecimal.valueOf(2)).add(p4.getPrice());

        Order order = Order.builder()
                .customer(c1)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.CONFIRMED)
                .totalAmount(total)
                .items(List.of(item1, item2))
                .build();

        item1.setOrder(order);
        item2.setOrder(order);

        p1.setStock(p1.getStock() - 2);
        p4.setStock(p4.getStock() - 1);
        productRepository.saveAll(List.of(p1, p4));
        orderRepository.save(order);

        log.info("Seed concluído.");
    }

    private Product saveProduct(String name, String desc, BigDecimal price, int stock, Category cat) {
        return productRepository.save(Product.builder()
                .name(name).description(desc).price(price).stock(stock).category(cat).build());
    }
}

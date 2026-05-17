# Integração do InputSanitizer nos Controllers

## Visão Geral

Este documento documenta como integrar o `InputSanitizer` nos Controllers do Hermes Comercial para prevenir ataques de injeção e XSS.

## Padrão de Integração

### 1. Importar InputSanitizer

```java
import com.br.hermescomercial.util.InputSanitizer;
```

### 2. Sanitizar Entrada do Usuário

Antes de processar dados do usuário, sanitize a entrada:

```java
// Exemplo: Sanitizar nome de cliente
String nomeRaw = txtNome.getText();
String nomeSanitizado = InputSanitizer.sanitizeName(nomeRaw);

// Exemplo: Sanitizar email
String emailRaw = txtEmail.getText();
String emailSanitizado = InputSanitizer.sanitizeEmail(emailRaw);

// Exemplo: Sanitizar telefone
String telefoneRaw = txtTelefone.getText();
String telefoneSanitizado = InputSanitizer.sanitizePhone(telefoneRaw);

// Exemplo: Sanitizar CPF/CNPJ
String cpfRaw = txtCPF.getText();
String cpfSanitizado = InputSanitizer.sanitizeCPF_CNPJ(cpfRaw);
```

### 3. Verificar Padrões Suspeitos

Opcionalmente, verifique se a entrada contém padrões suspeitos:

```java
if (InputSanitizer.containsSuspiciousPatterns(nomeRaw)) {
    JOptionPane.showMessageDialog(this, 
        "Entrada contém padrões suspeitos!", 
        "Erro de Segurança", 
        JOptionPane.ERROR_MESSAGE);
    return;
}
```

## Exemplo de Integração Completa

### PDVFormularioGestaoCliente

```java
private void salvarCliente() {
    // Obter dados brutos
    String nomeRaw = txtNome.getText();
    String cpfRaw = txtCPF.getText();
    String telefoneRaw = txtTelefone.getText();
    String emailRaw = txtEmail.getText();
    
    // Sanitizar dados
    String nome = InputSanitizer.sanitizeName(nomeRaw);
    String cpf = InputSanitizer.sanitizeCPF_CNPJ(cpfRaw);
    String telefone = InputSanitizer.sanitizePhone(telefoneRaw);
    String email = InputSanitizer.sanitizeEmail(emailRaw);
    
    // Verificar padrões suspeitos
    if (InputSanitizer.containsSuspiciousPatterns(nomeRaw) ||
        InputSanitizer.containsSuspiciousPatterns(emailRaw)) {
        JOptionPane.showMessageDialog(this, 
            "Entrada contém padrões suspeitos!", 
            "Erro de Segurança", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validar dados
    if (!FieldValidator.validateNomeCliente(txtNome) ||
        !FieldValidator.validateCpfCnpj(txtCPF) ||
        !FieldValidator.validateTelefone(txtTelefone) ||
        !FieldValidator.validateEmail(txtEmail)) {
        return;
    }
    
    // Criar cliente com dados sanitizados
    Cliente cliente = new Cliente();
    cliente.setNome(nome);
    cliente.setCpf(cpf);
    cliente.setTelefone(telefone);
    cliente.setEmail(email);
    
    // Salvar cliente
    try {
        ClienteService.getInstance().salvar(cliente);
        JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + e.getMessage());
    }
}
```

## Controllers Recomendados para Integração

### Alta Prioridade (dados sensíveis)

1. **PDVFormularioGestaoCliente** - Dados de cliente (nome, CPF, email, telefone)
2. **PDVFormularioGestaoProduto** - Dados de produto (nome, código, descrição)
3. **PDVFormularioGestaoUsuario** - Dados de usuário (nome, login)
4. **PDVFormularioFornecedor** - Dados de fornecedor (nome, CNPJ, email)
5. **PDVFormularioLoja** - Dados da loja (nome, endereço)

### Média Prioridade (buscas e filtros)

6. **PDVFormularioBuscaAvancada** - Termos de busca
7. **PDVFormularioConsultarProduto** - Filtros de consulta
8. **PDVFormularioConsultarVendas** - Filtros de consulta
9. **PDVFormularioHistoricoBuscas** - Termos de busca

### Baixa Prioridade (dados internos)

10. **PDVFormularioConfiguracoes** - Configurações do sistema
11. **PDVFormularioParametros** - Parâmetros do sistema

## Mapeamento de Campos

### Cliente

| Campo | Método InputSanitizer |
|-------|----------------------|
| Nome | `sanitizeName()` |
| CPF/CNPJ | `sanitizeCPF_CNPJ()` |
| Telefone | `sanitizePhone()` |
| Email | `sanitizeEmail()` |
| Endereço | `sanitizeName()` |
| Cidade | `sanitizeName()` |
| Estado | `sanitizeName()` |

### Produto

| Campo | Método InputSanitizer |
|-------|----------------------|
| Nome | `sanitizeName()` |
| Código | `sanitizeProductCode()` |
| Descrição | `sanitize()` |
| Categoria | `sanitizeName()` |

### Usuário

| Campo | Método InputSanitizer |
|-------|----------------------|
| Nome | `sanitizeName()` |
| Login | `sanitizeName()` |
| Email | `sanitizeEmail()` |

### Fornecedor

| Campo | Método InputSanitizer |
|-------|----------------------|
| Nome | `sanitizeName()` |
| CNPJ | `sanitizeCPF_CNPJ()` |
| Telefone | `sanitizePhone()` |
| Email | `sanitizeEmail()` |

## Boas Práticas

1. **Sempre sanitize antes de validar** - Sanitize primeiro, depois valide
2. **Verifique padrões suspeitos** - Adicione verificação em campos críticos
3. **Use métodos específicos** - Use `sanitizeName()` para nomes, `sanitizeEmail()` para emails, etc.
4. **Mantenha o original para validação** - Valide o input original, use o sanitizado para salvar
5. **Log tentativas suspeitas** - Log quando padrões suspeitos são detectados

## Exemplo de Logging

```java
if (InputSanitizer.containsSuspiciousPatterns(nomeRaw)) {
    logger.warn("Padrão suspeito detectado em nome de cliente: {}", nomeRaw);
    JOptionPane.showMessageDialog(this, 
        "Entrada contém padrões suspeitos!", 
        "Erro de Segurança", 
        JOptionPane.ERROR_MESSAGE);
    return;
}
```

## Testes

Após integrar InputSanitizer, teste:

1. **Teste com entrada normal** - Verifique que dados normais são processados corretamente
2. **Teste com SQL injection** - Verifique que tentativas de SQL injection são bloqueadas
3. **Teste com XSS** - Verifique que tentativas de XSS são bloqueadas
4. **Teste com caracteres especiais** - Verifique que caracteres especiais são removidos
5. **Teste com entrada muito longa** - Verifique que entrada longa é truncada

## Implementação Gradual

Recomenda-se implementar gradualmente:

1. **Fase 1:** Controllers de alta prioridade (cliente, produto, usuário)
2. **Fase 2:** Controllers de média prioridade (buscas, filtros)
3. **Fase 3:** Controllers de baixa prioridade (configurações)

## Suporte

Para dúvidas sobre integração, consulte:
- `InputSanitizer.java` - Documentação dos métodos
- `InputSanitizerTest.java` - Exemplos de uso
- Guia de Desenvolvimento (`docs/DESENVOLVIMENTO.md`)

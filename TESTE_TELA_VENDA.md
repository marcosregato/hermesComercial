# TESTE COMPLETO DAS FUNÇÕES DA TELA DE VENDA

## 📋 **GUIA DE TESTE**

### ✅ **1. CAMPOS DE ENTRADA**
- [ ] **CPF/CNPJ**: Digitar números apenas (ex: 12345678901)
- [ ] **Valor Recebido**: Digitar números com vírgula decimal (ex: 100,50)
- [ ] **Busca de Produtos**: Digitar código, descrição ou código de barras
- [ ] **Código de Barras**: Testar funcionalidade (apenas mensagem informativa)

### ✅ **2. BOTÕES DE AÇÃO**
- [ ] **Botão Adicionar (tabela produtos)**: Clicar para adicionar produto ao carrinho
- [ ] **Botão Buscar**: Buscar produtos por texto
- [ ] **Botão Cód. Barras**: Testar mensagem de desenvolvimento
- [ ] **Seleção na tabela**: Clicar na linha para selecionar produto

### ✅ **3. CARRINHO DE COMPRAS**
- [ ] **Adicionar produtos**: Verificar se produtos aparecem no carrinho
- [ ] **Incrementar quantidade**: Clicar novamente no mesmo produto para aumentar quantidade
- [ ] **Remover item**: Usar botão "Cancelar Item" ou selecionar e remover
- [ ] **Atualizar totais**: Verificar se subtotal, desconto e total são calculados corretamente

### ✅ **4. CLIENTE**
- [ ] **Buscar cliente**: Digitar CPF/CNPJ e clicar em "Buscar"
- [ ] **Limpar cliente**: Clicar em "Limpar Cliente"
- [ ] **Dados do cliente**: Verificar se informações aparecem corretamente

### ✅ **5. PAGAMENTO**
- [ ] **Formas de pagamento**: Selecionar Dinheiro, Cartão, PIX ou Outro
- [ ] **Valor recebido**: Digitar valor e calcular troco automaticamente
- [ ] **Calcular troco**: Verificar se troco é calculado corretamente
- [ ] **Mudar forma**: Trocar entre formas de pagamento

### ✅ **6. FINALIZAÇÃO**
- [ ] **Botão Finalizar**: Clicar para finalizar venda
- [ ] **Validações**: Verificar se carrinho não está vazio
- [ ] **Mensagens de erro**: Testar todas as validações
- [ ] **Botão Cancelar**: Cancelar venda e limpar tudo

### ✅ **7. INTERFACE E USABILIDADE**
- [ ] **Layout responsivo**: Redimensionar janela para testar responsividade
- [ ] **Navegação**: Testar todos os campos e botões
- [ ] **Performance**: Verificar se a tela responde rapidamente
- [ ] **Mensagens de status**: Verificar se informações de status aparecem

### ✅ **8. CENÁRIOS DE TESTE**
#### **Cenário 1: Venda Simples**
1. Abrir tela de venda
2. Buscar produto por código
3. Adicionar ao carrinho
4. Digitar valor recebido
5. Finalizar venda

#### **Cenário 2: Venda com Múltiplos Itens**
1. Adicionar vários produtos diferentes
2. Verificar totais
3. Aplicar desconto
4. Finalizar venda

#### **Cenário 3: Venda com Cliente**
1. Buscar cliente por CPF/CNPJ
2. Adicionar produtos
3. Finalizar venda com cliente vinculado

#### **Cenário 4: Validações**
1. Tentar finalizar carrinho vazio
2. Digitar texto em campos numéricos
3. Usar caracteres inválidos nos campos

#### **Cenário 5: Teste de Stress**
1. Adicionar e remover muitos itens rapidamente
2. Trocar entre formas de pagamento
3. Testar performance da interface

### ✅ **9. RESULTADOS ESPERADOS**
- [ ] **Campos numéricos**: Apenas números permitidos
- [ ] **Botão Adicionar**: Funcionando corretamente
- [ ] **Cálculos**: Totais e troco corretos
- [ ] **Validações**: Mensagens claras e úteis
- [ ] **Interface**: Responsiva e intuitiva
- [ ] **Performance**: Rápida e fluida
- [ ] **Sem erros**: Sem exceções não tratadas

---

## 🔧 **INSTRUÇÕES DE TESTE**

### **Passo a Passo:**
1. **Abra a aplicação** Hermes PDV
2. **Faça login** como operador
3. **Abra o caixa** se necessário
4. **Clique em "Abrir Venda"**
5. **Execute cada item** da lista acima
6. **Marque como concluído** cada teste bem-sucedido
7. **Anote qualquer problema** encontrado
8. **Teste todos os cenários** antes de concluir

### **Critérios de Sucesso:**
- ✅ Todos os campos funcionam
- ✅ Validações ativas
- ✅ Cálculos corretos
- ✅ Interface responsiva
- ✅ Sem erros de sistema

---

**Teste completo!** 🎯

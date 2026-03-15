# hermesComercial
Configure Run VM Options: To run the application, you need to specify the module path and add the required modules in your run configuration:
* Go to Run | Edit Configurations.
* In the VM options field, add the following (adjust the path to your SDK):
bash
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base


o que um sistema comercial precisa ter para um loja comercial e salvar o resultado em uma arquivo pdf para fazer o download



Cadastro de Produtos

Nome, código, descrição, preço, quantidade em estoque, categoria.
Cadastro de Clientes

Nome, CPF/CNPJ, telefone, endereço, histórico de compras.
Registro de Vendas

Seleção de produtos, quantidade, preço, forma de pagamento, data e hora.
Controle de Estoque

Atualização automática do estoque conforme vendas e entradas.
Relatórios

Relatórios de vendas diárias, mensais, por produto, por cliente, etc.
Geração de Documentos em PDF

Recibos, notas fiscais, relatórios de vendas, extratos de cliente.
Interface para Download de Arquivos PDF

Botão ou link para salvar localmente os arquivos gerados.
Segurança e Backup

Controle de acesso, backups automáticos.
Para salvar o resultado em PDF e permitir o download, o sistema deve contar com uma biblioteca ou ferramenta que converta os dados em PDF. Exemplos comuns:

Para sistemas web: bibliotecas como jsPDF, TCPDF, FPDF, dompdf.
Para aplicações desktop: ferramentas específicas da linguagem usada (ex: iText para Java, ReportLab para Python).
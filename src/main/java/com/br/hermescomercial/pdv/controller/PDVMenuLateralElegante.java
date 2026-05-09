package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import java.awt.*;

/**
 * Menu Lateral Elegante - Versão Refatorada
 * Esta classe foi refatorada para usar classes especializadas:
 * - PDVMenuRenderer: Responsável pela renderização de UI
 * - PDVMenuDataManager: Responsável pela gestão de dados
 * - PDVFormularioFactory: Responsável pela criação de formulários
 */
public class PDVMenuLateralElegante {
    
    private JPanel workArea;
    private PDVMenuRenderer renderer;
    private PDVMenuDataManager dataManager;
    
    public PDVMenuLateralElegante(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.dataManager = new PDVMenuDataManager(usuarioAtual, nomeUsuario);
        this.renderer = new PDVMenuRenderer();
    }
    
    /**
     * Cria o menu lateral elegante
     */
    public JPanel criarMenuLateralElegante() {
        try {
            // Criar header usando o renderer
            JPanel headerPanel = renderer.criarHeaderMenu(dataManager.getNomeUsuario());
            
            // Criar painel de menus
            JPanel menusPanel = new JPanel();
            menusPanel.setLayout(new BoxLayout(menusPanel, BoxLayout.Y_AXIS));
            menusPanel.setBackground(Color.WHITE);
            
            // Adicionar menus principais com submenus usando o renderer
            String[] menusPrincipais = dataManager.getMenusPrincipais();
            for (String menu : menusPrincipais) {
                String[] subMenus = dataManager.getSubMenus(menu);
                renderer.criarMenuComSubmenus(menusPanel, menu, subMenus, 
                    new PDVMenuRenderer.MenuClickListener() {
                        @Override
                        public void onSubMenuClick(String subMenuText) {
                            handleSubMenuClick(subMenuText);
                        }
                    });
            }
            
            // Criar menu lateral usando o renderer
            JPanel menuPanel = renderer.criarMenuLateralPanel(headerPanel, menusPanel);
            
            SystemLogger.ui("Menu lateral elegante criado com sucesso");
            return menuPanel;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar menu lateral elegante", e);
            return renderer.criarMenuBasico();
        }
    }
    
    /**
     * Manipula cliques nos submenus
     */
    private void handleSubMenuClick(String subMenuText) {
        try {
            SystemLogger.ui("=== ABRINDO FORMULÁRIO ===");
            SystemLogger.ui("Item: " + subMenuText);
            SystemLogger.ui("Usuário: " + dataManager.getUsuarioAtual());
            SystemLogger.ui("WorkArea: " + (workArea != null ? "OK" : "NULL"));
            
            // Identificar módulo usando o dataManager
            String module = dataManager.identificarModulo(subMenuText);
            SystemLogger.ui("Módulo identificado: " + module);
            
            // Limpar workArea
            workArea.removeAll();
            
            // Garantir layout do workArea
            if (workArea.getLayout() == null) {
                workArea.setLayout(new BorderLayout());
            }
            
            JPanel formularioPanel = null;
            
            // Verificar se é um formulário especializado
            if ("� Dashboard Analytics".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Dashboard Analytics");
                formularioPanel = PDVFormularioDashboard.criarFormularioDashboard();
            } else if ("� Nova Venda".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Nova Venda");
                PDVFormularioNovaVenda formularioNovaVenda = new PDVFormularioNovaVenda(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioNovaVenda.criarFormularioNovaVenda();
            } else if ("🔍 Consultar Vendas".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Consultar Vendas");
                PDVFormularioConsultarVendas formularioConsultarVendas = new PDVFormularioConsultarVendas(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioConsultarVendas.criarFormularioConsultarVendas();
            } else if ("↩️ Devoluções".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Devoluções");
                PDVFormularioDevolucoes formularioDevolucoes = new PDVFormularioDevolucoes(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioDevolucoes.criarFormularioDevolucoes();
            } else if ("📊 Resumo Diário".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Resumo Diário");
                PDVFormularioResumoDiario formularioResumoDiario = new PDVFormularioResumoDiario(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioResumoDiario.criarFormularioResumoDiario();
            } else if ("🏷️ Orçamentos".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Orçamentos");
                PDVFormularioOrcamentos formularioOrcamentos = new PDVFormularioOrcamentos(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioOrcamentos.criarFormularioOrcamentos();
            } else if ("📊 Logs".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário fallback para Logs");
                formularioPanel = renderer.criarFormularioFallback(
                    subMenuText, "CONFIGURACOES", dataManager.getNomeUsuario(), dataManager.getUsuarioAtual()
                );
            } else if ("💰 Fechar Caixa".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Fechar Caixa");
                PDVFormularioFecharCaixa formularioFecharCaixa = new PDVFormularioFecharCaixa(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioFecharCaixa.criarFormularioFecharCaixa();
            } else if ("📦 Gestão Produto".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Gestão Produto");
                PDVFormularioGestaoProduto formularioGestaoProduto = new PDVFormularioGestaoProduto(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioGestaoProduto.criarFormularioGestaoProduto();
            } else if ("🏭 Gestão Fornecedores".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Gestão Fornecedores");
                PDVFormularioFornecedor formularioFornecedor = new PDVFormularioFornecedor(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioFornecedor.criarFormularioFornecedor();
            } else if ("⚙️ Configurações Principais".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Configurações Principais");
                PDVFormularioConfiguracoes formularioConfiguracoes = new PDVFormularioConfiguracoes(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioConfiguracoes.criarFormularioConfiguracoes();
            } else if ("💰 Gestão de Caixa".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Gestão de Caixa");
                PDVFormularioCaixa formularioCaixa = new PDVFormularioCaixa(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioCaixa.criarFormularioCaixa();
            } else if ("📊 Relatórios e Análises".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Relatórios e Análises");
                PDVFormularioRelatorios formularioRelatorios = new PDVFormularioRelatorios(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioRelatorios.criarFormularioRelatorios();
            } else if ("📤 Exportar Dados".equals(subMenuText) || "📥 Importar Dados".equals(subMenuText) || 
                       "📜 Histórico de Operações".equals(subMenuText) || "⚙️ Configurações de Export/Import".equals(subMenuText) ||
                       "📊 Logs de Operações".equals(subMenuText) || "🔄 Backup Automático".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Export/Import");
                PDVFormularioExportImport formularioExportImport = new PDVFormularioExportImport(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioExportImport.criarFormularioExportImport();
            } else if ("🔔 Central de Notificações".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Central de Notificações");
                PDVFormularioNotificacoes formularioNotificacoes = new PDVFormularioNotificacoes(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioNotificacoes.criarFormularioNotificacoes();
            } else if ("� Despesas".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Despesas");
                PDVFormularioDespesas formularioDespesas = new PDVFormularioDespesas(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioDespesas.criarFormularioDespesas();
            } else if ("⚙️ Cache do Sistema".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Cache do Sistema");
                PDVFormularioCacheConfig formularioCacheConfig = new PDVFormularioCacheConfig(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioCacheConfig.criarFormularioCacheConfig();
            } else if ("🔍 Busca Avançada".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Busca Avançada");
                PDVFormularioBuscaAvancada formularioBuscaAvancada = new PDVFormularioBuscaAvancada(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioBuscaAvancada.criarFormularioBuscaAvancada();
            } else if ("⚙️ Configuração de APIs".equals(subMenuText) || "💳 Transações".equals(subMenuText) || 
                       "🔗 Webhooks".equals(subMenuText) || "📊 Relatórios de Pagamentos".equals(subMenuText) ||
                       "🧪 Testar Conexões".equals(subMenuText) || "📋 Histórico".equals(subMenuText)) {
                SystemLogger.ui("Usando formulário especializado para Pagamentos API");
                PDVFormularioPagamentoAPI formularioPagamentoAPI = new PDVFormularioPagamentoAPI(
                    workArea, dataManager.getUsuarioAtual(), dataManager.getNomeUsuario()
                );
                formularioPanel = formularioPagamentoAPI.criarFormularioPagamentoAPI();
            } else {
                // Usar formulário fallback para outros casos
                SystemLogger.ui("Usando formulário fallback para: " + subMenuText);
                formularioPanel = renderer.criarFormularioFallback(
                    subMenuText, module, dataManager.getNomeUsuario(), dataManager.getUsuarioAtual()
                );
            }
            
            if (formularioPanel != null) {
                // Adicionar formulário ao workArea
                workArea.add(formularioPanel, BorderLayout.CENTER);
                
                // Atualizar informações da tela usando o dataManager
                dataManager.atualizarTelaAtual(subMenuText, module);
                
                // Forçar atualização
                workArea.revalidate();
                workArea.repaint();
                
                SwingUtilities.invokeLater(() -> {
                    workArea.revalidate();
                    workArea.repaint();
                });
                
                SystemLogger.ui("✅ Formulário aberto com sucesso: " + subMenuText);
                SystemLogger.ui("WorkArea size: " + workArea.getSize());
                SystemLogger.ui("Formulário components: " + formularioPanel.getComponentCount());
            } else {
                SystemLogger.error("❌ Formulário nulo para: " + subMenuText);
                JOptionPane.showMessageDialog(workArea, 
                    "Erro ao criar formulário para: " + subMenuText, 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            SystemLogger.error("❌ Erro ao abrir formulário: " + subMenuText, e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(workArea, 
                "Erro ao abrir: " + subMenuText + "\n\n" + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Obtém informações do usuário atual
     */
    public String getUsuarioAtual() {
        return dataManager.getUsuarioAtual();
    }
    
    public String getNomeUsuario() {
        return dataManager.getNomeUsuario();
    }
    
    /**
     * Obtém informações da tela atual
     */
    public String getTelaAtual() {
        return dataManager.getTelaAtual();
    }
    
    public String getModuloAtual() {
        return dataManager.getModuloAtual();
    }
    
    public String getTipoTelaAtual() {
        return dataManager.getTipoTelaAtual();
    }
    
    /**
     * Obtém estatísticas do menu
     */
    public java.util.Map<String, Object> getEstatisticasMenus() {
        return dataManager.getEstatisticasMenus();
    }
}

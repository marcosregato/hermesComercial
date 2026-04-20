package com.br.hermescomercial.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Classe de utilitários para UI do PDV
 * Contém métodos comuns para formatação, validação e interação com usuário
 */
public class PDVUIUtils {
    
    private static final DateTimeFormatter FORMATADOR_DATA_HORA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATADOR_DATA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // private static final DateTimeFormatter FORMATADOR_MOEDA = 
    //     DateTimeFormatter.ofPattern("R$ #,##0.00"); - não utilizado
    
    /**
     * Formata valor monetário para exibição
     */
    public static String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor);
    }
    
    /**
     * Formata valor monetário sem símbolo
     */
    public static String formatarMoedaSemSimbolo(BigDecimal valor) {
        if (valor == null) return "0,00";
        return String.format("%,.2f", valor);
    }
    
    /**
     * Formata data e hora para exibição
     */
    public static String formatarDataHora(LocalDateTime data) {
        if (data == null) return "";
        return data.format(FORMATADOR_DATA_HORA);
    }
    
    /**
     * Formata data para exibição
     */
    public static String formatarData(LocalDateTime data) {
        if (data == null) return "";
        return data.format(FORMATADOR_DATA);
    }
    
    /**
     * Valida se string representa um valor decimal válido
     */
    public static boolean validarValorDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) return false;
        return valor.matches("\\d*(\\.\\d{0,2})?") || valor.matches("\\d*(\\,\\d{0,2})?");
    }
    
    /**
     * Converte string para BigDecimal
     */
    public static BigDecimal converterParaDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(valor.replace(",", "."));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Mostra alerta informativo
     */
    public static void mostrarAlertaInformacao(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    /**
     * Mostra alerta de aviso
     */
    public static void mostrarAlertaAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    /**
     * Mostra alerta de erro
     */
    public static void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    /**
     * Mostra diálogo de confirmação
     */
    public static boolean confirmarAcao(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
    
    /**
     * Cria lista observável a partir de lista comum
     */
    public static <T> javafx.collections.ObservableList<T> criarListaObservavel(List<T> lista) {
        if (lista == null) return FXCollections.observableArrayList();
        return FXCollections.observableArrayList(lista);
    }
    
    /**
     * Valida se string não é nula ou vazia
     */
    public static boolean validarStringNaoVazia(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida se número é positivo
     */
    public static boolean validarNumeroPositivo(BigDecimal numero) {
        return numero != null && numero.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Valida se número é positivo ou zero
     */
    public static boolean validarNumeroNaoNegativo(BigDecimal numero) {
        return numero != null && numero.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    /**
     * Trunca texto para exibição
     */
    public static String truncarTexto(String texto, int tamanhoMaximo) {
        if (texto == null) return "";
        if (texto.length() <= tamanhoMaximo) return texto;
        return texto.substring(0, tamanhoMaximo - 3) + "...";
    }
    
    /**
     * Centraliza texto com espaços
     */
    public static String centralizarTexto(String texto, int tamanho) {
        if (texto == null) return " ".repeat(tamanho);
        if (texto.length() >= tamanho) return texto.substring(0, tamanho);
        
        int espacosEsquerda = (tamanho - texto.length()) / 2;
        int espacosDireita = tamanho - texto.length() - espacosEsquerda;
        
        return " ".repeat(espacosEsquerda) + texto + " ".repeat(espacosDireita);
    }
    
    /**
     * Alinha texto à direita com espaços
     */
    public static String alinharDireita(String texto, int tamanho) {
        if (texto == null) return " ".repeat(tamanho);
        if (texto.length() >= tamanho) return texto.substring(0, tamanho);
        
        return " ".repeat(tamanho - texto.length()) + texto;
    }
}

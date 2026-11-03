package br.com.Juan_J0rge.gestao_custos.custom_message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {
    // vai retornar um json mais bonito de se ver no body da api
    private String message;
    private  String type;
}

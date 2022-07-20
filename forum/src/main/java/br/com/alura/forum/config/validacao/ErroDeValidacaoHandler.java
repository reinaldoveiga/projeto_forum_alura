package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 
 * @author Reinaldo
 * O RestControllerAdvice é um interceptador, toda vez que ocorrer um exception,
 *  o spring chama esse interceptador onde a gente faz o tratamento apropriado
 */
@RestControllerAdvice
public class ErroDeValidacaoHandler {//vai realizar o tratamento de erro
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * 
	 * Para tratar os erros de validação do Bean Validation e personalizar o JSON,
	 * que será devolvido ao cliente da API, com as mensagens de erro, devemos criar 
	 * um método na classe @RestControllerAdvice e anotá-lo com @ExceptionHandler e 
	 * @ResponseStatus.
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDTO> handler(MethodArgumentNotValidException exception) {
		List<ErroDeFormularioDTO> dto = new ArrayList<>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		fieldErrors.forEach(e ->{
			// através do LocaleContextHolder o spring interpreta de onde/idioma é para pegar essa mensagem
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDTO erro = new ErroDeFormularioDTO(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
	}

}

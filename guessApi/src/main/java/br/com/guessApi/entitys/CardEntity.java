package br.com.guessApi.entitys;

import java.util.UUID;

import br.com.guessApi.enums.LevelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Card")
public class CardEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@NotBlank(message = "Field Identifier is Required")
	private String identifier;
	
	@Column(unique = true)
	@NotBlank(message = "Field Question is Required")
	private String question;
	
	
	private String photo;
	
	private LevelType level;
	
	
	
	
	
	

}

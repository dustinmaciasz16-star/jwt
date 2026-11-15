package com.example.jwt.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "item")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private String autor;
	@Column(nullable = false)
	private String titulo;
	@Column(name = "mime_type")
	private String mimeType;
	@Column(name = "foto", columnDefinition = "bytea")
	private byte[] foto;

	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Item(Integer id, String autor, String titulo, String mimeType, byte[] foto) {
		super();
		this.id = id;
		this.autor = autor;
		this.titulo = titulo;
		this.mimeType = mimeType;
		this.foto = foto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

}

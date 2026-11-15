package com.example.jwt.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.jwt.entidades.Item;
import com.example.jwt.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemController {

	private final ItemRepository itemRepo;

	public ItemController(ItemRepository itemRepo) {
		super();
		this.itemRepo = itemRepo;
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registrarVehiculo(@RequestParam("file") MultipartFile file,
			@RequestParam("autor") String autor, @RequestParam("titulo") String titulo) {
		try {
			Item item = new Item();
			item.setAutor(autor);
			item.setTitulo(titulo);
			item.setMimeType(file.getContentType());
			item.setFoto(file.getBytes());

			itemRepo.save(item);

			return ResponseEntity.status(HttpStatus.CREATED).body("Item registrado con éxito");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al precesar el archivo" + e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> listarVehiculos() {
		List<Item> lista = itemRepo.findAll();
		for (Item i : lista) {
			i.setFoto(null);
		}
		return ResponseEntity.ok(lista);

	}

	@GetMapping("/{id}/foto")
	public ResponseEntity<byte[]> obtenerFoto(@PathVariable Integer id) {
		Item vehiculo = itemRepo.findById(id).orElseThrow(() -> new RuntimeException("Item no Encontrado"));

		String mimeType = vehiculo.getMimeType();
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.parseMediaType(mimeType));

		return new ResponseEntity<>(vehiculo.getFoto(), headers, HttpStatus.OK);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarItem(@PathVariable Integer id) {
		Optional<Item> encontrado = itemRepo.findById(id);
		if (encontrado.isPresent()) {
			itemRepo.deleteById(id);
			return ResponseEntity.ok("Item eliminado con éxito");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item no encontrado para eliminar");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizarItem(
			@PathVariable Integer id,
			@RequestParam("autor") String autor,
			@RequestParam("titulo") String titulo,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			Optional<Item> encontrado = itemRepo.findById(id);
			if (!encontrado.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item no encontrado para actualizar");
			}

			Item itemExistente = encontrado.get();
			itemExistente.setAutor(autor);
			itemExistente.setTitulo(titulo);

			if (file != null && !file.isEmpty()) {
				itemExistente.setMimeType(file.getContentType());
				itemExistente.setFoto(file.getBytes());
			}

			itemRepo.save(itemExistente);
			return ResponseEntity.ok("Item actualizado con éxito");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al actualizar el item: " + e.getMessage());
		}
	}
}

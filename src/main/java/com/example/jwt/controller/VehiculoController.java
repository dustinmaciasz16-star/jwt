package com.example.jwt.controller;

import java.util.List;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.jwt.entidades.Vehiculo;
import com.example.jwt.repositories.VehiculoRepository;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "http://localhost:5173")
public class VehiculoController {
	private final VehiculoRepository vehiculoRepo;

	public VehiculoController(VehiculoRepository vehiculoRepo) {
		this.vehiculoRepo = vehiculoRepo;
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registrarVehiculo(@RequestParam("file") MultipartFile file,
			@RequestParam("marca") String marca, @RequestParam("modelo") String modelo) {
		try {
			Vehiculo vehiculo = new Vehiculo();
			vehiculo.setMarca(marca);
			vehiculo.setModelo(modelo);
			vehiculo.setMimeType(file.getContentType());
			vehiculo.setFoto(file.getBytes());

			vehiculoRepo.save(vehiculo);

			return ResponseEntity.status(HttpStatus.CREATED).body("Vehiculo registrado con éxito");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al precesar el archivo" + e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<?> listarVehiculos(){
		List<Vehiculo> lista = vehiculoRepo.findAll();
		for(Vehiculo v: lista) {
			v.setFoto(null);
		}
		return ResponseEntity.ok(lista);
		
	}
	
	@GetMapping("/{id}/foto")
	public ResponseEntity<byte[]> obtenerFoto(@PathVariable Integer id){
		Vehiculo vehiculo = vehiculoRepo.findById(id).orElseThrow(()-> new RuntimeException("Vehiculo no Encontrado"));
		
		String mimeType = vehiculo.getMimeType();
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.parseMediaType(mimeType));
		
		return new ResponseEntity<>(vehiculo.getFoto(), headers, HttpStatus.OK);
		
	}
}

package com.api.company_manager.controllers;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.company_manager.dtos.VehicleDto;
import com.api.company_manager.models.VehicleModel;
import com.api.company_manager.services.VehicleService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService service;

    @PostMapping
    public ResponseEntity<Object> createVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
        Optional<VehicleModel> existingVehicle = service.findByPlate(vehicleDto.getPlaca());
        if (existingVehicle.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a car with this license plate");
        }
        var vehicleModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleDto, vehicleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(vehicleModel));
    }

    @GetMapping
    public ResponseEntity<List<VehicleModel>> getAllVehicles() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getVehicleById(@PathVariable(value = "id") Integer id) {
        VehicleModel vehicle = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVehicle(
        @PathVariable(value = "id") Integer id,
        @RequestBody @Valid VehicleDto vehicleDto
    ) {
        VehicleModel vehicle = service.findById(id);

        Optional<VehicleModel> existingVehicle = service.findByPlate(vehicleDto.getPlaca());

        if (existingVehicle.isPresent() && !existingVehicle.get().getPlaca().equals(vehicle.getPlaca())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a car with this license plate");
        }
        
        var vehicleModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleDto, vehicleModel);
        vehicleModel.setId(vehicle.getId());
        return ResponseEntity.status(HttpStatus.OK).body(service.save(vehicleModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable(value = "id") Integer id) {
        VehicleModel vehicle = service.findById(id);

        service.delete(vehicle);
        return ResponseEntity.status(HttpStatus.OK).body("Vehicle deleted successfully");
    }
}

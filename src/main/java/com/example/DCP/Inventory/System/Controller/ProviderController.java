package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.ProviderEntity;
import com.example.DCP.Inventory.System.Service.ProviderService;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllProviders() {
        List<ProviderEntity> providers = providerService.getAllProviders();
        return Response.response(HttpStatus.OK, "Providers retrieved successfully", providers);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getProviderById(@PathVariable Long id) {
        ProviderEntity provider = providerService.getProviderById(id);
        if (provider != null) {
            return Response.response(HttpStatus.OK, "Provider found", provider);
        }
        return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Provider not found");
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createProvider(@RequestBody ProviderEntity provider) {
        ProviderEntity createdProvider = providerService.saveProvider(provider);
        return Response.response(HttpStatus.CREATED, "Provider created successfully", createdProvider);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateProvider(@PathVariable Long id, @RequestBody ProviderEntity providerDetails) {
        try {
            ProviderEntity updatedProvider = providerService.updateProvider(id, providerDetails);
            return Response.response(HttpStatus.OK, "Provider updated successfully", updatedProvider);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Provider not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "Provider deleted successfully");
    }
}

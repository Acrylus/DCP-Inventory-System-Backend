package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.ProviderEntity;
import com.example.DCP.Inventory.System.Repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public List<ProviderEntity> getAllProviders() {
        return providerRepository.findAll();
    }

    public ProviderEntity getProviderById(Long id) {
        return providerRepository.findById(id).orElse(null);
    }

    public ProviderEntity saveProvider(ProviderEntity provider) {
        return providerRepository.save(provider);
    }

    public ProviderEntity updateProvider(Long id, ProviderEntity providerDetails) {
        Optional<ProviderEntity> existingProviderOpt = providerRepository.findById(id);

        if (existingProviderOpt.isPresent()) {
            ProviderEntity existingProvider = existingProviderOpt.get();
            existingProvider.setName(providerDetails.getName());
            existingProvider.setSpeed(providerDetails.getSpeed());
            existingProvider.setUnit(providerDetails.getUnit());

            return providerRepository.save(existingProvider);
        } else {
            throw new RuntimeException("Provider not found");
        }
    }

    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }
}

package vn.jully.website_selling_technology_backend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.RoleDTO;
import vn.jully.website_selling_technology_backend.entities.Role;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public Role insertRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(Long id, RoleDTO roleDTO) throws DataNotFoundException {
        Role existingRole = getRole(id);
        modelMapper.map(roleDTO, existingRole);
        roleRepository.save(existingRole);
        return existingRole;
    }

    @Override
    public Role getRole(Long id) throws DataNotFoundException {
        return roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Role with ID = " + id));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
